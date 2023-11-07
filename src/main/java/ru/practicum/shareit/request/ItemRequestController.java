package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.servise.ItemRequestService;
import ru.practicum.shareit.request.servise.ItemRequestServiceImpl;

import java.util.Optional;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest addItemRequest (@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestBody ItemRequestDto itemRequestDto){
            return itemRequestService.addItemRequest(userId, itemRequestDto);
    }
}
