package edu.java.clients;

import edu.java.requests.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class BotClientImpl implements BotClient {
    private final static String BOT_URL = "http://localhost:8090";
    private final static String UPDATES = "/updates";
    private final WebClient webClient;

    private final Retry retry;

    @Autowired
    public BotClientImpl(Retry retry) {
        webClient = WebClient.create(BOT_URL);
        this.retry = retry;
    }

    public BotClientImpl(String url, Retry retry) {
        webClient = WebClient.create(url);
        this.retry = retry;
    }

    @Override
    public String update(LinkUpdateRequest linkUpdateRequest) {
        return webClient
            .post()
            .uri(UPDATES)
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .bodyToMono(String.class)
            .retryWhen(retry)
            .block();
    }
}
