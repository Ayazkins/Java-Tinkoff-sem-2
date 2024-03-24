package edu.java.bot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.requests.AddLinkRequest;
import edu.java.bot.requests.LinkUpdateRequest;
import edu.java.bot.responses.ApiErrorResponse;
import edu.java.bot.service.BotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import static com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.WireMockHelpers.jsonPath;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UpdateController.class)
public class UpdateControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BotService botService;
    @Autowired WebApplicationContext webApplicationContext;

    @Test
    void testHandleBadRequest() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        LinkUpdateRequest request = new LinkUpdateRequest(null, "https://example.com", "", List.of(1L));
        doNothing().when(botService).update(request);
        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
