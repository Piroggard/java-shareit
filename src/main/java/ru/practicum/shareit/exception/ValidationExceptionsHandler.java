package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.servise.BookingServise;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {UserController.class, UserValidation.class, UserServiceImpl.class,
        ItemController.class, ItemService.class, ItemValidation.class, BookingValidation.class, BookingController.class,
BookingServise.class})
public class ValidationExceptionsHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> validationData(final ValidationException e) {
        return Map.of("Error", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> dataNotFound(final ValidationData e) {
        return Map.of("Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> dataNotFound(final ValidationIdException e) {
        return Map.of("Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> dataNotFound(final MethodArgumentNotValidException e) {
        return Map.of("Error", "Неправильно указанна почта");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> checkItemAvailable(final ItemAvailableException e) {
        return Map.of("Error", e.getMessage());
    }
    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> checkItemAvailable(final NullPointerException e) {
        return Map.of("Error", e.getMessage());
    }*/



}
