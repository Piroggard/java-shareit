package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.InCorrectBookingException;
import ru.practicum.shareit.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class InCorrectBookingExceptionTest {
    @Test
    public void shouldInCorrectBookingExceptionTest() {

        ValidationException.ErrorResponse errorResponse = new ValidationException.ErrorResponse("Ошибка бронирования.",
                "Ошибка бронирования.");
        InCorrectBookingException exception = new InCorrectBookingException(
                "Ошибка бронирования.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());
    }
}
