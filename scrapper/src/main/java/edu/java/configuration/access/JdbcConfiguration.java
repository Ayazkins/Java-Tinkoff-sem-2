package edu.java.configuration.access;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.repository.impl.ChatLinkRepositoryImpl;
import edu.java.repository.impl.ChatRepositoryImpl;
import edu.java.repository.impl.LinkRepositoryImpl;
import edu.java.service.ChatService;
import edu.java.service.JdbcChatService;
import edu.java.service.JdbcLinkService;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfiguration {
    @Bean
    public LinkService linkService(
        LinkRepositoryImpl linkRepository,
        ChatLinkRepositoryImpl chatLinkRepository
    ) {
        return new JdbcLinkService(
            linkRepository,
            chatLinkRepository
        );
    }

    @Bean
    public ChatService chatService(
        ChatRepositoryImpl chatRepository
    ) {
        return new JdbcChatService(
            chatRepository
        );
    }

    @Bean
    public LinkUpdater linkUpdater(
        GitHubClient gitHubClient,
        StackOverflowClient stackOverflowClient,
        ChatLinkRepositoryImpl chatLinkRepository,
        BotClient botClient,
        LinkRepositoryImpl linkRepository
    ) {
        return new LinkUpdater(
            gitHubClient,
            stackOverflowClient,
            chatLinkRepository,
            botClient,
            linkRepository
        );
    }
}
