package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.errors.ObjectFoundException;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;


import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    private final User user = new User(1L, "User", "user@email.com");
    private final User user2 = new User(2L, "User2", "user2@email.com");

    @Test
    void createUserTest() {
        Mockito.when(userRepository.save(any()))
                .thenReturn(user);
        assertEquals(userService.save(UserMapper.convertToUserDto(user)).getEmail(), user.getEmail());
        assertEquals(userService.save(UserMapper.convertToUserDto(user)).getName(), user.getName());
        assertNotNull(userService.save(UserMapper.convertToUserDto(user)).getId());
    }

    @Test
    void createUserWithoutEmailTest() {
        Mockito.when(userRepository.save(any()))
                .thenReturn(user);
        user.setEmail(null);
        Assertions.assertThrows(ValidationException.class, () -> userService.save(UserMapper.convertToUserDto(user)));
        Assertions.assertEquals(0, userRepository.findAll().size());
    }

    @Test
    void updateUserTest() {
        Mockito.when(userRepository.existsById(1L))
                .thenReturn(true);
        Mockito.when(userRepository.getById(1L))
                .thenReturn(user);
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userRepository.findAll())
                .thenReturn(users);
        User updateUser = new User(1L, "updateUser", "updateUser@email.com");
        Mockito.when(userRepository.save(any()))
                .thenReturn(updateUser);
        assertEquals(userService.update(UserMapper.convertToUserDto(updateUser), 1L).getEmail(), updateUser.getEmail());
        assertEquals(userService.update(UserMapper.convertToUserDto(updateUser), 1L).getName(), updateUser.getName());
        assertNotNull(userService.update(UserMapper.convertToUserDto(updateUser), 1L).getId());
    }

    @Test
    void updateUserWithNotFoundIdTest() {
        Mockito.when(userRepository.existsById(1L))
                .thenReturn(false);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.update(UserMapper.convertToUserDto(user), 1L));
    }

    @Test
    void updateUserWithExistEmailTest() {
        Mockito.when(userRepository.existsById(2L))
                .thenReturn(true);
        Mockito.when(userRepository.getById(2L))
                .thenReturn(user);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        Mockito.when(userRepository.findAll())
                .thenReturn(users);
        User updateUser = new User(2L, "updateUser2", "user@email.com");
        Assertions.assertThrows(ObjectFoundException.class, () -> userService.update(UserMapper.convertToUserDto(updateUser), 2L));
    }

    @Test
    void deleteTest() {
        Mockito.when(userRepository.getById(1L))
                .thenReturn(user);
        Assertions.assertEquals(0, userService.getAll().size());
    }

    @Test
    void getAllTest() {
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userRepository.findAll())
                .thenReturn(users);
        Assertions.assertEquals(1, userService.getAll().size());
    }

    @Test
    void getByIdTest() {
        Mockito.when(userRepository.getById(1L))
                .thenReturn(user);
        Mockito.when(userRepository.existsById(1L))
                .thenReturn(true);
        assertEquals(userService.get(1L).getEmail(), user.getEmail());
        assertEquals(userService.get(1L).getName(), user.getName());
        assertEquals(userService.get(1L).getId(), user.getId());
    }

    @Test
    void getByIdNotExistTest() {
        Mockito.when(userRepository.existsById(1L))
                .thenReturn(false);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.get(1L));
    }
}
