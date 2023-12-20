package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    private final Item item = new Item(1L, "name", "description", true, 1L, 1L);

    private final User user = new User(1L, "User", "user@email.com");
    private final User itemUser = new User(99L, "itemUser", "user@email.com");
    private final Booking booking = new Booking(1L,
            LocalDateTime.of(2024, 1, 1, 0, 0, 0),
            LocalDateTime.of(2025, 1, 1, 0, 0, 0),
            BookingStatus.WAITING, item, user);

    @Test
    void createTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.save(any()))
                .thenReturn(booking);
        Mockito.when(itemService.get(anyLong(), anyLong()))
                .thenReturn(ItemMapper.convertToDto(item));
        BookingDtoResponse b = bookingService.save(BookingMapper.convertToBookingDto(booking), 2L);
        assertAll("Should return booking",
                () -> assertEquals(b.getStart(), booking.getStartDate()),
                () -> assertEquals(b.getEnd(), booking.getEndDate()),
                () -> assertEquals(b.getBooker().getId(), booking.getUser().getId()),
                () -> assertEquals(b.getItem().getId(), booking.getItem().getId()),
                () -> assertEquals(b.getId(), booking.getId())
        );
    }


    @Test
    void updateTest() {
        Mockito.when(bookingRepository.save(any()))
                .thenReturn(booking);
        Mockito.when(bookingRepository.getById(anyLong()))
                .thenReturn(booking);
        assertEquals(bookingService.update(1L, 1L, true).getStatus(), BookingStatus.APPROVED);
    }

    @Test
    void updateApprovedTest() {
        booking.setStatus(BookingStatus.APPROVED);
        Mockito.when(bookingRepository.save(any()))
                .thenReturn(booking);
        Mockito.when(bookingRepository.getById(anyLong()))
                .thenReturn(booking);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void updateBadUserTest() {
        Mockito.when(bookingRepository.save(any()))
                .thenReturn(booking);
        Mockito.when(bookingRepository.getById(anyLong()))
                .thenReturn(booking);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.update(1L, 2L, true));
    }

    @Test
    void getTest() {
        Mockito.when(bookingRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito.when(bookingRepository.getById(anyLong()))
                .thenReturn(booking);
        BookingDtoResponse b = bookingService.get(1L, 1L);
        assertAll("Should return booking",
                () -> assertEquals(b.getStart(), booking.getStartDate()),
                () -> assertEquals(b.getEnd(), booking.getEndDate()),
                () -> assertEquals(b.getBooker().getId(), booking.getUser().getId()),
                () -> assertEquals(b.getItem().getId(), booking.getItem().getId()),
                () -> assertEquals(b.getId(), booking.getId())
        );

    }

    @Test
    void getBadIdtTest() {
        Mockito.when(bookingRepository.existsById(anyLong()))
                .thenReturn(false);
        Mockito.when(bookingRepository.getById(anyLong()))
                .thenReturn(booking);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.get(1L, 1L));
    }

    @Test
    void getBadUserTest() {
        Mockito.when(bookingRepository.existsById(anyLong()))
                .thenReturn(true);
        User update2 = new User(2L, "User2", "user2@email.com");
        Item item2 = new Item(2L, "name2", "description2", true, 2L, 1L);
        booking.setUser(update2);
        booking.setItem(item2);
        Mockito.when(bookingRepository.getById(anyLong()))
                .thenReturn(booking);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.get(1L, 1L));
    }

    @Test
    void getAllTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.findByUserId(anyLong(), any()))
                .thenReturn(Arrays.asList(booking));
        assertEquals(1, bookingService.getAll(1L, "ALL", 0, 20).size());
    }

    @Test
    void getAllCurrentTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.findByUserIdCurrent(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(Arrays.asList(booking));
        assertEquals(1, bookingService.getAll(1L, "CURRENT", 0, 20).size());
    }

    @Test
    void getAllPastTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.findByUserIdPast(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(Arrays.asList(booking));
        assertEquals(1, bookingService.getAll(1L, "PAST", 0, 20).size());
    }

    @Test
    void getAllFutureTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.findByUserIdFuture(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(Arrays.asList(booking));
        assertEquals(1, bookingService.getAll(1L, "FUTURE", 0, 20).size());
    }


    @Test
    void getAllOwnerTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.findByOwnerId(anyLong(), any()))
                .thenReturn(Arrays.asList(booking));
        assertEquals(1, bookingService.getAllByOwner(1L, "ALL", 0, 20).size());
    }

    @Test
    void getAllOwnerCurrentTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.findByOwnerIdCurrent(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(Arrays.asList(booking));
        assertEquals(1, bookingService.getAllByOwner(1L, "CURRENT", 0, 20).size());
    }

    @Test
    void getAllOwnerPastTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.findByOwnerIdPast(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(Arrays.asList(booking));
        assertEquals(1, bookingService.getAllByOwner(1L, "PAST", 0, 20).size());
    }

    @Test
    void getAllOwnerFutureTest() {
        Mockito.when(userService.get(anyLong()))
                .thenReturn(UserMapper.convertToUserDto(user));
        Mockito.when(bookingRepository.findByOwnerIdFuture(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(Arrays.asList(booking));
        assertEquals(1, bookingService.getAllByOwner(1L, "FUTURE", 0, 20).size());
    }


}
