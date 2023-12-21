package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody @Valid ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.save(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                       @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping
    public List<ItemRequestDto> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getByUser(userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDto getById(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getById(id, userId);
    }

}
