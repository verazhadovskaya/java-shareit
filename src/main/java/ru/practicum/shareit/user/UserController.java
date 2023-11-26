package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

import ru.practicum.shareit.errors.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserMapper userMapper;
    private final UserServiceImpl userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userRequest) {
        User user = userMapper.convert(userRequest);
        return userMapper.convert(userService.save(user));
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody @Valid UserDto userRequest, @PathVariable("id") long id) {
        User currentUser = userService.get(id);
        User newUser = userMapper.convert(userRequest);
        return userMapper.convert(userService.update(currentUser, newUser, id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream()
                .map(userMapper::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id) {
        return userMapper.convert(userService.get(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

}
