package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.data.StackOverflowData;
import edu.java.data.StackOverflowDataList;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private static final StackOverflowDataList
        STACK_OVERFLOW_DATA =new StackOverflowDataList(List.of(new StackOverflowData(ID, OffsetDateTime.ofInstant(Instant.ofEpochSecond(1), ZoneOffset.UTC))));

    @Test
    public void StackOverflowTest() {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());

        stubFor(
            get(urlEqualTo("/questions/" + ID + "?site=stackoverflow")).
                willReturn(aResponse().
                    withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(JSON))
        );
        StackOverflowClient stackOverflowClient = new StackOverflowClientImpl(wireMockServer.baseUrl());
        StackOverflowDataList stackOverflowData = stackOverflowClient.checkQuestions(ID);
        assertEquals(STACK_OVERFLOW_DATA, stackOverflowData);
        wireMockServer.stop();
    }
}
