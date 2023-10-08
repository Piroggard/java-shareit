package ru.practicum.shareit.exception;

public class ValidationData extends RuntimeException {
    public ValidationData(String message) {
        super(message);
    }
}
