package ru.practicum.shareit.booking.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;


@Component
@Slf4j
@AllArgsConstructor
public class BookingStorage {
    JpaBooking jpaBooking;
    UserStorage userStorage;
    ItemStorage itemStorage;
    public Booking addBooking(Booking booking) {
        jpaBooking.save(booking);
        int primaryKey = booking.getId();
        log.info("Объект создан");
        User user = userStorage.getUser(booking.getBooker().getId());
        Item item = itemStorage.getItem(booking.getItem().getId());
        Booking booking1 = jpaBooking.getBookingById(primaryKey);
        booking1.setBooker(user);
        booking1.setItem(item);
        return booking1;
    }
}
