package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    ObjectMapper mapper = new ObjectMapper();
    @MockBean
    BookingServiceImpl bookingService;
    @MockBean
    BookingMapper bookingMapper;

    @Autowired
    MockMvc mockMvc;
    ItemDto item = new ItemDto(1L, "name", "description", true, 1L, null, null, new ArrayList<>(), 1L);
    UserDto userDto = new UserDto(1L, "name", "email@email.com");
    BookingDtoResponse bookingDto = new BookingDtoResponse(1L, item, userDto,
            LocalDateTime.of(2024, 1, 1, 0, 0, 0),
            LocalDateTime.of(2025, 1, 1, 0, 0, 0),
            BookingStatus.WAITING);

    @Test
    void create() throws Exception {
        when(bookingService.save(any(), anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        when(bookingService.update(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{id}?approved=true", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk());

        verify(bookingService).update(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void getAllBooking() throws Exception {
        when(bookingService.getAll(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @Test
    void getByIdBooking() throws Exception {
        when(bookingService.get(anyLong(), anyLong()))
                .thenReturn(bookingDto);
        String result = mockMvc.perform(get("/bookings/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(bookingDto), result);
    }

    @Test
    void getAllOwnerBooking() throws Exception {
        when(bookingService.getAllByOwner(anyLong(), (BookingState) any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(List.of(bookingDto)), result);
    }

}
