package edu.java.clients;

import edu.java.data.StackOverflowDataList;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClientImpl implements StackOverflowClient {
    private static final String DEFAULT_LINK = "https://api.stackexchange.com/2.3/";

    private static final String METHOD = "/questions/{id}?site=stackoverflow";


    private final WebClient webClient;

    public StackOverflowClientImpl() {
        webClient = WebClient.create(DEFAULT_LINK);
    }

    public StackOverflowClientImpl(String link) {
        webClient = WebClient.create(link);
    }

    @Override
    public StackOverflowDataList checkQuestions(Long id) {
        return webClient.get().uri(METHOD, id).retrieve().bodyToMono(StackOverflowDataList.class).block();
    }
}
