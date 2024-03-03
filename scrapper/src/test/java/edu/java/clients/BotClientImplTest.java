package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.requests.LinkUpdateRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class BotClientImplTest {

    private WireMockServer wireMockServer;

    @Before
    public void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testUpdate() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withBody("Update request processed")));
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            "test",
            "",
            List.of(1L)
        );


        BotClient botClient = new BotClientImpl(wireMockServer.baseUrl());
        String response = botClient.update(linkUpdateRequest);

        assertEquals("Update request processed", response);
    }

}
