package ru.practicum.shareit.user.model;

import lombok.*;



/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class User {
    private int id;
    private String name;
    private String email;
}
