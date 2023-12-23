package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;
    private final Item item = new Item(1L, "name", "description", true, 1L, 1L);

    private final User user = new User(1L, "User", "user@email.com");

    private final Comment comment = new Comment(1L, "comment", item, 1L);

    List<Comment> comments = Arrays.asList(comment);
    private final Booking booking = new Booking(1L,
            LocalDateTime.of(2000, 1, 1, 0, 0, 0),
            LocalDateTime.of(2000, 1, 1, 0, 0, 0),
            BookingStatus.WAITING, item, user);

    List<Booking> bookings = Arrays.asList(booking);

    @Test
    void createTest() {
        Mockito.when(userRepository.existsById(1L))
                .thenReturn(true);
        Mockito.when(itemRepository.save(any()))
                .thenReturn(item);
        ItemDto i = itemService.save(ItemMapper.convertToDto(item), 1L);
        assertAll("Should return item",
                () -> assertEquals(i.getDescription(), item.getDescription()),
                () -> assertEquals(i.getName(), item.getName()),
                () -> assertEquals(i.getAvailable(), item.getAvailable()),
                () -> assertEquals(i.getUserId(), item.getUserId()),
                () -> assertEquals(i.getRequestId(), item.getRequestId())
        );
    }

    @Test
    void createBadRequestTest() {
        Mockito.when(userRepository.existsById(1L))
                .thenReturn(true);
        Mockito.when(itemRepository.save(any()))
                .thenReturn(item);
        item.setDescription(null);
        Assertions.assertThrows(ValidationException.class, () -> itemService.save(ItemMapper.convertToDto(item), 1L));
        Assertions.assertEquals(0, itemService.getAll(1L).size());
    }

    @Test
    void createUserNotFoundTest() {
        Mockito.when(userRepository.existsById(1L))
                .thenReturn(false);
        Mockito.when(itemRepository.save(any()))
                .thenReturn(item);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> itemService.save(ItemMapper.convertToDto(item), 1L));
        Assertions.assertEquals(0, itemService.getAll(1L).size());
    }

    @Test
    void updateTest() {
        Item updateItem = new Item(1L, "nameUpdate", "descriptionUpdate", true, 1L, 1L);
        Mockito.when(itemRepository.save(any()))
                .thenReturn(updateItem);
        ItemDto i = itemService.update(ItemMapper.convertToDto(item), ItemMapper.convertToDto(updateItem), 1L);
        assertAll("Should return item",
                () -> assertEquals(i.getDescription(), updateItem.getDescription()),
                () -> assertEquals(i.getName(), updateItem.getName()),
                () -> assertEquals(i.getAvailable(), updateItem.getAvailable()),
                () -> assertEquals(i.getUserId(), updateItem.getUserId()),
                () -> assertEquals(i.getRequestId(), updateItem.getRequestId()),
                () -> assertEquals(i.getId(), updateItem.getId())
        );
    }

    @Test
    void updateChangeUserTest() {
        Item updateItem = new Item(1L, "nameUpdate", "descriptionUpdate", true, 2L, 1L);
        Mockito.when(itemRepository.save(any()))
                .thenReturn(updateItem);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> itemService.update(ItemMapper.convertToDto(item), ItemMapper.convertToDto(updateItem), 1L));
        Assertions.assertEquals(0, itemService.getAll(1L).size());
    }


    @Test
    void deleteTest() {
        Mockito.when(itemRepository.getById(1L))
                .thenReturn(item);
        Assertions.assertEquals(0, itemService.getAll(1L).size());
    }

    @Test
    void getTest() {
        Mockito.when(itemRepository.existsById(1L))
                .thenReturn(true);
        Mockito.when(itemRepository.getById(1L))
                .thenReturn(item);
        Mockito.when(itemRepository.findBookingByUserId(1L))
                .thenReturn(bookings);
        Mockito.when(itemRepository.findBookingId(1L))
                .thenReturn(bookings);
        Mockito.when(commentRepository.findAllByItemId(1L))
                .thenReturn(comments);
        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(user));
        ItemDto i = itemService.get(1L, 1L);
        assertAll("Should return item",
                () -> assertEquals(i.getDescription(), item.getDescription()),
                () -> assertEquals(i.getName(), item.getName()),
                () -> assertEquals(i.getId(), item.getId()),
                () -> assertEquals(i.getAvailable(), item.getAvailable()),
                () -> assertEquals(i.getUserId(), item.getUserId()),
                () -> assertEquals(i.getRequestId(), item.getRequestId())
        );
    }

    @Test
    void getAll() {
        Mockito.when(itemRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(item));
        Mockito.when(itemRepository.findBooking())
                .thenReturn(bookings);
        Mockito.when(commentRepository.findAllByItemId(1L))
                .thenReturn(comments);
        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(user));
        Assertions.assertEquals(1, itemService.getAll(1L).size());
    }

    @Test
    void getbyText() {
        Mockito.when(itemRepository.getByText(any()))
                .thenReturn(Arrays.asList(item));
        Assertions.assertEquals(1, itemService.getByText("test").size());
    }
}
