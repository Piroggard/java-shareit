package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(toBuilder = true)
public class User {
    int id;
    String name;
    String email;
}
