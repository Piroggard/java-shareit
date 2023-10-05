package ru.practicum.shareit.user.storage;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.*;

@Component

public class UserStorage {
    private Map <Integer,User> saveUser = new HashMap<>();

    public User addUser (User user){
        saveUser.put(user.getId(), user);
        return saveUser.get(user.getId());
    }



    public List<User> getUser(){
        List<User> userList = new ArrayList<>();
        Collection<User> users = saveUser.values();
        for (User user : users) {
            userList.add(user);
        }
        return userList;
    }

    public User getUser(Integer i ) {
        return saveUser.get(i);
    }

    public void removeUser (Integer id){
        saveUser.remove(id);
    }
}
