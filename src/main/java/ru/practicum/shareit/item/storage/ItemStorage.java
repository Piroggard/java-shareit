package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemStorage {
    JpaItemRepository jpaItemRepository;

    public ItemStorage(JpaItemRepository jpaItemRepository) {
        this.jpaItemRepository = jpaItemRepository;
    }

    public Item addItem(Item item) {

        return jpaItemRepository.save(item);
    }

    public List<Item> getItems() {

        return jpaItemRepository.findAll();
    }

    public Item getItem(Integer id) {
        return jpaItemRepository.findItemById(id);
    }
}
