package ru.practicum.shareit.booking.servise;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ItemAvailableException;

public interface BookingServise {
    Booking addBooking (BookingDto bookingDto , Integer booker) throws ItemAvailableException;
}
