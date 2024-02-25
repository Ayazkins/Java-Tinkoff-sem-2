package edu.java.clients;

import edu.java.data.GitHubData;

public interface GitHubClient {
    GitHubData checkRepo(String owner, String repo);
}
