package edu.java.configuration;

import edu.java.clients.BotClient;
import edu.java.requests.LinkUpdateRequest;
import edu.java.service.HttpUpdater;
import edu.java.service.KafkaUpdater;
import edu.java.service.MessageUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class UpdateConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public MessageUpdater httpUpdater(BotClient botClient) {
        return new HttpUpdater(botClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public MessageUpdater kafkaUpdater(KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate) {
        return new KafkaUpdater(kafkaTemplate);
    }
}
