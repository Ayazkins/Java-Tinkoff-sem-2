package edu.java.bot.configuration;

import edu.java.bot.utils.LinearRetry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
@RequiredArgsConstructor
public class RetryConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry-specification.back-off-type", havingValue = "fixed")
    public Retry fixedRetry() {
        ApplicationConfig.RetrySpecification retrySpecification = applicationConfig.retrySpecification();
        return Retry.fixedDelay(retrySpecification.maxAttempts(), retrySpecification.delay())
            .filter(this::filter)
            .jitter(retrySpecification.jitter())
            .onRetryExhaustedThrow((rbs, retrySignal) -> retrySignal.failure());
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry-specification.back-off-type", havingValue = "linear")
    public Retry linearRetry() {
        ApplicationConfig.RetrySpecification retrySpecification = applicationConfig.retrySpecification();
        return new LinearRetry(retrySpecification.maxAttempts(), retrySpecification.delay())
            .filter(this::filter)
            .onRetryThrow((rbs, retrySignal) -> retrySignal.failure());
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry-specification.back-off-type", havingValue = "exponential")
    public Retry exponentialRetry() {
        ApplicationConfig.RetrySpecification retrySpecification = applicationConfig.retrySpecification();
        Retry.max(0);
        return Retry.backoff(retrySpecification.maxAttempts(), retrySpecification.delay())
            .filter(this::filter)
            .jitter(retrySpecification.jitter())
            .onRetryExhaustedThrow((rbs, retrySignal) -> retrySignal.failure());
    }

    private boolean filter(Throwable throwable) {
        return throwable instanceof WebClientResponseException exception
            && applicationConfig.retrySpecification().codes().contains(exception.getStatusCode().value());
    }
}
