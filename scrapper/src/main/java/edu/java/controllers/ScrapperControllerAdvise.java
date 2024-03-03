package edu.java.controllers;

import edu.java.exceptions.ChatAlreadyExistException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.exceptions.LinkAlreadyAdded;
import edu.java.responses.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScrapperControllerAdvise {
    private final static String INVALID_PARAM = "Invalid params";
    private final static String CHAT_IS_ALREADY_IN_USE = "Chat already is registered";

    private final static String LINK_IS_ADDED = "Link is already tracked";

    private final static String CHAT_NOT_FOUND = "Chat is not registered";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleBadRequest(Exception exception) {
        return ApiErrorResponse.create(INVALID_PARAM, String.valueOf(HttpStatus.BAD_REQUEST.value()), exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ChatAlreadyExistException.class)
    public ApiErrorResponse handleChatAlreadyExistException(Exception exception) {
        return ApiErrorResponse.create(CHAT_IS_ALREADY_IN_USE, String.valueOf(HttpStatus.CONFLICT.value()), exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(LinkAlreadyAdded.class)
    public ApiErrorResponse handleLinkAlreadyAdded(Exception exception) {
        return ApiErrorResponse.create(LINK_IS_ADDED, String.valueOf(HttpStatus.CONFLICT.value()), exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ChatNotFoundException.class)
    public ApiErrorResponse handleChatNotFoundException(Exception exception) {
        return ApiErrorResponse.create(CHAT_NOT_FOUND, String.valueOf(HttpStatus.NOT_FOUND.value()), exception);
    }
}
