package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.data.GitHubData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.*;

class GitHubClientImplTest {

    private static final String REPO_NAME = "C-Sharp-Projects";

    private static final String USER = "Ayazkins";

    private static final OffsetDateTime TIME = OffsetDateTime.parse("2003-12-31T07:07:07Z");

    private static final String JSON = "{\"updated_at\": \"2003-12-31T07:07:07Z\"," + "\"name\": \"C-Sharp-Projects\"}";
    private final GitHubData gitHubData = new GitHubData(TIME, REPO_NAME);

    @Test
    public void GitHubTest() {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());

        stubFor(
            get(urlEqualTo("/repos/" + USER +"/" +REPO_NAME)).
                willReturn(aResponse().
                    withStatus(200).
                    withHeader("Content-type", "application/json").
                    withBody(JSON))
        );
        GitHubClient gitHubClient = new GitHubClientImpl(wireMockServer.baseUrl());
        GitHubData gitHubDataReal = gitHubClient.checkRepo(USER, REPO_NAME);
        assertEquals(gitHubDataReal, gitHubData);
        wireMockServer.stop();

    }
}
