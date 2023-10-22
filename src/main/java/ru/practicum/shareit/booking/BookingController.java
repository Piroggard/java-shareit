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
    public Booking addBooking (@Validated @RequestHeader("X-Sharer-User-Id") Integer booker, @RequestBody BookingDto bookingDto) throws ItemAvailableException {

        log.info("Дата начала {} и дата конца {}", bookingDto.getStart(), bookingDto.getEnd());
            return bookingServise.addBooking(bookingDto , booker);
    }


    @PatchMapping("{bookingId}")
    public Booking updateBooking (@RequestHeader("X-Sharer-User-Id") Integer id,
                                  @PathVariable Integer bookingId,
                                  @RequestParam (name = "approved") Boolean approved){
        System.out.println(1);
        log.info("Заголовок {}, id бронирования {}, значение {}" , id, bookingId, approved);
        return bookingServise.updateBooking(bookingId, id, approved);
    }

    @GetMapping("{bookingId}")
    public Booking getBooking (@PathVariable Integer bookingId){
        return bookingServise.getBooking(bookingId);
    }

    @GetMapping
    public List<Booking> getAllBookingUser (@RequestHeader("X-Sharer-User-Id") Integer id ){
        return bookingServise.getAllBookingUSers(id);
    }

 /*   @GetMapping("state")
    public List<Booking> getBookingByStatus (@RequestHeader("X-Sharer-User-Id") Integer id,
                                             @RequestParam (name = "state") String state){
        return bookingServise.getBookingByStatus(state);
    }
*/


}
