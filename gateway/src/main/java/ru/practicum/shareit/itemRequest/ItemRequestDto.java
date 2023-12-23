package ru.practicum.shareit.itemRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    private Long userId;
    private String description;
    private LocalDateTime created;
}
