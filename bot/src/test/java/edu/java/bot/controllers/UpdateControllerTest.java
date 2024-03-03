package edu.java.bot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.requests.AddLinkRequest;
import edu.java.bot.requests.LinkUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UpdateController.class)
public class UpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUpdateController() throws Exception {
        LinkUpdateRequest request = new LinkUpdateRequest(1L, "https://example.com", "", List.of(1L));
        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("test"));
    }
}
