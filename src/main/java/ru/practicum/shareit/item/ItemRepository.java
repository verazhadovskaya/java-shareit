package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserId(Long userId);

    @Query("SELECT i FROM Item i "
            + "WHERE upper(i.name) like upper(concat('%', ?1, '%')) "
            + "OR upper(i.description) like upper(concat('%', ?1, '%')) "
            + "AND i.available = true")
    List<Item> getByText(String text);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE b.item.id  = ?1 "
            + "AND i.userId  = ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findBookingItemId(Long id, Long userId);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.item.id  = ?1 "
            + "AND b.user.id  = ?2 "
            + "AND b.status  <> ?3 "
            + "AND b.startDate <  ?4 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findBookingItemForUserId(Long id, Long userId, BookingStatus status, LocalDateTime date);


}
