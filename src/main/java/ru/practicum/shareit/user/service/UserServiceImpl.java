package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.JpaUserRepository;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidation userValidation;
    private final JpaUserRepository jpaUserRepository;


    @Override
    public User addUser(UserDto userDto) {
        User userWithMail = jpaUserRepository.findUserByEmail(userDto.getEmail());
        userValidation.validationUser(userDto, userWithMail);
        User user = User.builder().name(userDto.getName()).email(userDto.getEmail()).build();
        log.info("Входный данне DTO {}", user);
        return jpaUserRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, UserDto userDto) {
        User userWithMail = jpaUserRepository.findUserByEmail(userDto.getEmail());
        userValidation.validationEmailUpdate(id, userWithMail);
        if (userDto.getEmail() == null)
            log.info("input Data {}", userDto);
        User userUpdate = jpaUserRepository.findUserById(id);
        if (userDto.getName() != null) {
            userUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userUpdate.setEmail(userDto.getEmail());
        }
        log.info("updateUser {} ", userUpdate);
        return jpaUserRepository.save(userUpdate);

    }

    @Override
    public User getUser(Integer id) {
        User user = jpaUserRepository.findUserById(id);
        userValidation.checkingDataNull(user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return jpaUserRepository.findAll();

    }

    @Override
    public void removeUser(Integer id) {
        jpaUserRepository.deleteById(id);
    }


}