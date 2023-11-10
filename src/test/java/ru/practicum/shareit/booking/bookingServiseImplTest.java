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
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.JpaItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.JpaUserRepository;
import ru.practicum.shareit.user.validation.UserValidation;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class bookingServiseImplTest {
    @InjectMocks
    private BookingServiseImpl bookingServise;
    @Mock
    private JpaBooking jpaBooking;
    @Mock
    private JpaUserRepository jpaUserRepository;
    @Mock
    private JpaItemRepository jpaItemRepository;
    @Mock
    private UserValidation userValidation;
    @Mock
    private BookingValidation bookingValidation;
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
       when(jpaUserRepository.findUserById(1)).thenReturn(user);
       when(jpaItemRepository.findItemById(1)).thenReturn(item);

        doNothing().when(userValidation).checkingDataNull(user);
        doNothing().when(bookingValidation).checkItemAvailable(bookingDto, item);
        doNothing().when(bookingValidation).checOwner(1, item);
        doNothing().when(bookingValidation).bookerValidation(user);

        doNothing().when(bookingValidation).checkBooking(booking);



       Booking booking1 = bookingServise.addBooking(bookingDto , user.getId());
        assertEquals(booking1 , booking);
    }




}
