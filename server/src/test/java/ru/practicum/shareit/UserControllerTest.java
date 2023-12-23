package ru.practicum.shareit;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    ObjectMapper mapper = new ObjectMapper();
    @MockBean
    UserServiceImpl userService;
    @MockBean
    UserMapper userMapper;

    @Autowired
    MockMvc mockMvc;

    User user = new User(1L, "name", "email@email.com");
    UserDto userDto = new UserDto(1L, "name", "email@email.com");

    @Test
    void createUser() throws Exception {
        when(userService.save(any()))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }


    @Test
    void getAllUser() throws Exception {
        when(userService.getAll())
                .thenReturn(List.of(userDto));
        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(List.of(userDto)), result);
    }

    @Test
    void getUser() throws Exception {
        when(userService.get(anyLong()))
                .thenReturn(userDto);
        String result = mockMvc.perform(get("/users/{id}", userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(userDto), result);
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).delete(1L);
    }
}