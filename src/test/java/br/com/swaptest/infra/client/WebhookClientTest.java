package br.com.swaptest.infra.client;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import br.com.swaptest.infra.client.dto.ResponseDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureWebTestClient
public class WebhookClientTest {

    @MockBean
    private WebClient.Builder webClientBuilder;

    private WebhookClient webhookClient;
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        webhookClient = new WebhookClient(webClientBuilder, "http://test.webhook.com");
    }

    @Test
    void testSendToWebhook_RetryOnFailure() {
        // Simular falha na chamada ao webhook
        when(webClient.post()).thenThrow(new RuntimeException("Service unavailable"));

        // Chamar o método
        Mono<Void> result = webhookClient.sendToWebhook(new ResponseDataDTO("owner", "repo", Collections.emptyList(), Collections.emptyList()));

        // Verificar se o método foi tentado 3 vezes (de acordo com a configuração do Resilience4j)
        verify(webClient, times(3)).post();
    }

    @Test
    void testSendToExternalEndpoint() {
        // Mock dos dados
        List<Issue> issues = List.of(new Issue("Sample Issue", new Issue.User("author"), Collections.emptyList(), null));
        List<Contributor> contributors = List.of(new Contributor("contributor", 1));

        // Simular sucesso na chamada ao webhook
        // when(webClient.post()).thenReturn(mock(WebClient.RequestBodySpec.class));
        when(webClient.post().bodyValue(any(ResponseDataDTO.class))).thenReturn(mock(WebClient.RequestHeadersSpec.class));
        when(webClient.post().bodyValue(any(ResponseDataDTO.class)).retrieve()).thenReturn(mock(WebClient.ResponseSpec.class));
        when(webClient.post().bodyValue(any(ResponseDataDTO.class)).retrieve().bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Chamar o método
        Mono<Void> result = webhookClient.sendToExternalEndpoint("owner", "repo", issues, contributors);

        // Verificar se a chamada foi feita corretamente
        verify(webClient.post(), times(1)).bodyValue(any(ResponseDataDTO.class));
    }
}
