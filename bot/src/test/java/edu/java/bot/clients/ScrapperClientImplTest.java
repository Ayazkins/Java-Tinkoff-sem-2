package edu.java.bot.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.bot.responses.LinkResponse;
import edu.java.bot.responses.ListLinksResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

public class ScrapperClientImplTest {

    private static WireMockServer wireMockServer;

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
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withBody("Chat registration successful")));

        ScrapperClient scrapperClient = new ScrapperClientImpl(wireMockServer.baseUrl());
        String response = scrapperClient.registerChat(123L);

        assertEquals("Chat registration successful", response);
    }

    @Test
    public void testDeleteChat() {
        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/456"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withBody("Chat deletion successful")));

        ScrapperClient scrapperClient = new ScrapperClientImpl(wireMockServer.baseUrl());
        String response = scrapperClient.deleteChat(456L);

        assertEquals("Chat deletion successful", response);
    }

    @Test
    public void testGetLinks() {
        ListLinksResponse expectedResponse = new ListLinksResponse(List.of(
            new LinkResponse(1L, "test"),
            new LinkResponse(2L, "test2")), 2);

        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
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

        ScrapperClient scrapperClient = new ScrapperClientImpl(wireMockServer.baseUrl());
        ListLinksResponse response = scrapperClient.getLinks(1L);

        assertEquals(expectedResponse, response);
    }
}
