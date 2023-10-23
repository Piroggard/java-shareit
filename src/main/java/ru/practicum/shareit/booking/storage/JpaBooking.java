package ru.practicum.shareit.booking.storage;

import org.apache.catalina.util.Introspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface JpaBooking extends JpaRepository<Booking , Integer> {
    /*@Query("SELECT b, i, u\n" +
            "FROM Booking b\n" +
            "JOIN FETCH b.item i\n" +
            "JOIN FETCH b.booker u\n" +
            "WHERE b.id = :id")
    Booking getBooking (@Param("id") Integer id);*/
    Booking getBookingByIdOrderByStart(Integer id);
    Booking findAllById (Integer id);
    List<Booking> findAllByBooker_Id (Integer id);
    //List<Booking> findAllByItem_Owner ();

    List<Booking> findAllByItem_Owner (Integer id);
    @Query("SELECT b FROM Booking b WHERE b.start> CURRENT_TIMESTAMP")
    List<Booking> findBookingsWithFutureStartTime();

    List<Booking> findAllByStatus (Status status);

}
