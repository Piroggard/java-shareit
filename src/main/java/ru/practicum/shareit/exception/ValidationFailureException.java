package ru.practicum.shareit.exception;

public class ValidationFailureException extends RuntimeException {
    public ValidationFailureException(String message) {
        super(message);
    }
}

