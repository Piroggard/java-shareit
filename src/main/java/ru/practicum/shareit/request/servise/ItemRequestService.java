package ru.practicum.shareit.request.servise;

import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestResponseDto;
import ru.practicum.shareit.request.ItemResponseDto;

import java.util.List;

public interface ItemRequestService {
   ItemResponseDto addItemRequest (Integer userId , ItemRequestDto itemRequest);
   List<ItemRequestResponseDto> getItemsRequests(Integer userId);

   List<ItemRequestResponseDto> getAllRequests(Integer userId , int from, int size);
   ItemRequestResponseDto getRequestById(Integer userId, Integer requestId);
}
