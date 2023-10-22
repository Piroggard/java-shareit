package ru.practicum.shareit.booking.storage;

import org.apache.catalina.util.Introspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

public interface JpaBooking extends JpaRepository<Booking , Integer> {
    /*@Query("SELECT b, i, u\n" +
            "FROM Booking b\n" +
            "JOIN FETCH b.item i\n" +
            "JOIN FETCH b.booker u\n" +
            "WHERE b.id = :id")
    Booking getBooking (@Param("id") Integer id);*/
    Booking getBookingById(Integer id);
}
