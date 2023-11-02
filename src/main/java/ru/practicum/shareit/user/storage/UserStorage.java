package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserStorage {
    private final JpaUserRepository jpaUserRepository;

    public UserStorage(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }


    public User addUser(User user) {
        jpaUserRepository.save(user);
        int primaryKey = user.getId();
        return jpaUserRepository.getReferenceById(primaryKey);
    }

    public User getUser(Integer id) {


        return jpaUserRepository.findUserById(id);
    }

    public List<User> getUser() {
        return jpaUserRepository.findAll();
    }

    public void removeUser(Integer id) {
        jpaUserRepository.deleteById(id);
    }

    public User updateUser(User user) {
        jpaUserRepository.save(user);
        return getUser(user.getId());
    }

    public User checkEmail(String email) {
        return jpaUserRepository.findUserByEmail(email);
    }

}

