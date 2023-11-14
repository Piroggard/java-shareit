package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.comment.dto.CommentMapper.toCommentDto;
import static ru.practicum.shareit.mappers.BookingMapper.toBookingDtoShort;
import static ru.practicum.shareit.mappers.ItemMapper.toItem;
import static ru.practicum.shareit.mappers.ItemMapper.toItemDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional
    @SneakyThrows
    public Item addItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item item = toItem(user, itemDto);
        log.info("Добавлен предмет {}, владелец: id = {}", itemDto, userId);
        item.setOwnerId(userId);
        item.setRequest(itemDto.getRequestId());

        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item itemUpdate = itemRepository.findById(itemDto.getId()).orElseThrow(() ->
                new ItemNotFoundException("Предмет не найден " + itemDto.getId()));
        Item item = toItem(user, itemDto);
        itemUpdate.setOwnerId(userId);
        if (Objects.nonNull(item.getName())) {
            itemUpdate.setName(item.getName());
        }
        if (Objects.nonNull(item.getDescription())) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (Objects.nonNull(item.getAvailable())) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        return itemRepository.save(itemUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItem(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Предмет не найден " + itemId));
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        ItemDto itemDto = toItemDto(item);
        if (item.getOwnerId().equals(userId)) {
            addLastAndNextDateTimeForBookingToItem(itemDto);
        }
        itemDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemByName(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }
        Pageable pageable = PageRequest.of(from / size, size);
        if (itemRepository.findAllByOwnerId(userId, pageable) == null) {
            throw new ItemNotFoundException("У пользователя нет предметов для аренды " + userId);
        }
        return getItemUsers(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> searchItem(String text, int from, int size) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        String textToLowerCase = text.toLowerCase();
        if ((from < 0 || size < 0 || (from == 0 && size == 0))) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Неправильный параметр пагинации");
        }
        Pageable pageable;
        if (text == null || text.isEmpty()) {
            log.debug("Предмет по запросу {} не найден", text);
            return Collections.emptyList();
        } else {
            pageable = PageRequest.of(from / size, size);
        }
        log.info("Выполнен поиск среди предметов по : {}.", text);
        return itemRepository.searchItem(textToLowerCase, pageable)
                .stream()
                .map(item -> toItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Комментарий не может быть пустым");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "комментарий  к предмету с id = '" + itemId
                        + "' пользователем с id = " + userId + " ; нет информации о пользователе."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "комментарий к предмету с id = '" + itemId
                        + "' пользователем с id = '" + userId + "' - отсутствует запись о вещи."));

        List<Booking> bookings = item.getBookings();

        if (bookings
                .stream()
                .filter(booking -> (booking.getBooker().getId().equals(userId))
                        && !booking.getStart().isAfter(LocalDateTime.now())
                        && !booking.getStatus().equals(Status.REJECTED)
                )
                .collect(Collectors.toList()).size() == 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST,
                    "Комментировать может только арендатор предмета, с наступившим началом времени бронирования " +
                            "и статусом НЕ REJECTED");
        }

        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);

        return toCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteItemById(Long userId, Long itemId) {
        userRepository.findById(userId);
        itemRepository.deleteById(itemId);
    }

    private void addLastAndNextDateTimeForBookingToItem(ItemDto itemDto) {
        LocalDateTime timeNow = LocalDateTime.now();

        Booking next = bookingRepository
                .findFirstBookingByItemIdAndStartIsAfterAndStatusNotLikeOrderByStartAsc(itemDto.getId(),
                        timeNow, Status.REJECTED);
        Booking last = bookingRepository
                .findFirstBookingByItemIdAndStartIsBeforeAndStatusNotLikeOrderByStartDesc(itemDto.getId(),
                        timeNow, Status.REJECTED);

        if (next != null) {
            itemDto.setNextBooking(toBookingDtoShort(next));
        } else {
            itemDto.setNextBooking(null);
        }

        if (last != null) {
            itemDto.setLastBooking(toBookingDtoShort(last));
        } else {
            itemDto.setLastBooking(null);
        }
    }


    public List<ItemDto> getItemUsers(Long userId, Pageable pageable) {
        List<ItemDto> itemDtoResponses = new ArrayList<>();
        List<Item> itemList = itemRepository.findAllByOwnerId(userId, pageable);
        List<Booking> allBookings = bookingRepository.findAll();
        for (Item item : itemList) {
            Booking nextBooking = null;
            Booking lastBooking = null;
            LocalDateTime closestPastDateTime = null;
            LocalDateTime firstPastDateTime = null;
            LocalDateTime currentDateTime = LocalDateTime.now();
            List<Booking> bookingListByItemId = new ArrayList<>();
            List<LocalDateTime> dateTimesStart = new ArrayList<>();
            List<LocalDateTime> dateTimesEnd = new ArrayList<>();
            BookingDtoShort bookingConciseLast;
            BookingDtoShort bookingConciseNext;

            for (Booking booking : allBookings) {
                if (Objects.equals(booking.getItem().getId(), item.getId()) && Status.APPROVED == booking.getStatus()) {
                    bookingListByItemId.add(booking);
                    dateTimesStart.add(booking.getStart());
                    dateTimesEnd.add(booking.getEnd());
                }
            }
            for (LocalDateTime dateTime : dateTimesStart) {
                if (dateTime.isBefore(currentDateTime)) {
                    if (closestPastDateTime == null || dateTime.isAfter(closestPastDateTime)) {
                        closestPastDateTime = dateTime;
                    }
                }
            }
            for (LocalDateTime dateTime : dateTimesEnd) {
                if (dateTime.isAfter(currentDateTime)) {
                    if (firstPastDateTime == null || dateTime.isBefore(firstPastDateTime)) {
                        firstPastDateTime = dateTime;
                    }
                }
            }
            for (Booking booking : bookingListByItemId) {
                if (booking.getStart().equals(closestPastDateTime)) {
                    lastBooking = booking;
                }
            }
            for (Booking booking : bookingListByItemId) {
                if (booking.getEnd().equals(firstPastDateTime)) {
                    nextBooking = booking;
                }
            }

            if (lastBooking != null || nextBooking != null) {
                bookingConciseLast = BookingDtoShort.builder().id(nextBooking.getId())
                        .bookerId(nextBooking.getBooker().getId()).build();
                bookingConciseNext = BookingDtoShort.builder().id(lastBooking.getId())
                        .bookerId(lastBooking.getBooker().getId()).build();
            } else {
                bookingConciseLast = null;
                bookingConciseNext = null;
            }
            ItemDto itemDtoResponse = ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .nextBooking(bookingConciseLast)
                    .lastBooking(bookingConciseNext).build();
            itemDtoResponses.add(itemDtoResponse);
        }
        return itemDtoResponses;
    }
}
