package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
    BookingDtoResponse save(BookingDto bookingDto, Long userId);

    BookingDtoResponse update(Long id, Long userId, boolean approved);

    BookingDtoResponse get(Long id, Long userId);

    List<BookingDtoResponse> getAll(Long id, BookingState state, int from, int size);

    List<BookingDtoResponse> getAllByOwner(Long id, BookingState state, int from, int size);

    ;
}
