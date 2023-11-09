package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.JpaUserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private JpaUserRepository userRepository;
    private User expectedUser;
    @BeforeEach
    void setUser() {
        expectedUser = User.builder()
                .id(1)
                .name("Max")
                .email("max.ru")
                .build();
    }
}
