package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.dto.BookingDtoItemResponse;

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
    private BookingDtoItemResponse lastBooking;
    private BookingDtoItemResponse nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
