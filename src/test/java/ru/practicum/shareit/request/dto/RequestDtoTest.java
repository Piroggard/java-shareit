package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RequestDtoTest {
    private RequestDto requestDto;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testSetAndGetId() {
        requestDto = RequestDto.builder()
                .id(1L)
                .build();
        Long id = 1L;
        assertEquals(id, requestDto.getId());
    }

    @Test
    public void testSetAndGetDescription() {
        requestDto = RequestDto.builder()
                .description("This is a test description")
                .build();
        String description = "This is a test description";

        assertEquals(description, requestDto.getDescription());
    }

    @Test
    public void testSetAndGetRequestor() {

        UserDto requestor = new UserDto();
        requestDto = RequestDto.builder()
                .requestor(requestor)
                .build();

        assertNotNull(requestDto.getRequestor());
    }

    @Test
    public void testSetAndGetCreatedTime() {
        LocalDateTime created = LocalDateTime.now();
        requestDto = RequestDto.builder()
                .created(created)
                .build();
        assertEquals(created, requestDto.getCreated());
    }

    @Test
    public void testSetAndGetItems() {
        List<ItemDtoRequest> items = new ArrayList<>();
        items.add(new ItemDtoRequest());
        requestDto = RequestDto.builder()
                .items(items)
                .build();
        assertEquals(items, requestDto.getItems());
    }
}
