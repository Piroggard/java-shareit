package ru.practicum.shareit.request.servise;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.JpaItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.JpaUserRepository;

import java.time.LocalDateTime;


@Slf4j
@AllArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    JpaItemRequest jpaItemRequest;
    JpaUserRepository jpaUserRepository;

    @Override
    public ItemRequest addItemRequest(Integer userId, ItemRequestDto itemRequestDto) {
        LocalDateTime localDateTime = LocalDateTime.now();
        User user = jpaUserRepository.getReferenceById(userId);
        ItemRequest createObjectItemRequest = ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requester(user)
                .end(localDateTime).build();
        log.info("Добавляем объект {} " , createObjectItemRequest);

       return jpaItemRequest.save(createObjectItemRequest);

    }
}
