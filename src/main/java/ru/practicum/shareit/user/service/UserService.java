package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User addUser(UserDto userDto);

    User updateUser(Integer id, UserDto userDto);

    User getUser(Integer id);

    List<User> getUsers();

    void removeUser(Integer id);
}