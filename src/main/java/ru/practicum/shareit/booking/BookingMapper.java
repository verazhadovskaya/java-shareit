package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItemResponse;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {

    public static Booking convertToBooking(BookingDto bookingDto, Item item, User user) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setUser(user);
        booking.setStartDate(bookingDto.getStart());
        booking.setEndDate(bookingDto.getEnd());
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static BookingDtoResponse convertToDto(Booking booking) {
        return new BookingDtoResponse()
                .setId(booking.getId())
                .setItem(ItemMapper.convertToDto(booking.getItem()))
                .setBooker(UserMapper.convertToUserDto(booking.getUser()))
                .setStart(booking.getStartDate())
                .setEnd(booking.getEndDate())
                .setStatus(booking.getStatus());
    }

    public static BookingDto convertToBookingDto(Booking booking) {
        return new BookingDto()
                .setId(booking.getId())
                .setItemId(booking.getItem().getId())
                .setUserId(booking.getUser().getId())
                .setStart(booking.getStartDate())
                .setEnd(booking.getEndDate())
                .setStatus(booking.getStatus());
    }

    public static BookingDtoItemResponse convertToDtoItem(Booking booking) {
        return new BookingDtoItemResponse()
                .setId(booking.getId())
                .setBookerId(booking.getUser().getId());
    }
}
