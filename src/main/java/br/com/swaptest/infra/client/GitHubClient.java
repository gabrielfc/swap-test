package br.com.swaptest.infra.client;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.format.DateTimeFormatter;

@Component
public class GitHubClient {
    private final WebClient webClient;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;

    public GitHubClient(WebClient.Builder webClientBuilder, @Value("${github.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Retry(name = "githubRetry")
    public Flux<Issue> fetchIssues(String owner, String repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}/issues", owner, repo)
                .retrieve()
                .bodyToFlux(Issue.class);
    }

    @Retry(name = "githubRetry")
    public Flux<Contributor> fetchContributors(String user, String repository) {
        return webClient.get()
                .uri("/repos/{user}/{repo}/contributors", user, repository)
                .retrieve()
                .bodyToFlux(Contributor.class);
    }
}

