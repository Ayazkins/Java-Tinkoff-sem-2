package edu.java.responses;

import java.util.Arrays;
import java.util.List;

public record ApiErrorResponse(
    String description,
    String code,
    String exception,
    String message,
    List<String> stackTrace
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
