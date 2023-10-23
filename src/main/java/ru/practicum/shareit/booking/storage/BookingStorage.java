package ru.practicum.shareit.booking.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;


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
        Booking booking1 = jpaBooking.getBookingByIdOrderByStart(primaryKey);
        booking1.setBooker(user);
        booking1.setItem(item);
        return booking1;
    }

    public Booking getBookingById (Integer id){
        return jpaBooking.getBookingByIdOrderByStart(id);
    }
    public List<Booking> getAllBookingUSers (Integer id){
        return jpaBooking.findAllByBooker_Id(id);
    }

    public List<Booking> getAllBooking (){
        return jpaBooking.findAll();
    }

    public List<Booking> getBookingByOwner (Integer idOwner){
        return jpaBooking.findAllByItem_Owner(idOwner);
    }

    public List<Booking> findBookingsWithFutureStartTime (){
        return jpaBooking.findBookingsWithFutureStartTime();
    }

    public List<Booking> findAllByStatus (Status status){
        return jpaBooking.findAllByStatus(status);
    }
}
