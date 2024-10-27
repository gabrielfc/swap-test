package br.com.swaptest.infra.client;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureWebTestClient
public class GitHubClientTest {

    @MockBean
    private WebClient.Builder webClientBuilder;

    private GitHubClient gitHubClient;
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        gitHubClient = new GitHubClient(webClientBuilder, "http://test.github.com");
    }

    @Test
    void testFetchIssues_RetryOnFailure() {
        when(webClient.get()).thenThrow(new RuntimeException("Service unavailable"));

        // Attempt to fetch issues; should retry based on Resilience4j configuration
        Flux<Issue> result = gitHubClient.fetchIssues("owner", "repo");

        // Verify that the retries happened
        verify(webClient, times(3)).get();
    }

    @Test
    void testFetchContributors_RetryOnFailure() {
        when(webClient.get()).thenThrow(new RuntimeException("Service unavailable"));

        // Attempt to fetch contributors; should retry based on Resilience4j configuration
        Flux<Contributor> result = gitHubClient.fetchContributors("user", "repo");

        // Verify that the retries happened
        verify(webClient, times(3)).get();
    }
}