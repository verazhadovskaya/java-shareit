package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = :userId ")
    List<Booking> findByUserId(Long userId, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId = :userId ")
    List<Booking> findByOwnerId(Long userId, Pageable page);


    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = :userId "
            + "AND b.startDate <  :date "
            + "AND b.endDate >  :date ")
    List<Booking> findByUserIdCurrent(Long userId, LocalDateTime date, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = :userId "
            + "AND b.endDate <  :date ")
    List<Booking> findByUserIdPast(Long userId, LocalDateTime date, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = ?1 "
            + "AND b.startDate >  ?2 ")
    List<Booking> findByUserIdFuture(Long userId, LocalDateTime date, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = ?1 "
            + "AND b.status =  ?2 ")
    List<Booking> findByUserIdWaiting(Long userId, BookingStatus status, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = ?1 "
            + "AND b.status =  ?2 ")
    List<Booking> findByUserIdReject(Long userId, BookingStatus status, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.startDate <  ?2 "
            + "AND b.endDate >  ?2 ")
    List<Booking> findByOwnerIdCurrent(Long userId, LocalDateTime date, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.endDate <  ?2 ")
    List<Booking> findByOwnerIdPast(Long userId, LocalDateTime date, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.startDate >  ?2 ")
    List<Booking> findByOwnerIdFuture(Long userId, LocalDateTime date, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.status =  ?2 ")
    List<Booking> findByOwnerIdWaiting(Long userId, BookingStatus status, Pageable page);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.status =  ?2 ")
    List<Booking> findByOwnerIdReject(Long userId, BookingStatus status, Pageable page);


}
