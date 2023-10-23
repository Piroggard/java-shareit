package ru.practicum.shareit.booking.servise;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.storage.JpaBooking;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.validation.UserValidation;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class BookingServiseImpl implements BookingServise{
    private final BookingStorage bookingStorage;
    private final BookingValidation bookingValidation;
    private final UserValidation userValidation;
    JpaBooking jpaBooking;

    @Override
    public Booking addBooking(BookingDto bookingDto , Integer booker ) throws ItemAvailableException {
        Item item = Item.builder().id(bookingDto.getItemId()).build();
        User user = User.builder().id(booker).build();
        bookingValidation.checkItemAvailable(bookingDto.getItemId(), bookingDto);
        bookingValidation.checOwner(booker , bookingDto.getItemId());
        bookingValidation.bookerValidation(booker);

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
    public Booking updateBooking(Integer bookingId, Integer userId, Boolean approved) throws ItemAvailableException {
        bookingValidation.checkUpdateBooking(bookingId, userId , approved);
        //LocalDateTime localDateTime = LocalDateTime.now();
        bookingValidation.checIdkBookerUpdate(bookingId , approved);
        Booking booking = bookingStorage.getBookingById(bookingId);
        if (approved){
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        //Booking booking1 = Booking.builder().id(booking.getId()).item(booking.getItem()).booker(booking.getBooker()).status(booking.getStatus()).build();
        /*booking.setStart(localDateTime);
        booking.setEnd(localDateTime);*/
        //booking.setEnd(null);
        return bookingStorage.addBooking(booking);
        //return jpaBooking.save(booking);
    }

    @Override
    public Booking getBooking(Integer bookingId, Integer id) {
        bookingValidation.checkBooking(bookingId);
        bookingValidation.checkBookerOrOwer(bookingId , id);
        return bookingStorage.getBookingById(bookingId);
    }

    @Override
    public List<Booking> getAllBookingUSers(Integer userId) {
        bookingValidation.checkBookerOrOwerUser(userId);

        return bookingStorage.getAllBookingUSers(userId);
    }

    @Override
    public List<Booking> getAll() {
        return bookingStorage.getAllBooking();
    }

    @Override
    public List<Booking> getBookingByState(String state) {
        if (state.equals("ALL")){
            return bookingStorage.getAllBooking();
        }
        if (state.equals("FUTURE")){
            return bookingStorage.findBookingsWithFutureStartTime();

        }
        if (state.equals("UNSUPPORTED_STATUS")){

        }
        if (state.equals("WAITING")){
            return bookingStorage.findAllByStatus(Status.WAITING);
        }

        if (state.equals("REJECTED")){
            return bookingStorage.findAllByStatus(Status.REJECTED);
        }
        return null;

    }
    @Override
    public List<Booking> getBookingByOwner(Integer idOwner) {
        bookingValidation.checkBookerOrOwerUser(idOwner);
        return bookingStorage.getBookingByOwner(idOwner);
    }

}
