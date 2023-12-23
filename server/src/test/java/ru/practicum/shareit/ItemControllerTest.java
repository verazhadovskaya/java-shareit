package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    ObjectMapper mapper = new ObjectMapper();
    @MockBean
    ItemServiceImpl itemService;
    @MockBean
    ItemMapper itemMapper;
    @Autowired
    MockMvc mockMvc;

    Item item = new Item(1L, "name", "description", true, 1L, 1L);
    ItemDto itemDto = new ItemDto(1L, "name", "description", true, 1L, null, null, new ArrayList<>(), 1L);

    CommentDto commentDto = new CommentDto(1L, "comment", "", LocalDateTime.of(2000, 1, 1, 0, 0, 0));

    @Test
    void create() throws Exception {
        when(itemService.save(any(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }



    @Test
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).delete(1L);
    }

    @Test
    void getByTextItem() throws Exception {
        when(itemService.getByText(anyString()))
                .thenReturn(List.of(itemDto));
        String result = mockMvc.perform(get("/items/search?text=descr")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(List.of(mapper.writeValueAsString(itemDto)).toString(), result);
    }

    @Test
    void createComment() throws Exception {
        when(itemService.saveComment(any(), anyLong(), anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/{id}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        when(itemService.update(any(), any(), anyLong()))
                .thenReturn(itemDto);
        when(itemService.get(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }
}
