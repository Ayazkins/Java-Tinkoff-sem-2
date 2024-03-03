package edu.java.bot.controllers;

import edu.java.bot.responses.ApiErrorResponse;
import java.util.Arrays;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UpdateControllerException {
    private final static String DESCRIPTION = "Invalid params";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
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
