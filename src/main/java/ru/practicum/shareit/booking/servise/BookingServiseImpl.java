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
}
