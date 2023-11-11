package ru.practicum.shareit.request.servise;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDtoItem;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.JpaItemRequest;
import ru.practicum.shareit.request.validation.ItemResponseValidation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.JpaUserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@AllArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final JpaItemRequest jpaItemRequest;
    private final JpaUserRepository jpaUserRepository;
    private final ItemResponseValidation itemResponseValidation;
    private final JpaItemRepository jpaItemRepository;

    @Override
    public ItemResponseDto addItemRequest(Integer userId, ItemRequestDto itemRequestDto) {
        User userMakesRequest = jpaUserRepository.findUserById(userId);
        itemResponseValidation.checkItemResponseUser(userMakesRequest);
        itemResponseValidation.checkItemResponse(itemRequestDto);
        LocalDateTime localDateTime = LocalDateTime.now();
        User user = jpaUserRepository.getReferenceById(userId);
        ItemRequest createObjectItemRequest = ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requester(user)
                .created(localDateTime).build();
        log.info("Добавляем объект {} " , createObjectItemRequest);

       jpaItemRequest.save(createObjectItemRequest);


       ItemRequest itemRequest = jpaItemRequest.findAllById(createObjectItemRequest.getId());

        return ItemResponseDto.builder().id(itemRequest.getId())
               .requester(new UserDto(itemRequest.getRequester().getId(),
                       itemRequest.getRequester().getName(), itemRequest.getRequester().getEmail()))
               .created(itemRequest.getCreated())
               .description(itemRequest.getDescription()).build();

    }

    @Override
    public List<ItemRequestResponseDto> getItemsRequests(Integer userId) {
        User userMakesRequest = jpaUserRepository.findUserById(userId);
        itemResponseValidation.checkItemResponseUser(userMakesRequest);
        List<ItemRequest> jpaItemRequests = jpaItemRequest.findByRequester_Id(userId);
        List<ItemRequestResponseDto> itemRequestResponseDtos = new ArrayList<>();
        List<Item> items = jpaItemRepository.findAllByRequest(userId);

        List<ItemRequestResponseDtoItem> itemList = new ArrayList<>();

        for (Item item : items) {
            ItemRequestResponseDtoItem itemRequestResponseDtoItem = ItemRequestResponseDtoItem.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .requestId(item.getRequest())
                    .available(item.getAvailable()).build();
            itemList.add(itemRequestResponseDtoItem);
        }


        for (ItemRequest itemRequest : jpaItemRequests) {
            ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
                    .id(itemRequest.getId())
                    .description(itemRequest.getDescription())
                    .items(itemList)
                    .created(itemRequest.getCreated()).build();
            itemRequestResponseDtos.add(itemRequestResponseDto);
        }
        return itemRequestResponseDtos;
    }

    @Override
    public List<ItemRequestResponseDto> getAllRequests(Integer userId, int from, int size) {

        if (from < 0) {
            throw new ValidationException("Отрицательное значение фром");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);


        List<ItemRequest> jpaItemRequests = jpaItemRequest.findAll();
        List<Item> items = jpaItemRepository.findAll();
        List<ItemRequestResponseDto> itemRequestResponseDtos = new ArrayList<>();

        List<ItemRequestResponseDtoItem> itemList = new ArrayList<>();


        for (ItemRequest itemRequest : jpaItemRequests) {
            if (itemRequest.getId() == userId){
                continue;
            }
            for (Item item : items) {
                try {
                    if (item.getRequest() == itemRequest.getId()){
                        ItemRequestResponseDtoItem itemRequestResponseDtoItem = ItemRequestResponseDtoItem.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .description(item.getDescription())
                                .requestId(item.getRequest())
                                .available(item.getAvailable()).build();
                        itemList.add(itemRequestResponseDtoItem);
                    }
                } catch (NullPointerException nullPointerException){

                    continue;
                }

            }

            ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
                    .id(itemRequest.getId())
                    .description(itemRequest.getDescription())
                    .items(itemList)
                    .created(itemRequest.getCreated()).build();
            itemRequestResponseDtos.add(itemRequestResponseDto);
        }
        return itemRequestResponseDtos;
    }

    @Override
    public ItemRequestResponseDto getRequestById(Integer userId, Integer requestId) {

        User userMakesRequest = jpaUserRepository.findUserById(userId);
        itemResponseValidation.checkItemResponseUser(userMakesRequest);
        ItemRequest itemRequestq = jpaItemRequest.findAllById(requestId);
        itemResponseValidation.checkItemRequest(itemRequestq);



        List<Item> items = jpaItemRepository.findAllByRequest(requestId);


        List<ItemRequestResponseDtoItem> itemList = new ArrayList<>();

        for (Item item : items) {
            ItemRequestResponseDtoItem itemRequestResponseDtoItem = ItemRequestResponseDtoItem.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .requestId(item.getRequest())
                    .available(item.getAvailable()).build();
            itemList.add(itemRequestResponseDtoItem);
        }

        ItemRequest itemRequest = jpaItemRequest.findAllById(requestId);

        ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .items(itemList)
                .created(itemRequest.getCreated()).build();

        return itemRequestResponseDto;








    }
}
