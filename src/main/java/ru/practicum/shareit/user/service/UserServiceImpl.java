package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserValidation userValidation;


    @Override
    public User addUser(UserDto userDto) {
        userValidation.validationUser(userDto , userDto.getId());
        User user = User.builder().name(userDto.getName()).email(userDto.getEmail()).build();
        log.info("Входный данне DTO {}", user);
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(Integer id, UserDto userDto) {

        userValidation.validationEmailUpdate(userDto , id);
        //userValidation.validationEmailToList(userDto , id);
        //userValidation.validationUpdateEmailToList(userDto.getEmail(), id);
        if (userDto.getEmail() == null)
        log.info("input Data {}", userDto);
        User userUpdate = userStorage.getUser(id);
        if (userDto.getName() != null) {
            userUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userUpdate.setEmail(userDto.getEmail());
        }
        log.info("updateUser {} ", userUpdate);
        //User user = User.builder().id(id).name(userDto.getName()).email(userDto.getEmail()).build();

        return userStorage.updateUser(userUpdate);

    }

    @Override
    public User getUser(Integer id) {
        User user = userStorage.getUser(id);
        userValidation.checkingDataNull(user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUser();

    }

    @Override
    public void removeUser(Integer id) {
        userStorage.removeUser(id);
    }
}
