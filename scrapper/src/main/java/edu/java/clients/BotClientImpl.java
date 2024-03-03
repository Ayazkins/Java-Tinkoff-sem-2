package edu.java.clients;

import edu.java.requests.LinkUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BotClientImpl implements BotClient {
    private final static String BOT_URL = "https://localhost:8090";
    private final static String UPDATES = "/updates";
    private final WebClient webClient;

    public BotClientImpl() {
        webClient = WebClient.create(BOT_URL);
    }

    @Override
    public String update(LinkUpdateRequest linkUpdateRequest) {
        return webClient
            .post()
            .uri(UPDATES)
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
