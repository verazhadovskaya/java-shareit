package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("id") long id) {
        log.info("Delete id {}", id);
        userClient.removeUser(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody UserDto userDto) {
        log.info("Create user= {}", userDto);
        return userClient.save(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto, @PathVariable("id") long id) {
        userDto.setId(id);
        log.info("Update user= {}, userId={}", userDto, id);
        return userClient.update(userDto, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") long id) {
        log.info("Get get with id {}", id);
        return userClient.get(id);
    }
}
