package edu.java.configuration;

import edu.java.clients.BotClientImpl;
import edu.java.clients.GitHubClientImpl;
import edu.java.clients.StackOverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
public class ClientConfiguration {
    @Bean
    public GitHubClientImpl gitHubClient(Retry retry) {
        return new GitHubClientImpl(retry);
    }

    @Bean
    public StackOverflowClientImpl stackOverflowClient(Retry retry) {
        return new StackOverflowClientImpl(retry);
    }

    @Bean
    public BotClientImpl botClient(Retry retry) {
        return new BotClientImpl(retry);
    }
}
