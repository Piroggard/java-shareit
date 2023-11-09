package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.JpaUserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import ru.practicum.shareit.user.validation.UserValidation;
import java.util.List;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private JpaUserRepository jpaUserRepository;
    @Mock
    private UserValidation userValidation;
    private User expectedUser;
    private UserDto userDto;


    @BeforeEach
    void setUser() {
        //userService = new UserServiceImpl(userValidation , userRepository);

        expectedUser = User.builder()
                .id(1)
                .name("Max")
                .email("max@max.ru")
                .build();

        userDto = UserDto.builder()
                .id(1)
                .name("Max")
                .email("max@max.ru")
                .build();
    }

    @Test
    void addUser() {
        String email = "max@max.ru";
        when(jpaUserRepository.save(any(User.class))).thenReturn(expectedUser);
        when(jpaUserRepository.findUserByEmail(email)).thenReturn(null);
        User user = userService.addUser(userDto);
        assertEquals(user, expectedUser);
        verify(jpaUserRepository).save(any(User.class));
    }

    @Test
    void updateUser() {
        User userDtoUpdate = User.builder()
                .id(1)
                .name("MaFed")
                .email("max@max.ru")
                .build();


        when(jpaUserRepository.save(any(User.class))).thenReturn(userDtoUpdate);
        when(jpaUserRepository.findUserByEmail(anyString())).thenReturn(null);
        User user = userService.addUser(userDto);
        assertEquals(user, userDtoUpdate);
        verify(jpaUserRepository).save(any(User.class));
    }

    @Test
    void getUsers() {
        List<User> expectedUsers = List.of(expectedUser);
        when(jpaUserRepository.findAll()).thenReturn(expectedUsers);

        List<User> users = userService.getUsers();

        assertEquals(users.size(), expectedUsers.size());
    }

    @Test
    void getUser(){
        when(jpaUserRepository.findUserById(anyInt())).thenReturn(expectedUser);

        User user = userService.getUser(1);
        assertEquals(user.getId(), expectedUser.getId());
    }
}
