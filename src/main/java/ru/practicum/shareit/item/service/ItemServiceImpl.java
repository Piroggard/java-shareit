package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.storage.JpaBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.storage.JpaCommentRepository;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service

public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemValidation itemValidation;
    private JpaCommentRepository jpaCommentRepository;
    private UserStorage userStorage;
    private BookingStorage bookingStorage;
    private JpaBooking jpaBooking;
    private MappingComment mappingComment;


    @Override
    public Item addItem(Integer id, ItemDto itemDto) {
        User user = userStorage.getUser(id);
        itemValidation.checkItem(itemDto);
        itemValidation.checkUserId(user);
        log.info("id {} , itemDto {}", id, itemDto);
        Item item = Item.builder().name(itemDto.getName()).description(itemDto.getDescription())
                .owner(id).available(itemDto.getAvailable()).build();
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(Integer idUser, Integer itemId, ItemDto itemDto) {

        itemValidation.checkItemUpdate(idUser, itemStorage.getItem(itemId));
        Item item = itemStorage.getItem(itemId);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return itemStorage.addItem(item);
    }

    @Override
    public ItemDtoResponse getItem(Integer itemId, Integer id) {
        itemValidation.checItemId(itemStorage.getItem(itemId));
        ItemDtoResponse item = itemStorage.getItemAndBooking(itemId, id);
        itemValidation.checkingDataNull(item);
        return item;
    }

    @Override
    public List<ItemDtoResponse> getItemUser(Integer userId) {
        return itemStorage.getItemUser(userId);

    }

    @Override
    public List<Item> getItemByName(String text) {

        String textToLower = text.toLowerCase();
        log.info(textToLower);
        if (text.length() == 0) {
            List<Item> itemList = new ArrayList<>();
            return itemList;
        }
        return itemStorage.findAllByNameOrDescription(textToLower);
    }

    @Override
    public CommentDto addComment(Integer idUser, Integer itemId, CommentDto commentDto) {
        LocalDateTime start = bookingStorage.getBookingById(itemId).getStart();
        itemValidation.checkComment(commentDto.getText(), start);
        List<Booking> bookings = jpaBooking.findAllByBooker_IdAndItem_Id(idUser, itemId);
        itemValidation.checkCommentBoocking(idUser, bookings);
        LocalDateTime localDateTime = LocalDateTime.now();
        Item item = itemStorage.getItem(itemId);
        User user = userStorage.getUser(idUser);

        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(user)
                .created(localDateTime).build();
        Comment comment1 = jpaCommentRepository.save(comment);
        return mappingComment.mappingCommentInCommentDto(comment1);
    }
}
