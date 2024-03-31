package edu.java.bot.configuration;

import edu.java.bot.service.LimitService;
import edu.java.bot.service.LimitServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LimitServiceConfig {
    @Bean LimitService limitService(ApplicationConfig applicationConfig) {
        return new LimitServiceImpl(applicationConfig);
    }
}
