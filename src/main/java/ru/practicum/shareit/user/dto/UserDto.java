package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Validated
public final class UserDto {
    private int id;
    private String name;
    @Email(message = "Неправильно указанна почта")
    private String email;
}
