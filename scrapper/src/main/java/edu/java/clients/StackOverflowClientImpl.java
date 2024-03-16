package edu.java.clients;

import edu.java.data.StackOverflowDataList;
import edu.java.entity.Link;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
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

    @Override
    public OffsetDateTime checkForUpdate(Link link) {
        try {
            URI uri = new URI(link.getUrl());
            String[] parsedLink = uri.getPath().split("/");
            Long id = Long.parseLong(parsedLink[parsedLink.length - 2]);
            StackOverflowDataList stackOverflowData = checkQuestions(id);
            return stackOverflowData.infoList().getFirst().date();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(link + " is incorrect");
        }
    }
}
