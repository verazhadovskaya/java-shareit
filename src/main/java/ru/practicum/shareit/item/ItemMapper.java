package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public Item convert(ItemDto item) {
        return new Item()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable())
                .setUserId(item.getUserId());
    }

    public ItemDto convert(Item item) {
        return new ItemDto()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable())
                .setUserId(item.getUserId());
    }
}
