package ru.practicum.shareit.exception;

public class DataNotFoundException extends NullPointerException {
    public DataNotFoundException(String s) {
        super(s);
    }
}
