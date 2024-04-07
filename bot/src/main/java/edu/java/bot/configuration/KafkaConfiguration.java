package edu.java.bot.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> containerFactory(
        ConsumerFactory<String, String> consumerFactory
    ) {
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
            containerFactory.setConcurrency(1);
            containerFactory.setConsumerFactory(consumerFactory);
            return containerFactory;
    }

    @Bean
    public NewTopic dlqTopic(ApplicationConfig applicationConfig) {
        ApplicationConfig.Topic dlq = applicationConfig.dlqTopic();
        return TopicBuilder.name(dlq.name()).replicas(dlq.replicas()).partitions(dlq.partitions()).build();
    }
}
