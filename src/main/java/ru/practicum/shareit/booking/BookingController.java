package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingDtoResponse create(@RequestBody @Valid BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        bookingDto.setUserId(userId);
        return bookingService.save(bookingDto, userId);
    }

    @PatchMapping("/{id}")
    public BookingDtoResponse udpate(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") long id, @RequestParam(value = "approved") boolean approved) {
        return bookingService.update(id, userId, approved);
    }

    @GetMapping("/{id}")
    public BookingDtoResponse get(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.get(id, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> getAll(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.getAll(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(userId, state);
    }
}
