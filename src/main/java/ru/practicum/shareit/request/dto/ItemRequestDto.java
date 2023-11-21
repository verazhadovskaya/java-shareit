package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class ItemRequestDto {
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
}
