package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;


import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserServiceImpl userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable("id") long id) {
        return userService.update(userDto, id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

}
