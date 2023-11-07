package ru.practicum.shareit.request.servise;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Optional;

public interface ItemRequestService {
   ItemRequest addItemRequest (Integer userId , ItemRequestDto itemRequest);


}
