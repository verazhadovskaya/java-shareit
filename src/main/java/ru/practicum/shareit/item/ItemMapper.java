package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public static Item convertToItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setUserId(itemDto.getUserId());
        return item;
    }

    public static ItemDto convertToDto(Item item) {
        return new ItemDto()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable())
                .setUserId(item.getUserId());
    }

    public static ItemDto convertToDtoWithBooking(Item item, Booking lastBooking, Booking nextBooking) {
        return new ItemDto()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable())
                .setUserId(item.getUserId());
    }
}
