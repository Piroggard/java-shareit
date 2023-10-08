package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    @PostMapping
    public User addUser(@RequestBody UserDto userDto) throws ValidationException {
        return userService.addUser(userDto);
    }

    @PatchMapping("{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);

    }

    @GetMapping("{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @DeleteMapping("{id}")
    public void removeUser(@PathVariable Integer id) {
        userService.removeUser(id);
    }


    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
