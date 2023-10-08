package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    int id;
    LocalDate start;
    LocalDate end;
    LocalDate item;
    int booker;
    Status status;

}
