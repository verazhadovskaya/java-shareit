package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.Accessors;


import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long userId;
    private List<CommentDto> comments;
    private Long requestId;
}
