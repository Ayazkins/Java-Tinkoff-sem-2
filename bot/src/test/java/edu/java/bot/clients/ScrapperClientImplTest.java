package edu.java.bot.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.bot.configuration.RetryConfiguration;
import edu.java.bot.responses.LinkResponse;
import edu.java.bot.responses.ListLinksResponse;
import edu.java.bot.utils.LinearRetry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = {RetryConfiguration.class})
public class ScrapperClientImplTest {

    private static WireMockServer wireMockServer;
    @Autowired
    private Retry retry;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testRegisterChat() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/123"))
                .inScenario("RetryScenario")
                .whenScenarioStateIs(STARTED)
                .willSetStateTo("Retry success")
                .willReturn(aResponse()
                .withStatus(HttpStatus.BAD_GATEWAY.value())));
        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/123"))
            .inScenario("RetryScenario")
                .whenScenarioStateIs("Retry success")
            .willReturn(aResponse()
                .withStatus(200)));

        ScrapperClient scrapperClient = new ScrapperClientImpl(wireMockServer.baseUrl(), retry);
        scrapperClient.registerChat(123L);
        wireMockServer.verify(2, postRequestedFor((urlEqualTo("/tg-chat/123"))));


    }

    @Test
    public void testDeleteChat() {
        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/456"))
            .inScenario("RetryScenario")
            .whenScenarioStateIs(STARTED)
            .willSetStateTo("Retry success")
            .willReturn(aResponse()
                .withStatus(HttpStatus.BAD_GATEWAY.value())));
        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/456"))
            .inScenario("RetryScenario")
            .whenScenarioStateIs("Retry success")
            .willReturn(aResponse()
                .withStatus(200)));

        ScrapperClient scrapperClient = new ScrapperClientImpl(wireMockServer.baseUrl(), retry);
        scrapperClient.deleteChat(456L);
        wireMockServer.verify(2, deleteRequestedFor((urlEqualTo("/tg-chat/456"))));

    }

    @Test
    public void testGetLinks() {
        wireMockServer.stubFor(get(urlEqualTo("/links"))
                .withHeader("chatId", equalTo("1"))
            .inScenario("RetryScenario")
            .whenScenarioStateIs(STARTED)
            .willSetStateTo("Retry success")
            .willReturn(aResponse()
                .withStatus(HttpStatus.BAD_GATEWAY.value())));

        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("chatId", equalTo("1"))
            .inScenario("RetryScenario")
            .whenScenarioStateIs("Retry success")
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                      "links": [
                        {
                          "id": 1,
                          "url": "test"
                        },
                        {
                          "id": 2,
                          "url": "test2"
                        }
                      ],
                      "size": 2
                    }
                    """)));

        ScrapperClient scrapperClient = new ScrapperClientImpl(wireMockServer.baseUrl(), retry);
        scrapperClient.getLinks(1L);
        wireMockServer.verify(2, getRequestedFor((urlEqualTo("/links"))));

    }
}
