package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserMapper userMapper;
    private final UserServiceImpl userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody @Valid UserDto userDto, @PathVariable("id") long id) {
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
