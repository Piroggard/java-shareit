package ru.practicum.shareit.item.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingConcise;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.JpaBooking;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.*;

@Component
@AllArgsConstructor
public class ItemStorage {
    JpaItemRepository jpaItemRepository;
    JpaBooking jpaBooking;
    JpaCommentRepository jpaCommentRepository;




    /*public Item getItem(Integer id) {
        return jpaItemRepository.findItemById(id);
    }*/





    /*public List<Item> findAllByNameOrDescription(String string) {
        return jpaItemRepository.search(string);
    }*/

}
