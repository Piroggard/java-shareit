package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Integer id, ItemDto itemDto);

    Item updateItem(Integer idUser, Integer itemId, ItemDto itemDto);

    ItemDtoResponse getItem(Integer itemId , Integer id);

    List<Item> getItemUser(Integer itemId);

    List<Item> getItemByName(String text);
}
