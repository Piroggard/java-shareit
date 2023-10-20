package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.servise.BookingServise;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {
    BookingServise bookingServise;
    @PostMapping
    public Booking addBooking (@Validated @RequestHeader("X-Sharer-User-Id") Integer booker, @RequestBody BookingDto bookingDto) throws ItemAvailableException {

        log.info("Дата начала {} и дата конца {}", bookingDto.getStart(), bookingDto.getEnd());
            return bookingServise.addBooking(bookingDto , booker);
    }


}
