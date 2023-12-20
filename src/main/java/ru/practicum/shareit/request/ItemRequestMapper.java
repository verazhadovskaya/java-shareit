package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.stream.Collectors;

@Component
public class ItemRequestMapper {
    public static ItemRequest convertToItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setUserId(itemRequestDto.getUserId());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(itemRequestDto.getCreated());
        return itemRequest;
    }

    public static ItemRequestDto convertToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto()
                .setId(itemRequest.getId())
                .setUserId(itemRequest.getUserId())
                .setDescription(itemRequest.getDescription())
                .setCreated(itemRequest.getCreated());
        if (itemRequest.getItems() != null) {
            itemRequestDto.setItems(itemRequest.getItems().stream()
                    .map(ItemMapper::convertToDto).collect(Collectors.toList()));
        }
        return itemRequestDto;
    }
}
