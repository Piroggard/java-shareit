package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class BookingDto {
    private Integer itemId;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
}
