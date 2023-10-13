package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.validation.ItemValidation;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemValidation itemValidation;

    int idItem = 1;

    @Override
    public Item addItem(Integer id, ItemDto itemDto) {
        itemValidation.checkItem(itemDto);
        itemValidation.checkUserId(id);
        log.info("id {} , itemDto {}", id, itemDto);
        Item item = Item.builder().id(idItem++).name(itemDto.getName()).description(itemDto.getDescription())
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
    public Item getItem(Integer itemId) {
        return itemStorage.getItem(itemId);
    }

    @Override
    public List<Item> getItemUser(Integer userId) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : itemStorage.getItems()) {
            if (item.getOwner() == userId) {
                itemList.add(item);
            }
        }
        return itemList;
    }

    @Override
    public List<Item> getItemByName(String text) {
        List<Item> itemList = new ArrayList<>();
        if (text.length() == 0) {
            return itemList;
        }
        String textToLower = text.toLowerCase();


        for (Item item : itemStorage.getItems()) {
            if (item.getAvailable() && (item.getName().toLowerCase().contains(textToLower) || item.getDescription().toLowerCase().contains(textToLower))) {
                itemList.add(item);
            }
        }
        return itemList;
    }


}
