package ru.practicum.shareit.request.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationFailureException;
import ru.practicum.shareit.exception.ValidationIdException;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Component
@Slf4j
@AllArgsConstructor
public class ItemResponseValidation {
    public void checkItemResponse(ItemRequestDto itemRequestDto) {
        String description = itemRequestDto.getDescription();
        if (description == null) {
            throw new ValidationFailureException("Не заполнено описание ");
        }
    }

    public void checkItemResponseUser(User user) {

        if (user == null) {
            throw new ValidationIdException("Пользователь не найден ");
        }
    }

    public void checkItemRequest(ItemRequest itemRequest) {

        if (itemRequest == null) {
            throw new ValidationIdException("Запись не найден ");
        }
    }


}
