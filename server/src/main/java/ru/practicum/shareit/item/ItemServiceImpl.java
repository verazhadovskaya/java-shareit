package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto save(ItemDto itemDto, Long userId) {
        if (itemDto.getAvailable() == null ||
                itemDto.getDescription() == null || itemDto.getDescription().equals("") ||
                itemDto.getName() == null || itemDto.getName().equals("")) {
            throw new ValidationException("Нет обязательного поля");
        }

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Нет пользователя");
        }
        Item item = ItemMapper.convertToItem(itemDto);
        item.setUserId(userId);
        return ItemMapper.convertToDto(repository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto currentItemDto, ItemDto newItemDto, Long id) {
        Item currentItem = ItemMapper.convertToItem(currentItemDto);
        Item newItem = ItemMapper.convertToItem(newItemDto);
        currentItem.setId(id);
        if (newItem.getName() != null) {
            currentItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            currentItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            currentItem.setAvailable(newItem.getAvailable());
        }
        if (!currentItem.getUserId().equals(newItem.getUserId())) {
            throw new ObjectNotFoundException("Нельзя обновить пользователя");
        }
        return ItemMapper.convertToDto(repository.save(currentItem));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Item item = repository.getById(id);
        repository.delete(item);
    }

    @Override
    public ItemDto get(Long id, Long userId) {
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("Нет item");
        }
        ItemDto itemDto = ItemMapper.convertToDto(repository.getById(id));
        List<Booking> allBookings = new ArrayList<>();
        if (itemDto.getUserId().equals(userId)) {
            allBookings = repository.findBookingId(id);
        } else {
            allBookings = repository.findBookingByUserId(id);
        }

        if (!allBookings.isEmpty()) {
            List<Booking> bookings = allBookings.stream()
                    .filter(b -> b.getItem().getId().equals(itemDto.getId()))
                    .collect(Collectors.toList());
            Booking lastBooking = bookings.stream()
                    .filter(obj -> !(obj.getStatus().equals(BookingStatus.REJECTED)))
                    .filter(obj -> obj.getStartDate().isBefore(LocalDateTime.now()))
                    .min((obj1, obj2) -> obj2.getStartDate().compareTo(obj1.getStartDate())).orElse(null);
            Booking nextBooking = bookings.stream()
                    .filter(obj -> !(obj.getStatus().equals(BookingStatus.REJECTED)))
                    .filter(obj -> obj.getStartDate().isAfter(LocalDateTime.now()))
                    .min(Comparator.comparing(Booking::getStartDate)).orElse(null);
            if (lastBooking != null) {
                itemDto.setLastBooking(BookingMapper.convertToDtoItem(lastBooking));
            }
            if (nextBooking != null) {
                itemDto.setNextBooking(BookingMapper.convertToDtoItem(nextBooking));
            }

        }
        List<Comment> comments = commentRepository.findAllByItemId(id);
        List<CommentDto> commentsDto = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (Comment comment : comments) {
            User user = users.stream()
                    .filter(u -> u.getId().equals(comment.getUserId()))
                    .findFirst().get();
            commentsDto.add(CommentMapper.convertToDto(comment, user));
        }
        itemDto.setComments(commentsDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        List<ItemDto> newListItem = new ArrayList<>();
        List<ItemDto> listItem = repository.findByUserId(userId).stream()
                .map(ItemMapper::convertToDto).collect(Collectors.toList());
        List<Booking> allBookings = repository.findBooking();
        for (ItemDto itemDto : listItem) {
            List<Booking> bookings = allBookings.stream()
                    .filter(b -> b.getItem().getId().equals(itemDto.getId()))
                    .collect(Collectors.toList());
            if (!bookings.isEmpty()) {
                Booking lastBooking = bookings.stream()
                        .filter(obj -> !(obj.getStatus().equals(BookingStatus.REJECTED)))
                        .filter(obj -> obj.getStartDate().isBefore(LocalDateTime.now()))
                        .min((obj1, obj2) -> obj2.getStartDate().compareTo(obj1.getStartDate())).orElse(null);
                Booking nextBooking = bookings.stream()
                        .filter(obj -> !(obj.getStatus().equals(BookingStatus.REJECTED)))
                        .filter(obj -> obj.getStartDate().isAfter(LocalDateTime.now()))
                        .min(Comparator.comparing(Booking::getStartDate)).orElse(null);
                if (lastBooking != null) {
                    itemDto.setLastBooking(BookingMapper.convertToDtoItem(lastBooking));
                }
                if (nextBooking != null) {
                    itemDto.setNextBooking(BookingMapper.convertToDtoItem(nextBooking));
                }
            }
            List<Comment> comments = commentRepository.findAllByItemId(itemDto.getId());
            List<CommentDto> commentsDto = new ArrayList<>();
            List<User> users = userRepository.findAll();
            for (Comment comment : comments) {
                User user = (User) users.stream()
                        .filter(u -> u.getId().equals(comment.getUserId()))
                        .findFirst().get();
                commentsDto.add(CommentMapper.convertToDto(comment, user));
            }
            itemDto.setComments(commentsDto);
            newListItem.add(itemDto);
        }
        return newListItem;
    }

    @Override
    public List<ItemDto> getByText(String text) {
        if (!StringUtils.hasText(text)) {
            text = "-1";
        }
        return repository.getByText(text).stream()
                .map(ItemMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto saveComment(CommentDto commentDto, Long itemId, Long userId) {
        Item item = repository.getById(itemId);
        User user = userRepository.getById(userId);
        if (repository.findBookingItemForUserId(itemId, userId, BookingStatus.REJECTED, LocalDateTime.now()) > 0
                && !commentDto.getText().isEmpty() && !commentDto.getText().isBlank()) {
            Comment comment = CommentMapper.convertToComment(commentDto, item, user);
            return CommentMapper.convertToDto(commentRepository.save(comment), user);
        } else {
            throw new ValidationException("Нельзя добавить комментарий");
        }
    }
}