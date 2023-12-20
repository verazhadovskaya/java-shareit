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
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
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
        assertEquals(itemService.save(ItemMapper.convertToDto(item), 1L).getDescription(), item.getDescription());
        assertEquals(itemService.save(ItemMapper.convertToDto(item), 1L).getName(), item.getName());
        assertEquals(itemService.save(ItemMapper.convertToDto(item), 1L).getAvailable(), item.getAvailable());
        assertEquals(itemService.save(ItemMapper.convertToDto(item), 1L).getUserId(), item.getUserId());
        assertEquals(itemService.save(ItemMapper.convertToDto(item), 1L).getRequestId(), item.getRequestId());
        assertNotNull(itemService.save(ItemMapper.convertToDto(item), 1L).getId());
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
        assertEquals(itemService.update(ItemMapper.convertToDto(item), ItemMapper.convertToDto(updateItem), 1L).getDescription(), updateItem.getDescription());
        assertEquals(itemService.update(ItemMapper.convertToDto(item), ItemMapper.convertToDto(updateItem), 1L).getName(), updateItem.getName());
        assertEquals(itemService.update(ItemMapper.convertToDto(item), ItemMapper.convertToDto(updateItem), 1L).getAvailable(), updateItem.getAvailable());
        assertEquals(itemService.update(ItemMapper.convertToDto(item), ItemMapper.convertToDto(updateItem), 1L).getUserId(), updateItem.getUserId());
        assertEquals(itemService.update(ItemMapper.convertToDto(item), ItemMapper.convertToDto(updateItem), 1L).getRequestId(), updateItem.getRequestId());
        assertEquals(itemService.update(ItemMapper.convertToDto(item), ItemMapper.convertToDto(updateItem), 1L).getId(), updateItem.getId());
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
        Mockito.when(commentRepository.findAllByItemId(1L))
                .thenReturn(comments);
        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(user));
        assertEquals(itemService.get(1L, 1L).getDescription(), item.getDescription());
        assertEquals(itemService.get(1L, 1L).getName(), item.getName());
        assertEquals(itemService.get(1L, 1L).getId(), item.getId());
        assertEquals(itemService.get(1L, 1L).getAvailable(), item.getAvailable());
        assertEquals(itemService.get(1L, 1L).getUserId(), item.getUserId());
        assertEquals(itemService.get(1L, 1L).getRequestId(), item.getRequestId());
    }

    @Test
    void getAll() {
        Mockito.when(itemRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(item));
        Mockito.when(itemRepository.findBookingByUserId(1L))
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
