package ru.practicum.shareit.booking.servise;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class BookingServiseImpl implements BookingServise{
    private final BookingStorage bookingStorage;
    private final BookingValidation bookingValidation;

    @Override
    public Booking addBooking(BookingDto bookingDto , Integer booker ) throws ItemAvailableException {
        Item item = Item.builder().id(bookingDto.getItemId()).build();
        User user = User.builder().id(booker).build();
        bookingValidation.bookerValidation(booker);
        bookingValidation.checkItemAvailable(bookingDto.getItemId(), bookingDto);
        Booking booking = Booking.builder().
                item(item).
                start(bookingDto.getStart()).
                end(bookingDto.getEnd()).
                booker(user).
                status(Status.WAITING).
                build();
        return bookingStorage.addBooking(booking);
    }

    @Override
    public Booking updateBooking(Integer bookingId, Integer userId, Boolean approved) {
        //LocalDateTime localDateTime = LocalDateTime.now();
        Booking booking = bookingStorage.getBookingById(bookingId);
        booking.setStatus(Status.APPROVED);
        //Booking booking1 = Booking.builder().id(booking.getId()).item(booking.getItem()).booker(booking.getBooker()).status(booking.getStatus()).build();
        /*booking.setStart(localDateTime);
        booking.setEnd(localDateTime);*/
        //booking.setEnd(null);
        return bookingStorage.addBooking(booking);
    }

    @Override
    public Booking getBooking(Integer bookingId) {
        return bookingStorage.getBookingById(bookingId);
    }

    @Override
    public List<Booking> getAllBookingUSers(Integer userId) {
        return bookingStorage.getAllBookingUSers(userId);
    }

    @Override
    public List<Booking> getAll() {
        return bookingStorage.getAllBooking();
    }

    @Override
    public List<Booking> getBookingByStatus(String state) {
        if (state.equals("ALL")){
            return bookingStorage.getAllBooking();
        }
        if (state.equals("FUTURE")){

        }
        if (state.equals("UNSUPPORTED_STATUS")){

        }

        return null;

    }

}
