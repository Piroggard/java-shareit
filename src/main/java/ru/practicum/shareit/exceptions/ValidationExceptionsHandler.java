package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ValidationExceptionsHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ValidationException.ErrorResponse handleConflictException(final ConflictException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Пользователь с таким email уже существует.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationException.ErrorResponse handleNotFoundException(EntityNotFoundException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "message");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException.ErrorResponse handleValidationException(final ValidationException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Ошибка валидациии");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException.ErrorResponse handleInCorrectBookingException(final InCorrectBookingException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Ошибка бронирования");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException.ErrorResponse handleInCorrectDateException(final InCorrectDateException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Некорректная дата бронирования");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException.ErrorResponse handleUnsupportedStateException(final StatusException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Unknown state: UNSUPPORTED_STATUS");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException.ErrorResponse handleCommentNotAuthorNotBookingException(final CommentException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Комментарии разрешенны только пользователю, " +
                "бронирующему предмет");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException.ErrorResponse handleItemUnavailableException(final ItemUnavailableException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Аренда предмета недоступна");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException.ErrorResponse handleBadRequestException(final BadRequestException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Неверный запрос");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationException.ErrorResponse handleRequestNotFoundException(final RequestNotFoundException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Запрос не найден.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationException.ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Пользователь не найден.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationException.ErrorResponse handleItemNotFoundException(final ItemNotFoundException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Предмет не найден.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationException.ErrorResponse handleBookingNotFoundException(final BookingNotFoundException e) {
        return new ValidationException.ErrorResponse(e.getMessage(), "Бронирование не найдено.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ValidationException.ErrorResponse handleThrowable(final RuntimeException e) {
        return new ValidationException.ErrorResponse(e.getMessage(),
                "Сервер прилёг отдохнуть. Но обещал вернуться;)");
    }
}
