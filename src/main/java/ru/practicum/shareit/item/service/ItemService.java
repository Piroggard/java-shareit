package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item addItem(ItemDto itemDto, Long userId);

    Item updateItem(ItemDto itemDto, Long userId);

    ItemDto getItem(Long userId, Long itemId);

    List<ItemDto> getItemByName(Long userId, int from, int size);

    Collection<ItemDto> searchItem(String text, int from, int size);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    void deleteItemById(Long userId, Long itemId);
}
