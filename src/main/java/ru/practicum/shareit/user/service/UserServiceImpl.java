package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
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
    int id = 1;

    @Override
    public User addUser(UserDto userDto)  {
        userValidation.validationUser(userDto);
        User user = User.builder().id(id++).name(userDto.getName()).email(userDto.getEmail()).build();

            return userStorage.addUser(user);
    }

    @Override
    public User updateUser(Integer id, UserDto userDto) {
        userValidation.validationUpdateEmailToList(userDto.getEmail(), id);
        log.info("input Data {}" , userDto);
        User userUpdate = userStorage.getUser(id);
        if (userDto.getName() != null){
            userUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null){
            userUpdate.setEmail(userDto.getEmail());
        }
        log.info("updateUser {} ", userUpdate);

        return userStorage.addUser(userUpdate);
    }

    @Override
    public User getUser(Integer id) {
        return userStorage.getUser(id);
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
