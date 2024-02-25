package edu.java.configuration;

import edu.java.clients.GitHubClientImpl;
import edu.java.clients.StackOverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GitHubClientImpl gitHubClient() {
        return new GitHubClientImpl();
    }

    @Bean
    public StackOverflowClientImpl stackOverflowClient() {
        return new StackOverflowClientImpl();
    }
}
