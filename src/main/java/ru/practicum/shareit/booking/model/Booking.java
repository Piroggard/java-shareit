package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "start_of_booking")
    private LocalDateTime start;
    @Column(name = "end_of_booking")
    private LocalDateTime end;
    @Column(name = "item_id")
    private Integer itemId;
    @Column(name = "booker_id")
    private Integer booker;
    @Column(name = "status")
    private Status status;

}
