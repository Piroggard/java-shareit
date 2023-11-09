package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.servise.BookingServiseImpl;
import ru.practicum.shareit.booking.storage.JpaBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class bookingServiseImplTest {
    @InjectMocks
    private BookingServiseImpl bookingServise;
    @Mock
    private JpaBooking jpaBooking;
    private Item item;
    private User user;
    private Booking booking;
    BookingDto bookingDto;

    @BeforeEach
    void setBooking (){
        item = Item.builder().id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(1)
                .request(1).build();

        user = User.builder()
                .id(1)
                .name("Max")
                .email("max@max.ru")
                .build();



        booking =Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusWeeks(2))
                .item(item)
                .booker(user)
                .status(Status.APPROVED).build();

        bookingDto = BookingDto
                .builder()
                .itemId(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusWeeks(2)).build();

    }

    @Test
    void addBooking (){
       when(jpaBooking.save(booking)).thenReturn(booking);
       when(jpaBooking.findAllBookingsWithItemAndUserById(1)).thenReturn(booking);

       Booking booking1 = bookingServise.addBooking(bookingDto , user.getId());
        assertEquals(booking1 , booking);




    }




}
