package br.com.swaptest.infra.rest.client;

import br.com.swaptest.domain.ResponseDataBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebhookClient {
    private final WebClient webClient;

    public WebhookClient(WebClient.Builder webClientBuilder, @Value("${webhook.url}") String webhookUrl) {
        this.webClient = webClientBuilder.baseUrl(webhookUrl).build();
    }

    public Mono<Void> sendToWebhook(ResponseDataBody data) {
        return webClient.post()
                .bodyValue(data)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

