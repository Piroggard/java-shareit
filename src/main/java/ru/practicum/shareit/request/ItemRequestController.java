package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.servise.ItemRequestService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

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
    public ItemResponseDto addItemRequest (@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @RequestBody ItemRequestDto itemRequestDto){
        log.info("Запрос на поиск {} ", itemRequestDto.getDescription());
            return itemRequestService.addItemRequest(userId, itemRequestDto);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.getItemsRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> returnAll(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size ) {
        return itemRequestService.getAllRequests(userId, from,  size);

    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestResponseDto get(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @PathVariable Integer requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
