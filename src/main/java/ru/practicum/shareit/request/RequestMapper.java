package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class RequestMapper {
    public ItemRequest convert(ItemRequestDto itemRequestRequest) {
        return new ItemRequest()
                .setId(itemRequestRequest.getId())
                .setUserId(itemRequestRequest.getUserId())
                .setName(itemRequestRequest.getName())
                .setDescription(itemRequestRequest.getDescription());
    }

    public ItemRequestDto convert(ItemRequest itemRequest) {
        return new ItemRequestDto()
                .setId(itemRequest.getId())
                .setUserId(itemRequest.getUserId())
                .setName(itemRequest.getName())
                .setDescription(itemRequest.getDescription());
    }
}
