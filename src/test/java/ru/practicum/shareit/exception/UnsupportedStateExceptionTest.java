package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.StatusException;
import ru.practicum.shareit.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UnsupportedStateExceptionTest {
    @Test
    public void shouldUnsupportedStateExceptionTest() {
        ValidationException.ErrorResponse errorResponse = new ValidationException.ErrorResponse("Неверный статус бронирования.",
                "Неверный статус бронирования.");
        StatusException exception = new StatusException(
                "Неверный статус бронирования.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());
    }
}
