package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.model.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


/**
 * TODO Sprint add-controllers.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Table(name = "items")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
    @Column(name = "item_name", nullable = false)
     String name;
    @Column(name = "description", nullable = false)
     String description;
    @Column(name = "owner_id", nullable = false)
     Long ownerId;
    @Column(name = "is_available")
     Boolean available;
    @Column
     Long request;
    @OneToMany(mappedBy = "item")
     List<Booking> bookings;
    @OneToMany(mappedBy = "item")
     List<Comment> comments;
}
