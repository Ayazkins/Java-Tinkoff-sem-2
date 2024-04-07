package edu.java.bot.service;

import edu.java.bot.exceptions.ExitedLimitRequestsException;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitHandler implements HandlerInterceptor {
    @Autowired
    private LimitService limitService;

    @Override
    public boolean preHandle(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler
    ) throws ExitedLimitRequestsException {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = request.getRemoteAddr();
        }
        Bucket bucket = limitService.resolve(ip);

        if (bucket.tryConsume(1)) {
            return true;
        } else {
            throw new ExitedLimitRequestsException();
        }

    }

}
