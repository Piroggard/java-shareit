package ru.practicum.shareit.booking.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.ValidationData;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.ValidationIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
@AllArgsConstructor
public class BookingValidation {
    BookingStorage bookingStorage;
    ItemStorage itemStorage;
    UserStorage userStorage;

    public void checkItemAvailable (Integer idItem, BookingDto bookingDto) throws ItemAvailableException {
        Item item = itemStorage.getItem(idItem);
        LocalDateTime localDate = LocalDateTime.now();
        if (item == null){
            throw new ValidationIdException("Вешь не найдена");
        }
        if (bookingDto.getEnd() == null || bookingDto.getStart() == null){
            throw new ValidationData("Не заполнено время ");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) ){
            throw new ValidationData("Время начала бранирование позже конца");
        }

        if (bookingDto.getEnd().isEqual(bookingDto.getStart()) && bookingDto.getStart().isAfter(localDate)){
            throw new ValidationData("Мутная провенрка");
        }
        if (bookingDto.getStart().isBefore(localDate)){
            throw new ValidationData("Вчерашняя дата");
        }



        if (!item.getAvailable()){
            throw new ItemAvailableException("Вешь недоступна для бронирования");
        }
    }

    public void bookerValidation (Integer bookerId){
        User user = userStorage.getUser(bookerId);
        if (user == null){
            throw new ValidationIdException("Нет такого пользователя");
        }
    }

    public void checkBookerOrOwer (Integer idBooking , Integer idUser){
        Booking booking = bookingStorage.getBookingById(idBooking);
        if (booking.getBooker().getId() != idUser && booking.getItem().getOwner() != idUser){
            throw new ValidationIdException("Нет разрешения для просмотра");
        }
    }

    public void checkBooking (Integer idBooking){
        if (bookingStorage.getBookingById(idBooking) == null){
            throw new ValidationIdException("Нет такой вещи");
        }

    }

    public void checkBookerOrOwerUser (Integer id){
        User user = userStorage.getUser(id);
        if (user == null){
            throw new ValidationIdException("Нет такого пользователя");
        }
    }

    public void checkUpdateBooking (Integer idBooking , Integer idUser , Boolean approved)throws ItemAvailableException{
        if (approved == null){
            throw new ItemAvailableException("Нет данных для изменения");
        }
       Booking booking = bookingStorage.getBookingById(idBooking);
        if (booking.getItem().getOwner() != idUser){
            throw new ValidationIdException("Нет разрешения на изменение");
        }
    }

    public void checIdkBookerUpdate ( Integer bookingId , Boolean approved){
        Booking booking = bookingStorage.getBookingById(bookingId);
        log.info(""+ booking.getStatus() );
        log.info(""+ approved);
        if (approved && booking.getStatus() == Status.APPROVED){

                throw new ValidationData("Данные уже обновлены ");



        }
    }

    public void checOwner ( Integer ownerId , Integer itemId){
        Item item = itemStorage.getItem(itemId);
        if (item.getOwner() == ownerId){
            throw new ValidationIdException("Сам на себя создает бронирование, ну бред же ");
        }
    }

}
