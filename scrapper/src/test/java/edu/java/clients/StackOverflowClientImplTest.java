package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.configuration.RetryConfiguration;
import edu.java.data.StackOverflowData;
import edu.java.data.StackOverflowDataList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import reactor.util.retry.Retry;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {RetryConfiguration.class})

public class StackOverflowClientImplTest {

    private final static String JSON = """
        {
        "items": [
        {
            "tags": [
                    "java"
                  ],
            "last_activity_date": 1,
            "question_id": 1
        }]}
        """;

    private static final Long ID = 1L;

    @Autowired
    private Retry retry;
    private static final StackOverflowDataList
        STACK_OVERFLOW_DATA =new StackOverflowDataList(List.of(new StackOverflowData(ID, OffsetDateTime.ofInstant(Instant.ofEpochSecond(1), ZoneOffset.UTC))));

    @Test
    public void StackOverflowTest() {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());

        stubFor(
            get(urlEqualTo("/questions/" + ID + "?site=stackoverflow"))
            .inScenario("RetryScenario")
            .whenScenarioStateIs(STARTED)
            .willSetStateTo("Retry success")
            .willReturn(aResponse()
                .withStatus(HttpStatus.BAD_GATEWAY.value())));

        stubFor(get(urlEqualTo("/questions/" + ID + "?site=stackoverflow"))
            .inScenario("RetryScenario")
                .whenScenarioStateIs("Retry success")
            .willReturn(aResponse().withStatus(200)));

        StackOverflowClient stackOverflowClient = new StackOverflowClientImpl(wireMockServer.baseUrl(), retry);
        stackOverflowClient.checkQuestions(ID);
        wireMockServer.verify(2, getRequestedFor(urlEqualTo("/questions/" + ID + "?site=stackoverflow")));

        wireMockServer.stop();
    }
}
