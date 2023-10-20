package ru.practicum.shareit.booking.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;


@Component
@Slf4j
@AllArgsConstructor
public class BookingStorage {
    JpaBooking jpaBooking;
    public Booking addBooking(Booking booking) {
        jpaBooking.save(booking);
        int primaryKey = booking.getId();
        log.info("Объект создан");
        return jpaBooking.getBookingById(primaryKey);
    }
}
