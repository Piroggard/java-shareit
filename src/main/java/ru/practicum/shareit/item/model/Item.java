package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;


/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(toBuilder = true)
public class Item {
    private  int id;
    private String name;
    private String description;
    private boolean available;
    private int owner;
    private String request;




}
