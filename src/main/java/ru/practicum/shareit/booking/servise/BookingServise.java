package ru.practicum.shareit.booking.servise;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ItemAvailableException;

import java.util.List;

public interface BookingServise {
    Booking addBooking (BookingDto bookingDto , Integer booker) throws ItemAvailableException;
    Booking updateBooking (Integer bookingId , Integer userId , Boolean approved);

    Booking getBooking (Integer bookingId );
    List <Booking> getAllBookingUSers (Integer userId);

    List <Booking> getAll ();

    List <Booking> getBookingByStatus (String state);
}
