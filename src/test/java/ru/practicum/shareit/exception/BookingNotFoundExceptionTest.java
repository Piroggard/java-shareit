package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingNotFoundExceptionTest {
    @Test
    public void shouldBookingNotFoundExceptionTest() {
        ValidationException.ErrorResponse errorResponse = new ValidationException.ErrorResponse("Бронирование не найдено.",
                "Бронирование не найдено.");
        BookingNotFoundException bookingNotFoundException = new BookingNotFoundException("Бронирование не найдено.");

        assertNotNull(errorResponse);
        assertNotNull(bookingNotFoundException);
        assertEquals(errorResponse.getDescription(), bookingNotFoundException.getMessage());
        assertEquals(errorResponse.getError(), bookingNotFoundException.getMessage());
    }
}
