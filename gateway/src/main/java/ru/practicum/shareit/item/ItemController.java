package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long id,
                                         @RequestBody @Valid ItemDto itemDto) {
        log.info("Update itemId= {}, userId={}", id, userId);
        return itemClient.update(id, userId, itemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable("id") Long id) {
        log.info("Get itemId= {}, userId={}", id, userId);
        return itemClient.get(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getAll(userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        itemClient.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getByText(@RequestParam(required = false) String text) {
        return itemClient.getByText(text);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@RequestBody @Valid CommentDto commentDto,
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable("id") long id) {
        return itemClient.saveComment(id, userId, commentDto);
    }
}
