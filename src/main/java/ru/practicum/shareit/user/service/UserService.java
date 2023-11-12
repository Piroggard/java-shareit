package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User addUser(User user);

    void removeUser(Long userId);

    User updateUser(User user);

    User getUserById(Long id);
}
