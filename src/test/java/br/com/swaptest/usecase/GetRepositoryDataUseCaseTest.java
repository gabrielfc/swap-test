package br.com.swaptest.usecase;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import br.com.swaptest.infra.client.GitHubClient;
import br.com.swaptest.infra.client.WebhookClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetRepositoryDataUseCaseTest {

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private WebhookClient webhookClient;

    @InjectMocks
    private GetRepositoryDataUseCase getRepositoryDataUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessRepositoryData_ValidData() {
        String owner = "validUser";
        String repo = "validRepo";

        Contributor contributor = new Contributor("contributor", 1);
        Issue issue = new Issue("issue", new Issue.User("user"), null, LocalDate.now().atTime(OffsetTime.now()));

        when(gitHubClient.fetchContributors(owner, repo)).thenReturn(Flux.just(contributor));
        when(gitHubClient.fetchIssues(owner, repo)).thenReturn(Flux.just(issue));
        when(webhookClient.sendToExternalEndpoint(any(), any(), any(), any())).thenReturn(Mono.empty());

        Mono<Void> result = getRepositoryDataUseCase.processRepositoryData(owner, repo);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(gitHubClient).fetchContributors(owner, repo);
        verify(gitHubClient).fetchIssues(owner, repo);
        verify(webhookClient).sendToExternalEndpoint(owner, repo, List.of(issue), List.of(contributor));
    }

    @Test
    void testProcessRepositoryData_InvalidOwner() {
        String owner = "invalid_user"; // Supondo que isso seja inválido.
        String repo = "validRepo";

        Mono<Void> result = getRepositoryDataUseCase.processRepositoryData(owner, repo);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException && throwable.getMessage().equals(GetRepositoryDataUseCase.ERROR_OWNER_MSG))
                .verify();

        verifyNoInteractions(gitHubClient, webhookClient);
    }

    @Test
    void testProcessRepositoryData_InvalidRepo() {
        String owner = "validUser";
        String repo = "invalidRepo"; // Supondo que isso seja inválido.

        Mono<Void> result = getRepositoryDataUseCase.processRepositoryData(owner, repo);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException && throwable.getMessage().equals(GetRepositoryDataUseCase.ERROR_REPO_MSG))
                .verify();

        verifyNoInteractions(gitHubClient, webhookClient);
    }

    @Test
    void testProcessRepositoryData_GitHubClientError() {
        String owner = "validUser";
        String repo = "validRepo";

        when(gitHubClient.fetchContributors(owner, repo)).thenReturn(Flux.error(new RuntimeException("GitHub error")));

        Mono<Void> result = getRepositoryDataUseCase.processRepositoryData(owner, repo);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException && throwable.getMessage().contains("Erro ao buscar informações no github. Detalhes: GitHub error"))
                .verify();

        verify(gitHubClient).fetchContributors(owner, repo);
        verifyNoMoreInteractions(webhookClient);
    }


    @Test
    void testProcessIssuesForDate_Success() {
        String owner = "validUser";
        String repo = "validRepo";
        Contributor contributor = new Contributor("contributor", 1);
        Issue issue = new Issue("issue", new Issue.User("user"), null, LocalDate.now().atTime(OffsetTime.now()));

        // Mocking the behavior of GitHubClient
        when(gitHubClient.fetchContributors(owner, repo)).thenReturn(Flux.just(contributor));
        when(gitHubClient.fetchIssues(owner, repo)).thenReturn(Flux.just(issue));
        when(webhookClient.sendToExternalEndpoint(any(), any(), any(), any())).thenReturn(Mono.empty());

        // Process the repository data
        getRepositoryDataUseCase.processRepositoryData(owner, repo).block();

        // Verify that the webhook client method was called correctly
        verify(webhookClient).sendToExternalEndpoint(owner, repo, List.of(issue), List.of(contributor));
    }

    @Test
    void testProcessIssuesForDate_WebhookError() {
        String owner = "validUser";
        String repo = "validRepo";
        Contributor contributor = new Contributor("contributor", 1);
        Issue issue = new Issue("issue", new Issue.User("user"), null, LocalDate.now().atTime(OffsetTime.now()));

        // Mocking the behavior of GitHubClient
        when(gitHubClient.fetchContributors(owner, repo)).thenReturn(Flux.just(contributor));
        when(gitHubClient.fetchIssues(owner, repo)).thenReturn(Flux.just(issue));
        when(webhookClient.sendToExternalEndpoint(any(), any(), any(), any())).thenReturn(Mono.error(new RuntimeException("Webhook error")));

        // Process the repository data
        Mono<Void> result = getRepositoryDataUseCase.processRepositoryData(owner, repo);

        // Verify that an error is thrown
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException && throwable.getMessage().contains("Erro ao enviar informações para o webhook. Detalhes: Webhook error"))
                .verify();

        // Verify the webhook client method was called
        verify(webhookClient).sendToExternalEndpoint(owner, repo, List.of(issue), List.of(contributor));
    }
}

