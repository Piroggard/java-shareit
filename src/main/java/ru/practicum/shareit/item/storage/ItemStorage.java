package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemStorage {
    private Map<Integer, Item> saveItem = new HashMap<>();

    public Item addItem(Item item) {
        saveItem.put(item.getId(), item);
        return saveItem.get(item.getId());
    }

    public List<Item> getItems() {
        List<Item> itemList = new ArrayList<>();
        Collection<Item> items = saveItem.values();
        for (Item item : items) {
            itemList.add(item);
        }
        return itemList;
    }

    public Item getItem(Integer id) {
        for (Item item : getItems()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
