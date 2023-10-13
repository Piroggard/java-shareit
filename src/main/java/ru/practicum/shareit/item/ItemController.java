package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") Integer id, @RequestBody ItemDto itemDto) {
        log.info("@Post - id {} , itemDto {}", id, itemDto);
        return itemService.addItem(id, itemDto);
    }

    @PatchMapping("{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Integer id, @PathVariable Integer itemId,
                           @RequestBody ItemDto itemDto) {
        log.info("@Patch - id {} , itemDto {}", id, itemDto);
        return itemService.updateItem(id, itemId, itemDto);
    }

    @GetMapping("{itemId}")
    public Item getItem(@PathVariable Integer itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<Item> getItemUser(@RequestHeader("X-Sharer-User-Id") Integer id) {
        return itemService.getItemUser(id);
    }

    @GetMapping("/search")
    public List<Item> getItemByName(@RequestParam(name = "text") String text) {
        return itemService.getItemByName(text);
    }
}
