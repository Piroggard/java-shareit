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

    public ItemDtoResponse getItemAndBooking(Integer id, Integer ownerId ) {
        LocalDateTime localDateTime = LocalDateTime.now();

        Item item = jpaItemRepository.findItemById(id);

        Booking nextBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsAfterOrStartEqualsOrderByStart(id
                , Status.APPROVED , localDateTime, localDateTime);
        Booking lastBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsBeforeOrStartEqualsOrderByEndDesc(id
                , Status.APPROVED , localDateTime, localDateTime);

        BookingConcise bookingConciseLast;
        BookingConcise bookingConciseNext;

        if (item.getOwner() == ownerId){
            try {
                bookingConciseLast  = BookingConcise.builder().id(nextBooking.getId())
                        .bookerId(nextBooking.getBooker().getId()).build();
                bookingConciseNext = BookingConcise.builder().id(lastBooking.getId())
                        .bookerId(lastBooking.getBooker().getId()).build();
            } catch (NullPointerException nullPointerException){
                bookingConciseLast = null;
                bookingConciseNext = null;
            }


        } else {
            bookingConciseLast = null;
            bookingConciseNext = null;
        }
            List<CommentDto> commentDtoList = new ArrayList<>();
        List<Comment> comments = jpaCommentRepository.findAllByItem_Id(item.getId());
        for (Comment comment : comments) {
            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .text(comment.getText())
                    .itemId(comment.getItem().getId())
                    .authorName(comment.getAuthor().getName())
                    .created(comment.getCreated()).build();
            commentDtoList.add(commentDto);
        }

        ItemDtoResponse itemDtoResponse = ItemDtoResponse.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .nextBooking(bookingConciseLast)
                .comments(commentDtoList)
                .lastBooking(bookingConciseNext).build();
        return itemDtoResponse;

    }
    public List<ItemDtoResponse> getItemUser(Integer userId) {
        List<ItemDtoResponse> itemDtoResponses = new ArrayList<>();
        List<Item> itemList = jpaItemRepository.findAllByOwner(userId);

        for (Item item : itemList) {
            LocalDateTime localDateTime = LocalDateTime.now();
            Booking nextBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsAfterOrStartEqualsOrderByStart(item.getId()
                    , Status.APPROVED , localDateTime, localDateTime);
            Booking lastBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsBeforeOrStartEqualsOrderByEndDesc(item.getId()
                    , Status.APPROVED , localDateTime, localDateTime);
            BookingConcise bookingConciseLast;
            BookingConcise bookingConciseNext;

            if (lastBooking != null || nextBooking != null){


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

            itemDtoResponses.add(itemDtoResponse);
            nextBooking = null;
            lastBooking = null;
            bookingConciseNext = null;
            bookingConciseLast = null;


        }
        return itemDtoResponses;
    }

}
