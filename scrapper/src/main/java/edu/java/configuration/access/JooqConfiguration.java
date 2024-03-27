package edu.java.configuration.access;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.repository.jooq.JooqChatLinkRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.jooq.JooqChatService;
import edu.java.service.jooq.JooqLinkService;
import edu.java.service.jooq.JooqUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqConfiguration {
    @Bean
    public LinkService linkService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository
    ) {
        return new JooqLinkService(
            linkRepository,
            chatLinkRepository
        );
    }

    @Bean
    public ChatService chatService(
        JooqChatRepository chatRepository
    ) {
        return new JooqChatService(
            chatRepository
        );
    }

    @Bean
    public JooqUpdater linkUpdater(
        GitHubClient gitHubClient,
        StackOverflowClient stackOverflowClient,
        JooqChatLinkRepository chatLinkRepository,
        BotClient botClient,
        JooqLinkRepository linkRepository
    ) {
        return new JooqUpdater(
            gitHubClient,
            stackOverflowClient,
            chatLinkRepository,
            botClient,
            linkRepository
        );
    }
}
