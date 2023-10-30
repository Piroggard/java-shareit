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
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class BookingServiseImpl implements BookingServise {
    private final BookingStorage bookingStorage;
    private final BookingValidation bookingValidation;
    JpaBooking jpaBooking;

    @Override
    public Booking addBooking(BookingDto bookingDto, Integer booker) throws ItemAvailableException {
        Item item = Item.builder().id(bookingDto.getItemId()).build();
        User user = User.builder().id(booker).build();
        bookingValidation.checkItemAvailable(bookingDto.getItemId(), bookingDto);
        bookingValidation.checOwner(booker, bookingDto.getItemId());
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
        bookingValidation.checkUpdateBooking(bookingId, userId, approved);
        bookingValidation.checIdkBookerUpdate(bookingId, approved);
        Booking booking = bookingStorage.getBookingById(bookingId);
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingStorage.addBooking(booking);
    }

    @Override
    public Booking getBooking(Integer bookingId, Integer id) {
        bookingValidation.checkBooking(bookingId);
        bookingValidation.checkBookerOrOwer(bookingId, id);
        return bookingStorage.getBookingById(bookingId);
    }

    @Override
    public List<Booking> getAllBookingUSers(Integer userId) {
        bookingValidation.checkBookerOrOwerUser(userId);
        return bookingStorage.getAllBookingUsers(userId);
    }


    @Override
    public List<Booking> getBookingByState(String state, Integer id) throws StatusException {
        if (state.equals("ALL")) {
            return bookingStorage.getAllBookingUsers(id);
        }
        if (state.equals("FUTURE")) {
            return bookingStorage.findBookingsWithFutureStartTime(id);
        }
        if (state.equals("UNSUPPORTED_STATUS")) {
            throw new StatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (state.equals("WAITING")) {
            return bookingStorage.findAllByStatus(Status.WAITING);
        }

        if (state.equals("REJECTED")) {
            return bookingStorage.findAllByItem_OwnerOrStatus(id, Status.REJECTED);
        }

        if (state.equals("CURRENT")) {
            List<Booking> bookingList = bookingStorage.getAllBookingUsers(id);
            List<Booking> list = new ArrayList<>();
            LocalDateTime localDateTime = LocalDateTime.now();

            for (Booking booking : bookingList) {
                if (booking.getStart().isBefore(localDateTime) && booking.getEnd().isAfter(localDateTime)) {
                    list.add(booking);
                }
            }
            Collections.sort(list);
            return list;
        }

        if (state.equals("PAST")) {
            List<Booking> bookingList = bookingStorage.getAllBookingUsers(id);
            List<Booking> list = new ArrayList<>();
            LocalDateTime localDateTime = LocalDateTime.now();

            for (Booking booking : bookingList) {
                if (booking.getEnd().isBefore(localDateTime)) {
                    list.add(booking);
                }
            }
            return list;
        }
        return null;

    }

    @Override
    public List<Booking> getBookingByOwner(String state, Integer idOwner) throws StatusException {
        bookingValidation.checkBookerOrOwerUser(idOwner);
        if (state == null) {
            return bookingStorage.getBookingByOwner(idOwner);
        }
        if (state.equals("ALL")) {
            return bookingStorage.getBookingByOwner(idOwner);
        }
        if (state.equals("FUTURE")) {
            return jpaBooking.findBooking(idOwner);
        }
        if (state.equals("UNSUPPORTED_STATUS")) {
            throw new StatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (state.equals("WAITING")) {
            return bookingStorage.findAllByStatus(Status.WAITING);
        }

        if (state.equals("REJECTED")) {
            return bookingStorage.findAllByItem_OwnerOrStatusWaiting(idOwner, Status.REJECTED);
        }

        if (state.equals("CURRENT")) {
            return bookingStorage.findAllByItem_OwnerOrStatusWaiting(idOwner, Status.REJECTED);
        }

        if (state.equals("PAST")) {
            List<Booking> bookingList = bookingStorage.getBookingByOwner(idOwner);
            List<Booking> list = new ArrayList<>();
            LocalDateTime localDateTime = LocalDateTime.now();

            for (Booking booking : bookingList) {
                if (booking.getEnd().isBefore(localDateTime)) {
                    System.out.println(1);
                    list.add(booking);
                }
            }
            System.out.println(bookingList);
            return list;
        }
        return null;

    }

}
