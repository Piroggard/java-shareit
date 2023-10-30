package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
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
    public ItemDtoResponse getItem(@RequestHeader("X-Sharer-User-Id") Integer id, @PathVariable Integer itemId) {
        log.info("Метод - getItem, ");
        return itemService.getItem(itemId, id);
    }

    @GetMapping
    public List<ItemDtoResponse> getItemUser(@RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("getItemUser");
        return itemService.getItemUser(id);
    }

    @GetMapping("/search")
    public List<Item> getItemByName(@RequestParam(name = "text") String text) {
        return itemService.getItemByName(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer id,
                                 @PathVariable Integer itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("addComment id {},itemId {}, text {}", id, itemId, commentDto.getText());

        return itemService.addComment(id, itemId, commentDto);
    }


}
