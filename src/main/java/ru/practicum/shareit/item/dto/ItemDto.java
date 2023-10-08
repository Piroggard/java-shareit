package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder(toBuilder = true)
public class ItemDto {


    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private int owner;
}
