package ru.practicum.shareit.user.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class userDto {
    int id;
    String name;
    String email;
}
