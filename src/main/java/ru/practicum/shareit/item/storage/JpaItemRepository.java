package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

public interface JpaItemRepository extends JpaRepository<Item , Integer> {
    Item findItemById (Integer id);
}
