package ru.practicum.shareit.booking.servise;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.storage.JpaBooking;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validation.UserValidation;

import javax.validation.ValidationException;
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
    private final JpaBooking jpaBooking;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final UserValidation userValidation;

    @Override
    public Booking addBooking(BookingDto bookingDto, Integer booker) {
        Item item = Item.builder().id(bookingDto.getItemId()).build();
        User user = User.builder().id(booker).build();
        User userBooker = userStorage.getUser(booker);
        Item itemOwner = itemStorage.getItem(bookingDto.getItemId());
        Item itemValid = itemStorage.getItem(bookingDto.getItemId());
        userValidation.checkingDataNull(userBooker);
        bookingValidation.checkItemAvailable(bookingDto, itemValid);
        bookingValidation.checOwner(booker, itemOwner);
        bookingValidation.bookerValidation(user);


        Booking booking = Booking.builder()
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .booker(user)
                .status(Status.WAITING)
                .build();
        return bookingStorage.addBooking(booking);
    }

    @Override
    public Booking updateBooking(Integer bookingId, Integer userId, Boolean approved) {
        Booking booking = bookingStorage.getBookingById(bookingId);
        bookingValidation.checkUpdateBooking(userId, approved, booking);
        bookingValidation.checIdkBookerUpdate(approved, booking);
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingStorage.addBooking(booking);
    }

    @Override
    public Booking getBooking(Integer bookingId, Integer id) {
        Booking booking = bookingStorage.getBookingById(bookingId);
        bookingValidation.checkBooking(booking);

        bookingValidation.checkBookerOrOwer(bookingStorage.getAllBookingUsers(id, Pageable.unpaged()),
                bookingStorage.getBookingByOwner(id,  Pageable.unpaged()));
        return bookingStorage.getBookingById(bookingId);
    }

    @Override
    public List<Booking> getAllBookingUsers(Integer userId, Integer from, Integer size) {
        if (from < 0) {
            throw new ValidationException("Отрицательное значение фром");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);


        User user = userStorage.getUser(userId);
        bookingValidation.checkBookerOrOwerUser(user);
        return bookingStorage.getAllBookingUsers(userId, page);
    }


    @Override
    public List<Booking> getBookingByState(String state, Integer id, Integer from, Integer size) {

        if (from < 0) {
            throw new ValidationException("Отрицательное значение фром");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);



        if (state.equals("ALL")) {
            return bookingStorage.getAllBookingUsers(id , page);
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
            List<Booking> bookingList = bookingStorage.getAllBookingUsers(id, page);
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
            List<Booking> bookingList = bookingStorage.getAllBookingUsers(id, page);
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
    public List<Booking> getBookingByOwner(String state, Integer idOwner, Integer from, Integer size) {
        bookingValidation.checkBookerOrOwerUser(userStorage.getUser(idOwner));

        if (from < 0) {
            throw new ValidationException("Отрицательное значение фром");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);


        if (state == null) {
            return bookingStorage.getBookingByOwner(idOwner, page);
        }
        if (state.equals("ALL")) {
            return bookingStorage.getBookingByOwner(idOwner, page);
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
            List<Booking> bookingList = bookingStorage.getBookingByOwner(idOwner, page);
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

}
