package ru.practicum.shareit.item.model;

import lombok.*;


/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private int owner;
    private String request;

}
