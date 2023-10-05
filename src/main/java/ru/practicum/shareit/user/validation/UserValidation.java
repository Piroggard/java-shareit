package ru.practicum.shareit.user.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationData;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@AllArgsConstructor
@Slf4j
@Component
public class UserValidation {
    UserStorage userStorage;
    public void validationUser (UserDto user) throws ValidationException {
        log.info("user {}", user);
        validationUserData(user);
        validationEmail(user);
        validationEmailToList(user);
    }

    public void validationEmailToList(UserDto user){
        for (User user1 : userStorage.getUser()) {
            log.info("userStorage {}", userStorage.getUser());
            if (user1.getEmail().equals(user.getEmail())){
                throw new ValidationException("Такая почта уже существует");
            }
        }
    }

    public void validationUpdateEmailToList(String mail, Integer id){
        for (User user1 : userStorage.getUser()) {
            log.info("userStorage {}", userStorage.getUser());
            if (user1.getEmail().equals(mail)){
                if (id != user1.getId()){
                    throw new ValidationException("Такая почта уже существует");
                }

            }
        }
    }

    public void validationUserData (UserDto user){
        if(user.getEmail() == null || user.getName() == null){
            throw new ValidationData("Получены не все данные ");
        }
    }

    public void validationEmail (UserDto user) {
        char[] mail = user.getEmail().toCharArray();
        boolean validMail = false;
        for (char c : mail) {
            if (c == '@'){
                validMail = true;
            }
        }
        if (!validMail){
            throw new ValidationData("Неправильно указанна почта");
        }
    }

}
