package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemMapper itemMapper;
    private final ItemServiceImpl itemService;

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto itemRequest, @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemMapper.convert(itemRequest);
        item.setUserId(userId);
        return itemMapper.convert(itemService.save(item));
    }

    @PatchMapping("/{id}")
    public ItemDto udpate(@RequestBody @Valid ItemDto itemRequest, @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") long id) {
        Item currentItem = itemService.get(id);
        Item newItem = itemMapper.convert(itemRequest);
        newItem.setUserId(userId);
        return itemMapper.convert(itemService.update(currentItem, newItem, id));
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAll(userId).stream()
                .map(itemMapper::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") Long id) {
        return itemMapper.convert(itemService.get(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> getByText(@RequestParam(required = false) String text) {
        return itemService.getByText(text).stream()
                .map(itemMapper::convert)
                .collect(Collectors.toList());
    }
}
