package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;

@Component
public class BookingMapper {
    public Booking convert(BookingDto booking) {
        return new Booking()
                .setId(booking.getId())
                .setItemId(booking.getItemId())
                .setUserId(booking.getUserId())
                .setStartDate(booking.getStartDate())
                .setEndDate(booking.getEndDate())
                .setFeedback(booking.getFeedback());
    }

    public BookingDto convert(Booking booking) {
        return new BookingDto()
                .setId(booking.getId())
                .setItemId(booking.getItemId())
                .setUserId(booking.getUserId())
                .setStartDate(booking.getStartDate())
                .setEndDate(booking.getEndDate())
                .setFeedback(booking.getFeedback());
    }
}
