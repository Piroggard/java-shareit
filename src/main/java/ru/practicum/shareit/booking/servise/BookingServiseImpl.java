package ru.practicum.shareit.booking.servise;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.JpaBooking;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.JpaItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.JpaUserRepository;
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
    private final BookingValidation bookingValidation;
    private final JpaBooking jpaBooking;
    JpaItemRepository jpaItemRepository;
    private final UserValidation userValidation;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public Booking addBooking(BookingDto bookingDto, Integer booker) {
        Item item = Item.builder().id(bookingDto.getItemId()).build();
        User user = User.builder().id(booker).build();
        User userBooker = jpaUserRepository.findUserById(booker);
        Item itemOwner = jpaItemRepository.findItemById(bookingDto.getItemId());
        Item itemValid = jpaItemRepository.findItemById(bookingDto.getItemId());
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
        jpaBooking.save(booking);
        return jpaBooking.findAllBookingsWithItemAndUserById(booking.getId());
    }

    @Override
    public Booking updateBooking(Integer bookingId, Integer userId, Boolean approved) {
        Booking booking = jpaBooking.getBookingByIdOrderByStart(bookingId);
        bookingValidation.checkUpdateBooking(userId, approved, booking);
        bookingValidation.checIdkBookerUpdate(approved, booking);
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        jpaBooking.save(booking);
        return jpaBooking.findAllBookingsWithItemAndUserById(booking.getId());
    }

    @Override
    public Booking getBooking(Integer bookingId, Integer id) {
        Booking booking = jpaBooking.getBookingByIdOrderByStart(bookingId);
        bookingValidation.checkBooking(booking);

        bookingValidation.checkBookerOrOwer(jpaBooking.findAllByBooker_IdOrderByStartDesc(id, Pageable.unpaged()),
                jpaBooking.findBookingByItem_OwnerOrderByStartDesc(id,  Pageable.unpaged()));
        return jpaBooking.getBookingByIdOrderByStart(bookingId);
    }

    @Override
    public List<Booking> getAllBookingUsers(Integer userId, Integer from, Integer size) {
        if (from < 0) {
            throw new ValidationException("Отрицательное значение фром");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);


        User user = jpaUserRepository.findUserById(userId);
        bookingValidation.checkBookerOrOwerUser(user);
        return jpaBooking.findAllByBooker_IdOrderByStartDesc(userId, page);
    }


    @Override
    public List<Booking> getBookingByState(String state, Integer id, Integer from, Integer size) {

        if (from < 0) {
            throw new ValidationException("Отрицательное значение фром");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);



        if (state.equals("ALL")) {
            return jpaBooking.findAllByBooker_IdOrderByStartDesc(id , page);
        }
        if (state.equals("FUTURE")) {
            return jpaBooking.findBookingsWithFutureStartTime(id);
        }
        if (state.equals("UNSUPPORTED_STATUS")) {
            throw new StatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (state.equals("WAITING")) {
            return jpaBooking.findAllByStatus(Status.WAITING);
        }

        if (state.equals("REJECTED")) {
            return jpaBooking.findAllByBooker_IdAndStatus(id, Status.REJECTED);
        }

        if (state.equals("CURRENT")) {
            List<Booking> bookingList = jpaBooking.findAllByBooker_IdOrderByStartDesc(id, page);
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
            List<Booking> bookingList = jpaBooking.findAllByBooker_IdOrderByStartDesc(id, page);
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
        bookingValidation.checkBookerOrOwerUser(jpaUserRepository.findUserById(idOwner));

        if (from < 0) {
            throw new ValidationException("Отрицательное значение фром");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);


        if (state == null) {
            return jpaBooking.findBookingByItem_OwnerOrderByStartDesc(idOwner, page);
        }
        if (state.equals("ALL")) {
            return jpaBooking.findBookingByItem_OwnerOrderByStartDesc(idOwner, page);
        }
        if (state.equals("FUTURE")) {
            return jpaBooking.findBooking(idOwner);
        }
        if (state.equals("UNSUPPORTED_STATUS")) {
            throw new StatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (state.equals("WAITING")) {
            return jpaBooking.findAllByStatus(Status.WAITING);
        }

        if (state.equals("REJECTED")) {
            return jpaBooking.findAllByItem_OwnerAndStatus(idOwner, Status.REJECTED);
        }

        if (state.equals("CURRENT")) {
            return jpaBooking.findAllByItem_OwnerAndStatus(idOwner, Status.REJECTED);
        }

        if (state.equals("PAST")) {
            List<Booking> bookingList = jpaBooking.findBookingByItem_OwnerOrderByStartDesc(idOwner, page);
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
