package edu.java.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import edu.java.service.LinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LinksController.class)
public class LinksControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private LinkService linkService;
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetLinks() throws Exception {
        when(linkService.findAll(anyLong())).thenReturn(new ListLinksResponse(new ArrayList<>(), 0));
        mockMvc.perform(get("/links")
                .header("chatId", 12345))
            .andExpect(status().isOk())
            .andExpect(content().string("links"));
    }

    @Test
    public void testAddLink() throws Exception {
        when(linkService.add(anyLong(), new AddLinkRequest(anyString()))).thenReturn(new LinkResponse(1L, "http://example.com"));
        AddLinkRequest request = new AddLinkRequest("http://example.com");
        mockMvc.perform(post("/links")
                .header("chatId", 12345)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteLink() throws Exception {
        when(linkService.remove(anyLong(), new RemoveLinkRequest(anyString()))).thenReturn(new LinkResponse(1L, "http://example.com"));
        RemoveLinkRequest request = new RemoveLinkRequest("http://example.com");
        mockMvc.perform(delete("/links")
                .header("chatId", 12345)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("http://example.com is deleted"));
    }
}
