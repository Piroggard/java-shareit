package ru.practicum.shareit.user.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Validated
public class UserDto {
    private int id;
    private String name;
    @Email(message = "Неправильно указанна почта")
    private String email;
}
