package ru.practicum.shareit.item.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingConcise;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.JpaBooking;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.*;

@Component
@AllArgsConstructor
public class ItemStorage {
    JpaItemRepository jpaItemRepository;
    JpaBooking jpaBooking;




    public Item addItem(Item item) {

        return jpaItemRepository.save(item);
    }

    public List<Item> getItems() {

        return jpaItemRepository.findAll();
    }
 /*   public Item getItemList(Integer id) {
        return jpaItemRepository.findAllByOwner(id);
    }*/




    public Item getItem(Integer id  ) {
        return jpaItemRepository.findItemById(id);
    }

    public ItemDtoResponse getItemAndBooking(Integer id, Integer ownerId) {
        LocalDateTime localDateTime = LocalDateTime.now();

        Item item = jpaItemRepository.findItemById(id);

        Booking nextBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsAfterOrStartEqualsOrderByStart(id
                , Status.APPROVED , localDateTime, localDateTime);
        Booking lastBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsBeforeOrStartEqualsOrderByEndDesc(id
                , Status.APPROVED , localDateTime, localDateTime);

        BookingConcise bookingConciseLast;
        BookingConcise bookingConciseNext;

        if (item.getOwner() == ownerId){

            bookingConciseLast  = BookingConcise.builder().id(nextBooking.getId())
                    .bookerId(nextBooking.getBooker().getId()).build();


            bookingConciseNext = BookingConcise.builder().id(lastBooking.getId())
                    .bookerId(lastBooking.getBooker().getId()).build();


        } else {
            bookingConciseLast = null;
            bookingConciseNext = null;
        }
















        ItemDtoResponse itemDtoResponse = ItemDtoResponse.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .nextBooking(bookingConciseLast)
                .lastBooking(bookingConciseNext).build();




        return itemDtoResponse;
    }
}
