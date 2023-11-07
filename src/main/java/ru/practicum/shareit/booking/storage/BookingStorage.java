package ru.practicum.shareit.booking.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;


@Component
@Slf4j
@AllArgsConstructor
public class BookingStorage {
    private final JpaBooking jpaBooking;

    public Booking addBooking(Booking booking) {
        jpaBooking.save(booking);
        return jpaBooking.findAllBookingsWithItemAndUserById(booking.getId());
    }

    public Booking getBookingById(Integer id) {
        return jpaBooking.getBookingByIdOrderByStart(id);
    }

    public List<Booking> getAllBookingUsers(Integer id) {
        return jpaBooking.findAllByBooker_IdOrderByStartDesc(id);
    }


    public List<Booking> getBookingByOwner(Integer idOwner) {

        return jpaBooking.findBookingByItem_OwnerOrderByStartDesc(idOwner);
    }

    public List<Booking> findBookingsWithFutureStartTime(Integer id) {
        return jpaBooking.findBookingsWithFutureStartTime(id);
    }

    public List<Booking> findAllByStatus(Status status) {
        return jpaBooking.findAllByStatus(status);
    }

    public List<Booking> findAllByItem_OwnerOrStatus(Integer id, Status status) {
        return jpaBooking.findAllByBooker_IdAndStatus(id, status);
    }

    public List<Booking> findAllByItem_OwnerOrStatusWaiting(Integer id, Status status) {
        return jpaBooking.findAllByItem_OwnerAndStatus(id, status);
    }


}
