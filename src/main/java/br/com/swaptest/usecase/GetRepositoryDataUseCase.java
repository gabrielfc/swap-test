package br.com.swaptest.usecase;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import br.com.swaptest.domain.ResponseDataBody;
import br.com.swaptest.infra.rest.client.GitHubClient;
import br.com.swaptest.infra.rest.client.WebhookClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetRepositoryDataUseCase {
    private final GitHubClient gitHubClient;
    private final WebhookClient webhookClient;

    public GetRepositoryDataUseCase(GitHubClient gitHubClient, WebhookClient webhookClient) {
        this.gitHubClient = gitHubClient;
        this.webhookClient = webhookClient;
    }

    public Mono<Void> execute(String user, String repository) {
        return Flux.fromStream(LocalDate.now().minusDays(30).datesUntil(LocalDate.now().plusDays(1)))
                .flatMap(date -> gatherDataForDate(user, repository, date))
                .then();
    }

    private Mono<Void> gatherDataForDate(String user, String repository, LocalDate date) {
        Mono<List<Issue>> issuesMono = gitHubClient.getIssues(user, repository, date).collectList();
        Mono<List<Contributor>> contributorsMono = gitHubClient.getContributors(user, repository).collectList();

        return Mono.zip(issuesMono, contributorsMono)
                .flatMap(data -> {
                    ResponseDataBody repositoryData = new ResponseDataBody(user, repository, date, data.getT1(), data.getT2());
                    return webhookClient.sendToWebhook(repositoryData);
                });
    }
}