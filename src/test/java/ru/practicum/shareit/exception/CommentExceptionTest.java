package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.CommentException;
import ru.practicum.shareit.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CommentExceptionTest {
    @Test
    public void shouldCommentNotAuthorNotBookingExceptionTest() {
        ValidationException.ErrorResponse errorResponse = new ValidationException.ErrorResponse("Комментарии доступны только пользователям," +
                " ранее бронировавшим предмет.",
                "Комментарии доступны только пользователям, ранее бронировавшим предмет.");
        CommentException exception = new CommentException(
                "Комментарии доступны только пользователям, ранее бронировавшим предмет.");

        assertNotNull(errorResponse);
        assertNotNull(exception);
        assertEquals(errorResponse.getDescription(), exception.getMessage());
        assertEquals(errorResponse.getError(), exception.getMessage());

    }
}
