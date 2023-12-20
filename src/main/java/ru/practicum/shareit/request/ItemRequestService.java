package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto save(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> getAll(Long userId, int from, int size);
    List<ItemRequestDto> getByUser(Long userId);

    ItemRequestDto getById(Long id, Long userId);
}
