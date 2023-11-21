package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class ItemRequestDto {
    private Long id;
    private Long userId;
    private String name;
    private String description;
}
