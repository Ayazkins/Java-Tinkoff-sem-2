package edu.java.bot.controllers;

import edu.java.bot.exceptions.ExitedLimitRequestsException;
import edu.java.bot.responses.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class UpdateControllerException {
    private final static String DESCRIPTION = "Invalid params";

    private final static String TOO_MANY_REQUEST_DESCRIPTION = "Wait a bit, too many requests";

    @ExceptionHandler({ExitedLimitRequestsException.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiErrorResponse handleTooManyRequests(Exception exception, WebRequest request) {
        return new ApiErrorResponse(
            TOO_MANY_REQUEST_DESCRIPTION,
            String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentNotValidException.class,
        WebClientResponseException.class})
    public ApiErrorResponse handleBadRequest(Exception exception) {
        return new ApiErrorResponse(
            DESCRIPTION,
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
