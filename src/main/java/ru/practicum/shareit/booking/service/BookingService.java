package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(Long userId, BookingDto bookingDtoItem);

    Booking updateBooking(Long userId, Long bookingId, boolean approved);

    Booking getBookingByOwner(Long userId, Long bookingId);

    List<Booking> getAllBookingUsers(Long userId, String state, boolean isOwner, int from, int size);

}
