package ru.practicum.shareit.booking.servise;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingServise {
    Booking addBooking(BookingDto bookingDto, Integer booker);

    Booking updateBooking(Integer bookingId, Integer userId, Boolean approved);

    Booking getBooking(Integer bookingId, Integer id);

    List<Booking> getAllBookingUsers(Integer userId);

    List<Booking> getBookingByState(String state, Integer id);

    List<Booking> getBookingByOwner(String state, Integer idOwner);
}
