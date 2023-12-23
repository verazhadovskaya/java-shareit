package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemMapper itemMapper;
    private final ItemServiceImpl itemService;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.save(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto udpate(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") long id) {
        Item currentItem = itemMapper.convertToItem(itemService.get(id, userId));
        Item newItem = itemMapper.convertToItem(itemDto);
        newItem.setUserId(userId);
        return itemService.update(itemMapper.convertToDto(currentItem), itemMapper.convertToDto(newItem), id);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        System.out.println("Get itemId= {}, userId={}" + userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        System.out.println("Get itemId= {}, userId={}" + id + userId);
        return itemService.get(id, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> getByText(@RequestParam(required = false) String text) {
        return itemService.getByText(text);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long id) {
        return itemService.saveComment(commentDto, id, userId);
    }

}
