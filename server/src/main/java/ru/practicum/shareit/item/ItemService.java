package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto save(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto currentItem, ItemDto newItem, Long id);

    void delete(Long id);

    ItemDto get(Long id, Long userId);

    List<ItemDto> getAll(Long userId);

    List<ItemDto> getByText(String text);

    CommentDto saveComment(CommentDto commentDto, Long itemId, Long userId);
}
