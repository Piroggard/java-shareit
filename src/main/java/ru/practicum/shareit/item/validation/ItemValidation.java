package ru.practicum.shareit.item.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationData;
import ru.practicum.shareit.exception.ValidationId;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@AllArgsConstructor
@Slf4j
@Component
public class ItemValidation {
    UserStorage userStorage;
    ItemStorage itemStorage;

    public void checkUserId(int id) {
        boolean checkUser = false;
        for (User user : userStorage.getUser()) {
            if (user.getId() == id) {
                checkUser = true;
                break;
            }
        }

        if (!checkUser) {
            throw new ValidationId("Пользыватель с id = " + id + " не найден");
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

    public void checkItemUpdate(Integer idUser, Integer idItem) {

        if (itemStorage.getItem(idItem).getOwner() != idUser) {
            throw new ValidationId("Невозможно обновитть данные");
        }
    }

}
