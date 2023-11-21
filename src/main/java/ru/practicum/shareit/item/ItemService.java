package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemService {
    Item save(Item item);

    Item update(Item currentItem, Item newItem, Long id);

    void delete(Long id);

    Item get(Long id);

    List<Item> getAll(Long userId);

    List<Item> getByText(String text);
}
