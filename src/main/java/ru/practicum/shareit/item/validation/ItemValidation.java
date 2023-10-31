package ru.practicum.shareit.item.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidationData;
import ru.practicum.shareit.exception.ValidationIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Component
public class ItemValidation {

    public void checkUserId(User user) {
        boolean checkUser = false;

        if (user != null) {
            checkUser = true;

        }
        if (!checkUser) {
            throw new ValidationIdException("Пользователь с id не найден");
        }
    }

    public void checItemId(Item item) {
        boolean checkUser = false;

        if (item != null) {
            checkUser = true;

        }
        if (!checkUser) {
            throw new ValidationIdException("Пользователь с id не найден");
        }
    }


    public void checkingDataNull(ItemDtoResponse item) {

        if (item == null) {
            throw new ValidationIdException("Вещь не найдена ");
        }
    }

    public void checkItem(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationData("Получены не все данные ");
        }

        if (itemDto.getName().length() == 0) {
            throw new ValidationData("Имя не может быть пустым");
        }

        if (itemDto.getDescription() == null) {
            throw new ValidationData("Нет описание вещи ");
        }
    }

    public void checkItemUpdate(Integer idUser, Item item) {

        if (item.getOwner() != idUser) {
            throw new ValidationIdException("Невозможно обновить данные");
        }
    }

    public void checkComment(String text, LocalDateTime start) {
        if (text.isEmpty()) {
            throw new ValidationData("Нет комментария ");
        }
        LocalDateTime localDateTime = LocalDateTime.now();

        if (start.isBefore(localDateTime)) {
            throw new ValidationData("Неверный статус ");
        }
    }

    public void checkCommentBoocking(Integer userId, List<Booking> bookings) {

        if (bookings.size() == 0) {
            throw new ValidationData("Пользователь" + userId + " не брал вишь в аренду ");
        }
        for (Booking booking : bookings) {
            if (booking.getStatus() == Status.REJECTED) {
                throw new ValidationData("Бронирование отклонено ");
            }
        }


    }
}
