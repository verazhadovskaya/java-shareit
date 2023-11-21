package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class ItemRequest {
    private Long id;
    private Long userId;
    private String name;
    private String description;
}
