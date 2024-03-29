package edu.java.clients;

import edu.java.data.GitHubData;
import edu.java.data.GitHubEventsData;
import edu.java.data.Update;
import edu.java.entity.jdbc.Link;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;


public class GitHubClientImpl implements GitHubClient {
    private final static String DEFAULT_LINK = "https://api.github.com";

    private final static String METHOD = "/repos/{owner}/{repo}";

    private final static String EVENTS = "/repos/{owner}/{repo}/events";
    private final WebClient webClient;

    public GitHubClientImpl() {
        webClient = WebClient.create(DEFAULT_LINK);
    }

    public GitHubClientImpl(String link) {
        webClient = WebClient.create(link);
    }

    @Override
    public GitHubData checkRepo(String owner, String repo) {
        return webClient.get()
            .uri(METHOD, owner, repo)
            .retrieve().bodyToMono(GitHubData.class).block();
    }

    @Override
    public List<GitHubEventsData> checkEvents(String owner, String repo) {
        return webClient.get()
            .uri(EVENTS, owner, repo)
            .retrieve().bodyToFlux(GitHubEventsData.class).collectList().block();
    }

    @Override
    public Update checkForUpdate(Link link) {
        try {
            URI uri = new URI(link.getUrl());
            String[] parsedLink = uri.getPath().split("/");
            GitHubData gitHubData = checkRepo(parsedLink[1], parsedLink[2]);
            if (gitHubData.time().isAfter(link.getLastUpdated())) {
                List<GitHubEventsData> eventsData = checkEvents(parsedLink[1], parsedLink[2]);
                GitHubEventsData lastEvent = eventsData
                    .stream()
                    .max(Comparator.comparing(GitHubEventsData::createdAt))
                    .get();
                return new Update(lastEvent.createdAt(), lastEvent.eventData().message(lastEvent.payload()));
            }
            return new Update(gitHubData.time(), "no updates");
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(link + " is incorrect");
        }
    }
}
