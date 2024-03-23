package edu.java.clients;

import edu.java.data.GitHubData;
import edu.java.entity.Link;
import java.time.OffsetDateTime;

public interface GitHubClient {
    GitHubData checkRepo(String owner, String repo);

    OffsetDateTime checkForUpdate(Link link);
}
