package ru.practicum.shareit.request.servise;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {
   ItemResponseDto addItemRequest (Integer userId , ItemRequestDto itemRequest);
   List<ItemRequestResponseDto> getItemsRequests(Integer userId);

   List<ItemRequestResponseDto> getAllRequests(Integer userId , int from, int size);
   ItemRequestResponseDto getRequestById(Integer userId, Integer requestId);
}
