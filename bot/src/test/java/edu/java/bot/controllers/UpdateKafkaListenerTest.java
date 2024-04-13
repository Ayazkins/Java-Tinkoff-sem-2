package edu.java.bot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.requests.LinkUpdateRequest;
import edu.java.bot.service.BotService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UpdateKafkaListenerTest {

    @Mock
    private BotService botService;

    @InjectMocks
    UpdateKafkaListener updateKafkaListener;
    @Test
    public void listenerTest() {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(1L, "Test", "Test", List.of());
        updateKafkaListener.receiveUpdates(linkUpdateRequest);
        verify(botService, times(1)).update(linkUpdateRequest);
    }
}
