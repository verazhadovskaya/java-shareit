package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public BookingDtoResponse save(BookingDto bookingDto, Long userId) {
        if (userService.get(userId) == null) {
            throw new ObjectNotFoundException("Нет пользователя");
        }
        if (itemService.get(bookingDto.getItemId(), userId) == null) {
            throw new ObjectNotFoundException("Item не доступен");
        }
        if (!itemService.get(bookingDto.getItemId(), userId).getAvailable()) {
            throw new ValidationException("Item не доступен");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("Дата окончания и дата начала не могут быть пустыми");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата окончания не может быть раньше текущей");
        }
        if (bookingDto.getStart().toLocalDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Дата начала не может быть больше текущей");
        }
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Дата окончания не может быть равна дате начала");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Дата окончания не может быть меньше дате начала");
        }
        User user = userMapper.convertToUser(userService.get(userId));
        user.setId(userId);
        Item item = itemMapper.convertToItem(itemService.get(bookingDto.getItemId(), userId));
        item.setId(bookingDto.getItemId());
        if (item.getUserId().equals(userId)) {
            throw new ObjectNotFoundException("пользователь не может бронировать свою же вещь");
        }
        Booking booking = bookingMapper.convertToBooking(bookingDto, item, user);
        booking.setUser(user);
        booking.setStatus(BookingStatus.WAITING);
        return bookingMapper.convertToDto(repository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoResponse update(Long id, Long userId, boolean approved) {
        Booking currentBooking = repository.getById(id);
        currentBooking.setId(id);
        if (repository.getById(id).getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Бронирование уже подтверждено");
        }
        if (approved) {
            currentBooking.setStatus(BookingStatus.APPROVED);
        } else {
            currentBooking.setStatus(BookingStatus.REJECTED);
        }
        if (!repository.getById(id).getItem().getUserId().equals(userId)) {
            throw new ObjectNotFoundException("Данный пользователь " +
                    "не может обновлять чужое бронирование");
        }

        return bookingMapper.convertToDto(repository.save(currentBooking));
    }

    @Override
    public BookingDtoResponse get(Long id, Long userId) {
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("Нет бронирования");
        }
        if (!repository.getById(id).getUser().getId().equals(userId)
                && !repository.getById(id).getItem().getUserId().equals(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не может получить чужое бронирование");
        }
        return bookingMapper.convertToDto(repository.getById(id));
    }

    @Override
    public List<BookingDtoResponse> getAll(Long userId, String state, int from, int size) {
        Pageable page = pageParam(from, size);
        if (userService.get(userId) == null) {
            throw new ObjectNotFoundException("Нет пользователя");
        }
        LocalDateTime date = LocalDateTime.now();
        List<BookingDtoResponse> list = new ArrayList<>();
        if (state.equals("ALL")) {
            list = repository.findByUserId(userId, page).stream()
                    .map(BookingMapper::convertToDto).collect(Collectors.toList());
        } else if (state.equals("CURRENT")) {
            list = repository.findByUserIdCurrent(userId, date, page).stream()
                    .map(BookingMapper::convertToDto).collect(Collectors.toList());
        } else if (state.equals("PAST")) {
            list = repository.findByUserIdPast(userId, date, page).stream()
                    .map(BookingMapper::convertToDto).collect(Collectors.toList());
        } else if (state.equals("FUTURE")) {
            list = repository.findByUserIdFuture(userId, date, page).stream()
                    .map(BookingMapper::convertToDto).collect(Collectors.toList());
        } else if (state.equals("WAITING")) {
            list = repository.findByUserIdWaiting(userId, BookingStatus.WAITING, page).stream()
                    .map(BookingMapper::convertToDto).collect(Collectors.toList());
        } else if (state.equals("REJECTED")) {
            list = repository.findByUserIdReject(userId, BookingStatus.REJECTED, page).stream()
                    .map(BookingMapper::convertToDto).collect(Collectors.toList());
        } else {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return list;
    }

    @Override
    public List<BookingDtoResponse> getAllByOwner(Long userId, String state, int from, int size) {
            Pageable page = pageParam(from, size);
            if (userService.get(userId) == null) {
                throw new ObjectNotFoundException("Нет пользователя");
            }
            LocalDateTime date = LocalDateTime.now();
            List<BookingDtoResponse> list = new ArrayList<>();
            if (state.equals("ALL")) {
                list = repository.findByOwnerId(userId, page).stream()
                        .map(BookingMapper::convertToDto).collect(Collectors.toList());
            } else if (state.equals("CURRENT")) {
                list = repository.findByOwnerIdCurrent(userId, date, page).stream()
                        .map(BookingMapper::convertToDto).collect(Collectors.toList());
            } else if (state.equals("PAST")) {
                list = repository.findByOwnerIdPast(userId, date, page).stream()
                        .map(BookingMapper::convertToDto).collect(Collectors.toList());
            } else if (state.equals("FUTURE")) {
                list = repository.findByOwnerIdFuture(userId, date, page).stream()
                        .map(BookingMapper::convertToDto).collect(Collectors.toList());
            } else if (state.equals("WAITING")) {
                list = repository.findByOwnerIdWaiting(userId, BookingStatus.WAITING, page).stream()
                        .map(BookingMapper::convertToDto).collect(Collectors.toList());
            } else if (state.equals("REJECTED")) {
                list = repository.findByOwnerIdReject(userId, BookingStatus.REJECTED, page).stream()
                        .map(BookingMapper::convertToDto).collect(Collectors.toList());
            } else {
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
            return list;
    }

    public Pageable pageParam(int from, int size) {
        if (from < 0 || size == 0) {
            throw new ValidationException("from должен быть больше или равен 0, size - больше 0");
        }
        return PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "startDate"));
    }
}
