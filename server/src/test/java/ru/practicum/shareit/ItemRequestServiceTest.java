package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    private final Item item = new Item(1L, "name", "description", true, 1L, 1L);
    private final List<Item> items = Arrays.asList(item);
    private final ItemRequest itemRequest = new ItemRequest(1L, 1L, "description", LocalDateTime.of(2000, 1, 1, 0, 0, 0), items);

    @Test
    void createTest() {
        Mockito.when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);
        Mockito.when(userRepository.existsById(1L))
                .thenReturn(true);
        ItemRequestDto ir = itemRequestService.save(ItemRequestMapper.convertToItemRequestDto(itemRequest), 1L);
        assertAll("Should return create item request",
                () -> assertEquals(ir.getDescription(), itemRequest.getDescription()),
                () -> assertEquals(ir.getCreated(), itemRequest.getCreated()),
                () -> assertEquals(ir.getUserId(), itemRequest.getUserId()),
                () -> assertEquals(ir.getItems().size(), itemRequest.getItems().size())
        );
    }

    @Test
    void getByIdTest() {
        Mockito.when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito.when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito.when(itemRequestRepository.getById(anyLong()))
                .thenReturn(itemRequest);
        Mockito.when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(items);
        ItemRequestDto ir = itemRequestService.getById(1L, 1L);
        assertAll("Should return item request",
                () -> assertEquals(ir.getDescription(), itemRequest.getDescription()),
                () -> assertEquals(ir.getCreated(), itemRequest.getCreated()),
                () -> assertEquals(ir.getUserId(), itemRequest.getUserId()),
                () -> assertEquals(ir.getId(), itemRequest.getId()),
                () -> assertEquals(ir.getItems().size(), itemRequest.getItems().size())
        );
    }
}
