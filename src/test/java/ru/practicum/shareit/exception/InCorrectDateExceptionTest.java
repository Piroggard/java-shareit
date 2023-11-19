package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.InCorrectDateException;
import ru.practicum.shareit.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class InCorrectDateExceptionTest {
    @Test
    public void shouldInCorrectDateExceptionTest() {
        ValidationException.ErrorResponse errorResponse = new ValidationException.ErrorResponse("Некорректная дата бронирования.",
                "Некорректная дата бронирования.");
        InCorrectDateException exception = new InCorrectDateException(
                "Некорректная дата бронирования.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());
    }
}
