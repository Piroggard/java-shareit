package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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


    @Override
    public Item addItem(Integer id, ItemDto itemDto) {
        itemValidation.checkItem(itemDto);
        itemValidation.checkUserId(id);
        log.info("id {} , itemDto {}", id, itemDto);
        Item item = Item.builder().name(itemDto.getName()).description(itemDto.getDescription())
                .owner(id).available(itemDto.getAvailable()).build();
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(Integer idUser, Integer itemId, ItemDto itemDto) {
        itemValidation.checkItemUpdate(idUser, itemId);
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
        itemValidation.checItemId(itemId);
        ItemDtoResponse itemList = itemStorage.getItemAndBooking(itemId, id);
        itemValidation.checkingDataNull(itemList);
        return itemList;
    }

    @Override
    public List<ItemDtoResponse> getItemUser(Integer userId) {
        return itemStorage.getItemUser(userId);

    }

    @Override
    public List<Item> getItemByName(String text) {
        List<Item> itemList = new ArrayList<>();
        if (text.length() == 0) {
            return itemList;
        }
        String textToLower = text.toLowerCase();
        for (Item item : itemStorage.getItems()) {
            if (item.getAvailable() && (item.getName().toLowerCase().contains(textToLower) ||
                    item.getDescription().toLowerCase().contains(textToLower))) {
                itemList.add(item);
            }
        }
        return itemList;
    }

    @Override
    public CommentDto addComment(Integer idUser, Integer itemId, CommentDto commentDto) {
        itemValidation.checkComment(commentDto.getText(), itemId);
        itemValidation.checkCommentBoocking(idUser, itemId);
        LocalDateTime localDateTime = LocalDateTime.now();
        Item item = itemStorage.getItem(itemId);
        User user = userStorage.getUser(idUser);

        Comment comment = Comment.builder().text(commentDto.getText())
                .item(item)
                .author(user)
                .created(localDateTime).build();
        Comment comment1 = jpaCommentRepository.save(comment);

        CommentDto commentDto1 = CommentDto.builder()
                .id(comment1.getId())
                .text(comment1.getText())
                .itemId(comment1.getItem().getId())
                .authorName(comment1.getAuthor().getName())
                .created(comment1.getCreated()).build();
        return commentDto1;
    }


}
