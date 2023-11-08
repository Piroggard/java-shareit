package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.servise.BookingServise;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

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
    public Booking addBooking(@Validated @RequestHeader("X-Sharer-User-Id") Integer booker,
                              @RequestBody BookingDto bookingDto) {

        log.info("Дата начала {} и дата конца {}", bookingDto.getStart(), bookingDto.getEnd());
        return bookingServise.addBooking(bookingDto, booker);
    }


    @PatchMapping("{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") Integer id,
                                 @PathVariable Integer bookingId,
                                 @RequestParam(name = "approved", required = false) Boolean approved) {
        log.info("Заголовок {}, id бронирования {}, значение {}", id, bookingId, approved);
        return bookingServise.updateBooking(bookingId, id, approved);
    }

    @GetMapping("{bookingId}")
    public Booking getBooking(@RequestHeader("X-Sharer-User-Id") Integer id,
                              @PathVariable Integer bookingId) {
        log.info("Вызов метода получения информации по бронированию. Заголовок {}, вещь {}", id, bookingId);
        return bookingServise.getBooking(bookingId, id);
    }

    @GetMapping
    public List<Booking> getAllBookingUser(@RequestHeader("X-Sharer-User-Id") Integer id,
                                           @RequestParam(name = "state", required = false) String state,
                                           @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        log.info("Вызов метода получения информации. Заголовок {}, Статус {}", id, state);
        if (state == null) {
            return bookingServise.getAllBookingUsers(id, from, size);
        } else {
            return bookingServise.getBookingByState(state, id, from, size);
        }

    }

    @GetMapping("owner")
    public List<Booking> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Integer idOwner,
                                           @RequestParam(name = "state", required = false) String state,
                                           @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        log.info("Вызов метода получения информации об Owner. Заголовок {}, Статус {}", idOwner, state);

        return bookingServise.getBookingByOwner(state, idOwner, from, size);
    }


}
