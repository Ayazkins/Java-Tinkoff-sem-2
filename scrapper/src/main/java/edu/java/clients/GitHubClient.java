package edu.java.clients;

import edu.java.data.GitHubData;
import edu.java.data.GitHubEventsData;
import edu.java.data.Update;
import edu.java.entity.jdbc.Link;
import java.util.List;

public interface GitHubClient {
    GitHubData checkRepo(String owner, String repo);

    List<GitHubEventsData> checkEvents(String owner, String repo);

    Update checkForUpdate(Link link);
}
