package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class Booking {
    private Long id;
    private Long itemId;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String feedback;
}
