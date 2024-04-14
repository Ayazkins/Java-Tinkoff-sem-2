package edu.java.configuration.access;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.repository.jpa.JpaChatLinkRepository;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.service.MessageUpdater;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfiguration {
    @Bean
    public JpaLinkService linkService(
        JpaLinkRepository linkRepository,
        JpaChatLinkRepository chatLinkRepository,
        JpaChatRepository jpaChatRepository
    ) {
        return new JpaLinkService(
            linkRepository,
            jpaChatRepository,
            chatLinkRepository
        );
    }

    @Bean
    public JpaChatService chatService(
        JpaChatRepository chatRepository
    ) {
        return new JpaChatService(
            chatRepository
        );
    }

    @Bean
    public JpaLinkUpdater linkUpdater(
        GitHubClient gitHubClient,
        StackOverflowClient stackOverflowClient,
        JpaChatLinkRepository chatLinkRepository,
        MessageUpdater messageUpdater,
        JpaLinkRepository linkRepository
    ) {
        return new JpaLinkUpdater(
            linkRepository,
            gitHubClient,
            stackOverflowClient,
            messageUpdater,
            chatLinkRepository
        );
    }
}
