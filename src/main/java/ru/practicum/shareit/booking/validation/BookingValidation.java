package ru.practicum.shareit.booking.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.ValidationFailureException;
import ru.practicum.shareit.exception.ValidationIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class BookingValidation {

    public void checkItemAvailable(BookingDto bookingDto, Item item) {
        LocalDateTime localDate = LocalDateTime.now();
        if (item == null) {
            throw new ValidationIdException("Вещь не найдена");
        }
        if (bookingDto.getEnd() == null || bookingDto.getStart() == null) {
            throw new ValidationFailureException("Не заполнено время");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationFailureException("Время начала бронирования позже конца");
        }
        if (bookingDto.getEnd().isEqual(bookingDto.getStart()) && bookingDto.getStart().isAfter(localDate)) {
            throw new ValidationFailureException("Неправильная дата");
        }
        if (bookingDto.getStart().isBefore(localDate)) {
            throw new ValidationFailureException("Неправильная дата");
        }
        if (!item.getAvailable()) {
            throw new ItemAvailableException("Вещь недоступна для бронирования");
        }
    }

    public void bookerValidation(User user) {
        if (user == null) {
            throw new ValidationIdException("Пользователь не найден");
        }
    }

    public void checkBookerOrOwer(List<Booking> bookingUser, List<Booking> booking) {
        if (booking.isEmpty() && bookingUser.isEmpty()) {
            throw new ValidationIdException("Нет разрешения для просмотра");
        }
    }

    public void checkBooking(Booking booking) {
        if (booking == null) {
            throw new ValidationIdException("Вещь не найдена");
        }
    }

    public void checkBookerOrOwerUser(User user) {
        if (user == null) {
            throw new ValidationIdException("Пользователь не найден");
        }
    }

    public void checkUpdateBooking(Integer idUser, Boolean approved, Booking booking) {
        if (approved == null) {
            throw new ItemAvailableException("Нет данных для изменения");
        }
        if (booking.getItem().getOwner() != idUser) {
            throw new ValidationIdException("Нет разрешения на изменение");
        }
    }

    public void checIdkBookerUpdate(Boolean approved, Booking booking) {
        log.info("Получен статус" + booking.getStatus());
        if (approved && booking.getStatus() == Status.APPROVED) {
            throw new ValidationFailureException("Данные уже обновлены ");
        }
    }

    public void checOwner(Integer ownerId, Item itemChecOwner) {
        if (itemChecOwner.getOwner() == ownerId) {
            throw new ValidationIdException("Ошибка бронирования ");
        }
    }
}
