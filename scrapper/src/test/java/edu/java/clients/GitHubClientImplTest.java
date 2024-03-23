package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.data.EventData;
import edu.java.data.GitHubData;
import edu.java.data.Payload;
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

    private static final String JSON = "{\"updatedAt\": \"2003-12-31T07:07:07Z\"," + "\"name\": \"C-Sharp-Projects\"}";
    private static final String ISSUE_JSON = """
        [
         {
                "id": "36612344758",
                "type": "IssueCommentEvent",
                "payload": {
                    "action": "created",
                    "issue": {
                        "title": "hw5 ready"
                        }
                    }
                },
                {
                        "id": "36612334914",
                        "type": "PushEvent",
                        "payload": {
                            "commits": [
                                {
                                    "sha": "397d56c70a854b89b65dcf91f8a32274242fdaa9",
                                    "author": {
                                        "email": "10a-yahinayaz@mail.ru",
                                        "name": "Ayazkins"
                                    },
                                    "message": "fix",
                                    "distinct": true,
                                    "url": "https://api.github.com/repos/Ayazkins/Java-Tinkoff-sem-2/commits/397d56c70a854b89b65dcf91f8a32274242fdaa9"
                                }
                            ]
                        },
                        "public": true,
                        "created_at": "2024-03-16T19:27:59Z"
                    }
            ]
        """;
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

    @Test
    public void eventsTest() {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());
        stubFor(
            get(urlEqualTo("/repos/" + USER + "/" + REPO_NAME + "/" + "events"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-type", "application/json")
                    .withBody(ISSUE_JSON))
        );
        GitHubClient gitHubClient = new GitHubClientImpl(wireMockServer.baseUrl());
        var gitHubDataReal = gitHubClient.checkEvents(USER, REPO_NAME);
        assertEquals(gitHubDataReal.size(), 2);
        assertEquals(gitHubDataReal.getFirst().eventData(), EventData.ISSUE_COMMENT_EVENT);
        assertEquals(gitHubDataReal.getFirst().payload().issueComment().title(), "hw5 ready");
        assertEquals(gitHubDataReal.getLast().eventData(), EventData.PUSH_EVENT);
        assertEquals(gitHubDataReal.getLast().payload().push().getFirst().message(), "fix");
        wireMockServer.stop();
    }
}
