package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking implements Comparable<Booking> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "start_of_booking")
    private LocalDateTime start;
    @Column(name = "end_of_booking")
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    @Fetch(FetchMode.JOIN)
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private User booker;
    @Column(name = "status")
    private Status status;

    public Booking(int id, Item item, User booker, Status status) {
        this.id = id;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }

    @Override
    public int compareTo(Booking otherBooking) {
        return Integer.compare(this.id, otherBooking.id);
    }
}
