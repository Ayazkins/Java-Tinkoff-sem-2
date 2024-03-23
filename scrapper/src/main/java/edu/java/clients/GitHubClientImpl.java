package edu.java.clients;

import edu.java.data.GitHubData;
import edu.java.entity.Link;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import org.springframework.web.reactive.function.client.WebClient;


public class GitHubClientImpl implements GitHubClient {
    private final static String DEFAULT_LINK = "https://api.github.com";

    private final static String METHOD = "/repos/{owner}/{repo}";
    private final WebClient webClient;

    public GitHubClientImpl() {
        webClient = WebClient.create(DEFAULT_LINK);
    }

    public GitHubClientImpl(String link) {
        webClient = WebClient.create(link);
    }

    @Override
    public GitHubData checkRepo(String owner, String repo) {
        return webClient.get().uri(METHOD, owner, repo).retrieve().bodyToMono(GitHubData.class).block();
    }

    @Override
    public OffsetDateTime checkForUpdate(Link link) {
        try {
            URI uri = new URI(link.getUrl());
            String[] parsedLink = uri.getPath().split("/");
            GitHubData gitHubData = checkRepo(parsedLink[1], parsedLink[2]);
            return gitHubData.time();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(link + " is incorrect");
        }
    }
}
