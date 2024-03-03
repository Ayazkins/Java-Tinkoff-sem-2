package edu.java.bot.responses;

import java.util.Arrays;
import java.util.List;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    List<String> stacktrace
) {
    public static ApiErrorResponse create(String description, String code, Exception exception) {
        return new ApiErrorResponse(
            description,
            code,
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
