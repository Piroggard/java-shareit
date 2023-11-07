package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User addUser(@RequestBody @Valid UserDto userDto) {
        return userService.addUser(userDto);
    }


    @PatchMapping("{id}")
    public User updateUser(@PathVariable @Valid Integer id, @RequestBody UserDto userDto) {
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
