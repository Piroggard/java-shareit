package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class RequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    public RequestDto addItemRequest(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                     @Valid @RequestBody(required = false) RequestDto requestDto) {
        return itemRequestService.addItemRequest(requestDto, userId);
    }

    @GetMapping
    public List<RequestDtoWithRequest> getRequests(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        return itemRequestService.getItemRequest(userId);
    }

    @GetMapping(path = "/all")
    public List<RequestDtoWithRequest> getAllRequests(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                      @RequestParam(name = "from", defaultValue = "0") int from,
                                                      @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemRequestService.getAllItemRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDtoWithRequest getRequestById(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                @PathVariable Long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
