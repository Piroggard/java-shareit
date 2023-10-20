package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate start;
    private LocalDate end;
}
