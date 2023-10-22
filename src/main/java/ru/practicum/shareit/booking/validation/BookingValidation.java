package ru.practicum.shareit.booking.validation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.ValidationData;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.ValidationIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BookingValidation {
    BookingStorage bookingStorage;
    ItemStorage itemStorage;
    UserStorage userStorage;

    public void checkItemAvailable (Integer idItem, BookingDto bookingDto) throws ItemAvailableException {
        Item item = itemStorage.getItem(idItem);
        LocalDateTime localDate = LocalDateTime.now();
        if (item == null){
            throw new ValidationIdException("Вешь не найдена");
        }
        if (bookingDto.getEnd() == null || bookingDto.getStart() == null){
            throw new ValidationData("Не заполнено время ");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) ){
            throw new ValidationData("Время начала бранирование позже конца");
        }

        if (bookingDto.getEnd().isEqual(bookingDto.getStart()) && bookingDto.getStart().isAfter(localDate)){
            throw new ValidationData("Мутная провенрка");
        }
        if (bookingDto.getStart().isBefore(localDate)){
            throw new ValidationData("Вчерашняя дата");
        }



        if (!item.getAvailable()){
            throw new ItemAvailableException("Вешь недоступна для бронирования");
        }
    }

    public void bookerValidation (Integer bookerId){
        User user = userStorage.getUser(bookerId);
        if (user == null){
            throw new ValidationIdException("Нет такого пользователя");
        }
    }

}
