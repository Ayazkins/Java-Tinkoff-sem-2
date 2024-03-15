package edu.java.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LinksControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetLinks() throws Exception {
        mockMvc.perform(get("/links")
                .header("chatId", 12345))
            .andExpect(status().isOk())
            .andExpect(content().string("links"));
    }

    @Test
    public void testAddLink() throws Exception {
        AddLinkRequest request = new AddLinkRequest("http://example.com");
        mockMvc.perform(post("/links")
                .header("chatId", 12345)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteLink() throws Exception {
        RemoveLinkRequest request = new RemoveLinkRequest("http://example.com");
        mockMvc.perform(delete("/links")
                .header("chatId", 12345)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("http://example.com is deleted"));
    }
}
