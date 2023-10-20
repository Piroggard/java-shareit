package ru.practicum.shareit.booking.storage;

import org.apache.catalina.util.Introspection;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

public interface JpaBooking extends JpaRepository<Booking , Integer> {
    Booking getBookingById (Integer id);
}
