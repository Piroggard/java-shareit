package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingConcise;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.JpaBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.storage.JpaCommentRepository;
import ru.practicum.shareit.item.storage.JpaItemRepository;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.request.storage.JpaItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.JpaUserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service

public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemValidation itemValidation;
    private JpaCommentRepository jpaCommentRepository;

    private JpaBooking jpaBooking;
    private MappingComment mappingComment;

    private final JpaUserRepository jpaUserRepository;
    private JpaItemRepository jpaItemRepository;

    @Override
    public ItemDtoResponse addItem(Integer id, ItemDto itemDto) {
        User user = jpaUserRepository.findUserById(id);
        itemValidation.checkItem(itemDto);
        itemValidation.checkUserId(user);
        //ItemRequest itemRequest = jpaItemRequest.findAllById(itemDto.getRequestId());
        log.info("id {} , itemDto {}", id, itemDto);
        Item item = Item.builder().name(itemDto.getName()).description(itemDto.getDescription())
                .owner(id).available(itemDto.getAvailable()).request(itemDto.getRequestId()).build();
        jpaItemRepository.save(item);
        ItemDtoResponse itemDtoResponse = ItemDtoResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .requestId(item.getRequest()).build();

                /*ResponseItem responseItem = ResponseItem.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .owner(item.getOwner())
                        .available(item.getAvailable())
                        .request(itemRequest.getRequester()).build();*/

                return itemDtoResponse;
    }

    @Override
    public Item updateItem(Integer idUser, Integer itemId, ItemDto itemDto) {

        itemValidation.checkItemUpdate(idUser, jpaItemRepository.findItemById(itemId));
        Item item = jpaItemRepository.findItemById(itemId);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return jpaItemRepository.save(item);
    }

    @Override
    public ItemDtoResponse getItem(Integer itemId, Integer id) {
        itemValidation.checItemId(jpaItemRepository.findItemById(itemId));
        ItemDtoResponse item = getItemAndBooking(itemId, id);
        itemValidation.checkingDataNull(item);
        return item;
    }

    @Override
    public List<ItemDtoResponse> getItemUser(Integer userId) {
        return getItemUsers(userId);

    }

    @Override
    public List<Item> getItemByName(String text) {

        String textToLower = text.toLowerCase();
        log.info(textToLower);
        if (text.length() == 0) {
            List<Item> itemList = new ArrayList<>();
            return itemList;
        }
        return jpaItemRepository.search(textToLower);
    }

    @Override
    public CommentDto addComment(Integer idUser, Integer itemId, CommentDto commentDto) {
        LocalDateTime start = jpaBooking.getBookingByIdOrderByStart(itemId).getStart();
        itemValidation.checkComment(commentDto.getText(), start);
        List<Booking> bookings = jpaBooking.findAllByBooker_IdAndItem_Id(idUser, itemId);
        itemValidation.checkCommentBoocking(idUser, bookings);
        LocalDateTime localDateTime = LocalDateTime.now();
        Item item = jpaItemRepository.findItemById(itemId);
        User user = jpaUserRepository.findUserById(idUser);
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(user)
                .created(localDateTime).build();
        return mappingComment.mappingCommentInCommentDto(jpaCommentRepository.save(comment));
    }

    public ItemDtoResponse getItemAndBooking(Integer id, Integer ownerId) {
        LocalDateTime localDateTime = LocalDateTime.now();

        Item item = jpaItemRepository.findItemById(id);

        Booking nextBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsAfterOrStartEqualsOrderByStart(id,
                Status.APPROVED, localDateTime, localDateTime);
        Booking lastBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsBeforeOrStartEqualsOrderByEndDesc(id,
                Status.APPROVED, localDateTime, localDateTime);

        BookingConcise bookingConciseLast = null;
        BookingConcise bookingConciseNext = null;

        if (item.getOwner() == ownerId) {
            if (nextBooking == null) {
                bookingConciseNext = null;
            } else {
                bookingConciseLast = BookingConcise.builder().id(nextBooking.getId())
                        .bookerId(nextBooking.getBooker().getId()).build();
            }
            if (lastBooking == null) {
                bookingConciseLast = null;
            } else {
                bookingConciseNext = BookingConcise.builder().id(lastBooking.getId())
                        .bookerId(lastBooking.getBooker().getId()).build();
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
                .nextBooking(bookingConciseLast)
                .comments(commentDtoList)
                .lastBooking(bookingConciseNext).build();
        return itemDtoResponse;
    }

    public List<ItemDtoResponse> getItemUsers(Integer userId) {
        List<ItemDtoResponse> itemDtoResponses = new ArrayList<>();
        List<Item> itemList = jpaItemRepository.findAllByOwner(userId);
        List<Booking> allbookings = jpaBooking.findAll();
        for (Item item : itemList) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            List<Booking> bookingListByItemId = new ArrayList<>();
            List<LocalDateTime> dateTimesStart = new ArrayList<>();
            List<LocalDateTime> dateTimesEnd = new ArrayList<>();

            for (Booking booking : allbookings) {
                if (Objects.equals(booking.getItem().getId(), item.getId()) && Status.APPROVED == booking.getStatus()) {
                    bookingListByItemId.add(booking);
                    dateTimesStart.add(booking.getStart());
                    dateTimesEnd.add(booking.getEnd());
                }
            }

            Booking nextBooking = null;
            Booking lastBooking = null;
            LocalDateTime closestPastDateTime = null;
            LocalDateTime firstPastDateTime = null;

            for (LocalDateTime dateTime : dateTimesStart) {
                if (dateTime.isBefore(currentDateTime)) {
                    if (closestPastDateTime == null || dateTime.isAfter(closestPastDateTime)) {
                        closestPastDateTime = dateTime;
                    }
                }
            }

            for (LocalDateTime dateTime : dateTimesEnd) {
                if (dateTime.isAfter(currentDateTime)) {
                    if (firstPastDateTime == null || dateTime.isBefore(firstPastDateTime)) {
                        firstPastDateTime = dateTime;
                    }
                }
            }


            for (Booking booking : bookingListByItemId) {
                if (booking.getStart().equals(closestPastDateTime)) {
                    lastBooking = booking;
                }
            }

            for (Booking booking : bookingListByItemId) {
                if (booking.getEnd().equals(firstPastDateTime)) {
                    nextBooking = booking;
                }
            }
            BookingConcise bookingConciseLast;
            BookingConcise bookingConciseNext;

            if (lastBooking != null || nextBooking != null) {
                bookingConciseLast = BookingConcise.builder().id(nextBooking.getId())
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
