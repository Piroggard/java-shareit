package ru.practicum.shareit.item.dto;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private int owner;
}
