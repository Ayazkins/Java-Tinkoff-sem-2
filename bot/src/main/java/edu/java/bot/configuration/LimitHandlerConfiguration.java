package edu.java.bot.configuration;

import edu.java.bot.service.RateLimitHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class LimitHandlerConfiguration implements WebMvcConfigurer {
    private final RateLimitHandler rateLimitHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitHandler);
    }
}
