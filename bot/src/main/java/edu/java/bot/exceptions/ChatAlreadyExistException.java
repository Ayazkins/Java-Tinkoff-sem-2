package edu.java.bot.exceptions;

public class ChatAlreadyExistException extends RuntimeException {
    public ChatAlreadyExistException(String message) {
        super(message);
    }
}
