package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.configuration.RetryConfiguration;
import edu.java.requests.LinkUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import reactor.util.retry.Retry;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {RetryConfiguration.class})
public class BotClientImplTest {

    @Autowired
    private Retry retry;

    @Test
    public void testUpdate() {
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
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


        BotClient botClient = new BotClientImpl(wireMockServer.baseUrl(), retry);
        String response = botClient.update(linkUpdateRequest);

        assertEquals("Update request processed", response);
        wireMockServer.stop();

    }

}
