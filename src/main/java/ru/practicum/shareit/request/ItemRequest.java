package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    int id;
    String description;
    int requestor;
    LocalDate created;
}
