package edu.java.utils;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class LinearRetry extends Retry {
    private final int maxAttempts;

    private final Duration minDelay;

    private Predicate<Throwable> filter;

    private BiFunction<LinearRetry, RetrySignal, Throwable> retryGenerator;

    public LinearRetry(int maxAttempts, Duration minDelay) {
        this.maxAttempts = maxAttempts;
        this.minDelay = minDelay;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return flux.flatMap(this::generateRetry);
    }

    public LinearRetry filter(Predicate<Throwable> filter) {
        this.filter = filter;
        return this;
    }

    public LinearRetry onRetryThrow(BiFunction<LinearRetry, RetrySignal, Throwable> retryGenerator) {
        this.retryGenerator = retryGenerator;
        return this;
    }

    public Mono<Long> generateRetry(RetrySignal signal) {
        RetrySignal retrySignal = signal.copy();
        if (!filter.test(signal.failure())) {
            return Mono.error(signal.failure());
        }

        if (signal.totalRetries() < maxAttempts) {
            Duration delay = minDelay.multipliedBy(signal.totalRetries());
            return Mono.delay(delay).thenReturn(signal.totalRetries());
        }
        return Mono.error(retryGenerator.apply(this, retrySignal));
    }
}
