package edu.java.bot.clients;

import edu.java.bot.requests.AddLinkRequest;
import edu.java.bot.requests.RemoveLinkRequest;
import edu.java.bot.responses.LinkResponse;
import edu.java.bot.responses.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClientImpl implements ScrapperClient {

    private final static String SCRAPPER_URL = "https://localhost:8080";
    private final static String LINKS_PATH = "/links";
    private final static String CHAT_PATH = "/tg-chat/{id}";

    private final static String CHAT_HEADER = "Tg-Chat-Id";

    private final WebClient webClient;

    public ScrapperClientImpl() {
        webClient = WebClient.create(SCRAPPER_URL);
    }

    @Override
    public String registerChat(Long id) {
        return webClient.post().uri(CHAT_PATH, id).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public String deleteChat(Long id) {
        return webClient.delete().uri(CHAT_PATH, id).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public ListLinksResponse getLinks(Long id) {
        return webClient.get()
            .uri(LINKS_PATH)
            .header(CHAT_HEADER, String.valueOf(id))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
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
            .block();
    }
}
