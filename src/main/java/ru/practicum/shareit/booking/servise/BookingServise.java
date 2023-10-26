package ru.practicum.shareit.booking.servise;

import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.StatusException;

import java.util.List;

public interface BookingServise {
    Booking addBooking (BookingDto bookingDto , Integer booker) throws ItemAvailableException;
    Booking updateBooking (Integer bookingId , Integer userId , Boolean approved) throws ItemAvailableException;

    Booking getBooking (Integer bookingId , Integer id );
    List <Booking> getAllBookingUSers (Integer userId);

    List <Booking> getAll ();

    List <Booking> getBookingByState (String state , Integer id) throws StatusException;


    List<Booking> getBookingByOwner (String state , Integer idOwner) throws StatusException;
}
