package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<Item , Integer> {
    Item findItemById (Integer id);

    //Item findAllByOwner (Integer id);

    List <Item> findAllByOwner (Integer id);
}
