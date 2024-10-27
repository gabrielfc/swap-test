package br.com.swaptest.infra.client;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import br.com.swaptest.infra.client.dto.ResponseDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class WebhookClient {
    private final WebClient webClient;

    public WebhookClient(WebClient.Builder webClientBuilder, @Value("${webhook.url}") String webhookUrl) {
        this.webClient = webClientBuilder.baseUrl(webhookUrl).build();
    }

    public Mono<Void> sendToExternalEndpoint(String owner, String repo,  List<Issue> issues, List<Contributor> contributors) {

        ResponseDataDTO responseData = new ResponseDataDTO(owner, repo, issues, contributors);

        return sendToWebhook(responseData);
    }

    public Mono<Void> sendToWebhook(ResponseDataDTO data) {
        return webClient.post()
                .bodyValue(data)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

