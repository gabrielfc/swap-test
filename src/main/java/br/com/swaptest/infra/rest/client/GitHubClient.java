package br.com.swaptest.infra.rest.client;


import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
public class GitHubClient {
    private final WebClient webClient;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;

    public GitHubClient(WebClient.Builder webClientBuilder,  @Value("${github.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Flux<Issue> getIssues(String user, String repository, LocalDate date) {
        return webClient.get()
                .uri("/repos/{user}/{repo}/issues", user, repository)
                .retrieve()
                .bodyToFlux(Issue.class);
    }

    public Flux<Contributor> getContributors(String user, String repository) {
        return webClient.get()
                .uri("/repos/{user}/{repo}/contributors", user, repository)
                .retrieve()
                .bodyToFlux(Contributor.class);
    }


}

