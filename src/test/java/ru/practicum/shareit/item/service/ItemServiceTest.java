package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    private ModelMapper mapper = new ModelMapper();
    @InjectMocks
    private ItemServiceImpl itemService;
    private User user;

    private Comment comment;
    private Booking booking;
    private Booking booking2;
    private Item item;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();


        item = Item.builder()
                .id(1L)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1L)
                .ownerId(user.getId())
                .comments(new ArrayList<>())
                .build();
        comment = Comment.builder()
                .id(1L)
                .text("Комментарий")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();
        booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    void saveItemTest() {
        when(itemRepository.save(any()))
                .thenReturn(item);
        when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user));

        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest())
                .build();

        assertEquals(item.getName(), itemService.addItem(itemDto, user.getId()).getName());
    }


    @Test
    void saveItemWithEmptyNameTest() {
        item.setName("");
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(user.getId())
                .available(item.getAvailable())
                .requestId(item.getRequest())
                .build();
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> itemService.addItem(itemDto, 999L));

        assertEquals("Пользователь не найден 999", exception.getMessage());
    }

    @Test
    void saveItemWithEmptyAvailableTest() {
        item.setAvailable(null);
        ItemDto itemDto = mapper.map(item, ItemDto.class);
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> itemService.addItem(itemDto, 999L));

        assertEquals("Пользователь не найден 999", exception.getMessage());
    }

    @Test
    void saveItemWithWrongUserIDTest() {
        when(userRepository.findById(77L))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id=77 не найден"));
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(user.getId())
                .available(item.getAvailable())
                .requestId(item.getRequest())
                .build();
        var exception = assertThrows(
                NotFoundException.class,
                () -> itemService.addItem(itemDto, 77L));

        assertEquals("404 NOT_FOUND \"Пользователь с id=77 не найден\"", exception.getMessage());
    }

    @Test
    void updateItemTest() {
        when(itemRepository.findById(any()))
                .thenReturn(Optional.ofNullable(item));

        when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepository.save(any()))
                .thenReturn(item);

        item.setName("Щётка для обуви updated");
        item.setDescription("Стандартная щётка для обуви updated");
        ItemDto itemDto = mapper.map(item, ItemDto.class);
        Item updatedItem = itemService.updateItem(itemDto, 1L);
        assertEquals(updatedItem, item);

    }

    @Test
    void searchItemNullTest() {

        assertEquals(List.of(), itemService.searchItem("".toLowerCase(), 0, 20));
        assertEquals(List.of(), itemService.searchItem(" ", 0, 20));
    }

    @Test
    void postCommentTest() {
        booking.setBooker(user);
        item.setBookings(Collections.singletonList(booking));
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(item));

        when(commentRepository.save(any()))
                .thenReturn(comment);
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        CommentDto commentDto1 = itemService.addComment(1L, 1L, commentDto);

        assertEquals(commentDto1, commentDto);
    }
    @Test
    void addEmptyCommentsTest() {

        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        commentDto.setText("");

        var exception = assertThrows(
                BadRequestException.class,
                () -> itemService.addComment(1L, 1L, commentDto));

        assertEquals("400 BAD_REQUEST \"Комментарий не может быть пустым\"", exception.getMessage());
    }

    @Test
    void postCommentWrongUserTest() {
        booking.setBooker(user);
        item.setBookings(Collections.singletonList(booking));

        CommentDto commentDto = mapper.map(comment, CommentDto.class);

        var exception = assertThrows(
                NotFoundException.class,
                () -> itemService.addComment(1L, 1L, commentDto));

        assertEquals("404 NOT_FOUND \"комментарий  к предмету с id = '1' " +
                "пользователем с id = 1 ; нет информации о пользователе.\"", exception.getMessage());
    }

    @Test
    void postCommentFromWrongItemTest() {

        CommentDto commentDto = mapper.map(comment, CommentDto.class);

        var exception = assertThrows(
                NotFoundException.class,
                () -> itemService.addComment(1L, 1L, commentDto));

        assertEquals("404 NOT_FOUND \"комментарий  к предмету с id = '1' " +
                "пользователем с id = 1 ; нет информации о пользователе.\"", exception.getMessage());
    }

    @Test
    void saveItemWithWrongUserTest() {
        when(userRepository.findById(77L))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id=77 не найден"));
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(user.getId())
                .available(item.getAvailable())
                .requestId(item.getRequest())
                .build();
        var exception = assertThrows(
                NotFoundException.class,
                () -> itemService.addItem(itemDto, 77L));

        assertEquals("404 NOT_FOUND \"Пользователь с id=77 не найден\"", exception.getMessage());
    }

    @Test
    void addEmptyCommentTest() {

        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        commentDto.setText("");

        var exception = assertThrows(
                BadRequestException.class,
                () -> itemService.addComment(1L, 1L, commentDto));

        assertEquals("400 BAD_REQUEST \"Комментарий не может быть пустым\"", exception.getMessage());
    }

    @Test
    void postCommentWithBlankContentThrowsBadRequestException() {

        Long userId = 1L;
        Long itemId = 2L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("");

        assertThrows(BadRequestException.class, () -> {
            itemService.addComment(userId, itemId, commentDto);
        });
    }
}
