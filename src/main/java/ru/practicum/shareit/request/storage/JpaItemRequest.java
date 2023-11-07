package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;



public interface JpaItemRequest extends JpaRepository<ItemRequest, Integer> {
    ItemRequest findAllById (Integer id);

}
