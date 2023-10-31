package ru.practicum.shareit.user.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationData;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.ValidationIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@AllArgsConstructor
@Slf4j
@Component
public class UserValidation {
    UserStorage userStorage;

    public void validationUser(UserDto userDto, User user) throws ValidationException {
        log.info("user {}", userDto);
        validationUserData(userDto);
        validationEmaiAdd(user);
    }

    public void validationEmailUpdate( Integer id, User user ) {

        if (user != null) {
            if (id == user.getId()) {
                return;
            }
            if (id != null) {
                throw new ValidationException("Такая почта уже существует");
            }
        }
    }


    public void validationEmaiAdd( User userValid) {
        if (userValid != null) {
            User user = new User();
            userStorage.addUser(user);
            System.out.println(1);
            userStorage.removeUser(user.getId());
            throw new ValidationException("Такая почта уже существует");
        }
    }


    public void validationUserData(UserDto user) {
        if (user.getEmail() == null || user.getName() == null) {
            throw new ValidationData("Получены не все данные ");
        }
    }

    public void checkingDataNull(User user) {
        if (user == null) {
            throw new ValidationIdException("Пользователь не найден ");
        }
    }

}
