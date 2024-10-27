package br.com.swaptest.usecase;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import br.com.swaptest.infra.client.GitHubClient;
import br.com.swaptest.infra.client.WebhookClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetRepositoryDataUseCase {

    public static final String ERROR_GITHUB_MSG = "Erro ao buscar informações no github. Detalhes: ";
    public static final String ERROR_WEBHOOK_MSG = "Erro ao enviar informações para o webhook. Detalhes: ";

    private final GitHubClient gitHubClient;
    private final WebhookClient webhookClient;

    public GetRepositoryDataUseCase(GitHubClient gitHubClient, WebhookClient webhookClient) {
        this.gitHubClient = gitHubClient;
        this.webhookClient = webhookClient;
    }

    public Mono<Void> processRepositoryData(String owner, String repo) {
        return gitHubClient.fetchContributors(owner, repo)
                .collectList()
                .onErrorResume(error -> {
                    String errorMessage = ERROR_GITHUB_MSG + error.getMessage();
                    System.err.println(errorMessage);
                    return Mono.error(new RuntimeException(errorMessage));
                })
                .flatMapMany(contributors -> gitHubClient.fetchIssues(owner, repo)
                        .groupBy(issue -> issue.createdAt().toLocalDate())
                        .flatMap(groupedFlux -> processIssuesForDate(owner, repo, groupedFlux, contributors)))
                        .onErrorResume(error -> {
                            String errorMessage = ERROR_GITHUB_MSG + error.getMessage();
                            System.err.println(errorMessage);
                            return Mono.error(new RuntimeException(errorMessage));
                        })
                .then();
    }

    private Mono<Void> processIssuesForDate(String owner, String repo, GroupedFlux<LocalDate, Issue> groupedIssues, List<Contributor> contributors) {
        return groupedIssues
                .collectList()
                .flatMap(issues -> webhookClient.sendToExternalEndpoint(owner, repo, issues, contributors))
                .onErrorResume(error -> {
                    String errorMessage = ERROR_WEBHOOK_MSG + error.getMessage();
                    System.err.println(errorMessage);
                    return Mono.error(new RuntimeException(errorMessage));
                });
    }
}