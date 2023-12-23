package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
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

    @Query ("SELECT i FROM Item i "
            + "WHERE upper(i.name) like upper(concat('%', :text, '%')) "
            + "OR upper(i.description) like upper(concat('%', :text, '%')) "
            + "AND i.available = true")
    List<Item> getByText(@Param("text") String text);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.id  = :id "
            + "ORDER BY b.startDate DESC")
    List<Booking> findBookingId(@Param("id") Long id);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE b.user.id = i.id "
            + "ORDER BY b.startDate DESC")
    List<Booking> findBookingByUserId(@Param("id") Long id);


    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "ORDER BY b.startDate DESC")
    List<Booking> findBooking();

    @Query("SELECT count(b) FROM Booking b "
            + "WHERE b.item.id  = :id "
            + "AND b.user.id  = :userId "
            + "AND b.status  <> :status "
            + "AND b.startDate <  :date")
    Integer findBookingItemForUserId(@Param("id") Long id, @Param("userId") Long userId, @Param("status") BookingStatus status, @Param("date") LocalDateTime date);

    List<Item> findAllByRequestId(Long requestId);

}
