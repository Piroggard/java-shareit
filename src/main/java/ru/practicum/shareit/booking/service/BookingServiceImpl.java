package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.InCorrectDateException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.ItemUnavailableException;
import ru.practicum.shareit.exceptions.StatusException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.mappers.BookingMapper.toBooking;
import static ru.practicum.shareit.mappers.BookingMapper.toBookingDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;


    @Override
    @Transactional
    public BookingDto addBooking(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new ItemNotFoundException("Предмет не найден."));

        Booking booking = toBooking(user, item, bookingDto);

        if (!booking.getEnd().isAfter(booking.getStart())) {
            throw new InCorrectDateException("Дата бронирования указана неверно.");
        }

        if ((booking.getItem().getOwnerId()).equals(booking.getBooker().getId())) {
            throw new ItemNotFoundException("Зарезервировать собственный предмет");
        }

        if (!booking.getItem().getAvailable()) {
            throw new ItemUnavailableException("В данный момент невозможно арендовать данный предмет");
        }
        booking.setItem(item);
        if (booking.getStatus() == null) {
            booking.setStatus(Status.WAITING);
        }

        return toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public Booking updateBooking(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронирование не найдено"));

        if (!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        if (booking.getStatus() == Status.APPROVED) {
            log.debug("Бронирование уже подтверждено");
            throw new StatusException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new BookingNotFoundException("Только владелец предмета может подтвердить или отклонить бронирование."
                    + userId);
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking getBookingByOwner(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронирование не найдено"));

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        if (!booking.getItem().getOwnerId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            log.debug("Просмотр бронирования доступен лишь владельцу предмета или арендатору.");
            throw new BookingNotFoundException("Просмотр бронирования доступен лишь владельцу предмета или арендатору.");
        }

        if (!booking.getItem().getAvailable()) {
            throw new ItemUnavailableException("Предмет недоступен для бронирования");
        }

        return booking;
    }

    @Override
    public List<Booking> getAllBookingUsers(Long userId, String state, boolean isOwner, int from, int size) {
        if (!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден ", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        LocalDateTime timeNow = LocalDateTime.now();
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        switch (state) {
            case "ALL":
                if (isOwner) {
                    return bookingRepository.getBookingListByOwnerId(userId, pageable);
                } else {
                    return bookingRepository.getBookingListByBookerId(userId, pageable);
                }
            case "REJECTED":
                if (isOwner) {
                    return bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable);
                } else {
                    return bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable);
                }
            case "WAITING":
                if (isOwner) {
                    return bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable);
                } else {
                    return bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable);
                }
            case "CURRENT":
                if (isOwner) {
                    return bookingRepository.getAllCurrentBookingsByOwner(userId, timeNow, timeNow, pageable);
                } else {
                    return bookingRepository.getAllCurrentBookingsByBooker(userId, timeNow, timeNow, pageable);
                }
            case "PAST":
                if (isOwner) {
                    return bookingRepository.getAllPastBookingsByOwner(userId, timeNow, pageable);
                } else {
                    return bookingRepository.getAllPastBookingsByBooker(userId, timeNow, pageable);
                }
            case "FUTURE":
                if (isOwner) {
                    return bookingRepository.getAllFutureBookingsByOwner(userId, timeNow, pageable);
                } else {
                    return bookingRepository.getAllFutureBookingsByBooker(userId, timeNow, pageable);
                }
            default:
                throw new StatusException("Unknown state: "
                        + state);
        }
    }
}
