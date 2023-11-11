package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    Long id;
    @NotBlank(message = "Name не должен быть пустым")
    String name;
    @NotBlank(message = "description не должен быть пустым")
    String description;
    Long ownerId;
    @NotNull(message = "available не должен отсутствовать")
    Boolean available;
    List<CommentDto> comments;
    BookingDtoShort lastBooking;
    BookingDtoShort nextBooking;
    Long requestId;

}
