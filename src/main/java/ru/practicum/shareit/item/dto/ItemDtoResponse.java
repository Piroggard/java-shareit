package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingConcise;

import javax.persistence.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class ItemDtoResponse {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private int owner;
    private String request;
    private BookingConcise lastBooking;
    private BookingConcise nextBooking;
}






