package edu.java.exceptions;

public class ChatAlreadyExistException extends RuntimeException {
    public ChatAlreadyExistException(String message) {
        super(message);
    }
}
