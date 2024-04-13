package edu.java.bot.controllers;

import edu.java.bot.requests.LinkUpdateRequest;
import edu.java.bot.service.BotService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class UpdateKafkaListener {
    private final BotService botService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(
        topics = "${app.updates-topic.name}",
        autoStartup = "true",
        containerFactory = "containerFactory",
        groupId = "main"
    )
    public void receiveUpdates(@Payload @Valid LinkUpdateRequest payload) {
        try {
            botService.update(payload);
        } catch (Exception e) {
            kafkaTemplate.send("updates_dlq", payload.toString());
            log.error("Error", e);
        }
    }
}
