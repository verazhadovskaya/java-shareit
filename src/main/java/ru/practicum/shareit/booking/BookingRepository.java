package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = :userId "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByUserId(Long userId);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId = :userId "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByOwnerId(Long userId);


    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = :userId "
            + "AND b.startDate <  :date "
            + "AND b.endDate >  :date "
            + "ORDER BY b.startDate ASC")
    List<Booking> findByUserIdCurrent(Long userId, LocalDateTime date);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = :userId "
            + "AND b.endDate <  :date "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByUserIdPast(Long userId, LocalDateTime date);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = ?1 "
            + "AND b.startDate >  ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByUserIdFuture(Long userId, LocalDateTime date);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = ?1 "
            + "AND b.status =  ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByUserIdWaiting(Long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.user.id = ?1 "
            + "AND b.status =  ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByUserIdReject(Long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.startDate <  ?2 "
            + "AND b.endDate >  ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByOwnerIdCurrent(Long userId, LocalDateTime date);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.endDate <  ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByOwnerIdPast(Long userId, LocalDateTime date);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.startDate >  ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByOwnerIdFuture(Long userId, LocalDateTime date);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.status =  ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByOwnerIdWaiting(Long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.userId  = ?1 "
            + "AND b.status =  ?2 "
            + "ORDER BY b.startDate DESC")
    List<Booking> findByOwnerIdReject(Long userId, BookingStatus status);


}
