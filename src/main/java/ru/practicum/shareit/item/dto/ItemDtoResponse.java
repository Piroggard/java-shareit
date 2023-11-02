package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingConcise;

import ru.practicum.shareit.item.model.CommentDto;


import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public final class ItemDtoResponse {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private int owner;
    private String request;
    private BookingConcise lastBooking;
    private BookingConcise nextBooking;
    private List<CommentDto> comments;
}





