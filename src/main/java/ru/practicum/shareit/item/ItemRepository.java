package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    Item update(Item currentItem, Item newItem, Long id);

    void delete(Long id);

    Item getById(Long id);

    List<Item> getAll(Long userId);

    List<Item> getByText(String text);
}
