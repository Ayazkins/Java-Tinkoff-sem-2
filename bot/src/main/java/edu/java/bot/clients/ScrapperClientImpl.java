package edu.java.bot.clients;

import edu.java.bot.requests.AddLinkRequest;
import edu.java.bot.requests.RemoveLinkRequest;
import edu.java.bot.responses.LinkResponse;
import edu.java.bot.responses.ListLinksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Component
public class ScrapperClientImpl implements ScrapperClient {

    private final static String SCRAPPER_URL = "http://localhost:8080";
    private final static String LINKS_PATH = "/links";
    private final static String CHAT_PATH = "/tg-chat/{id}";

    private final static String CHAT_HEADER = "chatId";

    private final WebClient webClient;

    private final Retry retry;

    @Autowired
    public ScrapperClientImpl(Retry retry) {
        webClient = WebClient.create(SCRAPPER_URL);
        this.retry = retry;
    }

    public ScrapperClientImpl(String url, Retry retry) {
        webClient = WebClient.create(url);
        this.retry = retry;
    }

    @Override
    public String registerChat(Long id) {
        return webClient.post().uri(CHAT_PATH, id).retrieve().bodyToMono(String.class).retryWhen(retry).block();
    }

    @Override
    public String deleteChat(Long id) {
        return webClient.delete().uri(CHAT_PATH, id).retrieve().bodyToMono(String.class).retryWhen(retry).block();
    }

    @Override
    public ListLinksResponse getLinks(Long id) {
        return webClient.get()
            .uri(LINKS_PATH)
            .header(CHAT_HEADER, String.valueOf(id))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retry)
            .block();
    }

    @Override
    public LinkResponse addLink(Long id, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri(LINKS_PATH)
            .header(CHAT_HEADER, String.valueOf(id))
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }

    @Override
    public LinkResponse removeLink(Long id, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_PATH)
            .header(CHAT_HEADER, String.valueOf(id))
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }
}
