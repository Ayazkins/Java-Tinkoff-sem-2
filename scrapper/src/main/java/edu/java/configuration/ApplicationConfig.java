package edu.java.configuration;

import edu.java.utils.BackoffType;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,

    @NotNull
    AccessType databaseAccessType,

    RateLimiter rateLimiter,

    RetrySpecification retrySpecification,

    @NotNull
    Topic updatesTopic,

    boolean useQueue

) {
    @Bean
    public Duration interval() {
        return scheduler.interval();
    }

    public record Topic(String name, int replicas, int partitions) {
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record RateLimiter(
        boolean enable,
        @NotNull Integer limit,
        @NotNull Integer refillPerMinute
    ) {
    }

    public record RetrySpecification(
        @NotNull BackoffType backOffType,
        @NotNull Integer maxAttempts,
        @NotNull Duration delay,
        @NotNull Double jitter,
        Set<Integer> codes
    ) {
    }
}
