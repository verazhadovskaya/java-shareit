package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto save(ItemRequestDto itemRequestDto, Long userId) {
        itemRequestDto.setUserId(userId);
        if (!userRepository.existsById(itemRequestDto.getUserId())) {
            throw new ObjectNotFoundException("Нет пользователя");
        }
        if (itemRequestDto.getDescription() == null) {
            throw new ValidationException("Поле описание должно быть заполнено");
        }
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = ItemRequestMapper.convertToItemRequest(itemRequestDto);
        return ItemRequestMapper.convertToItemRequestDto(repository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAll(Long userId, int from, int size) {
        if (from < 0 || size == 0) {
            throw new ValidationException("from должен быть больше или равен 0, size - больше 0");
        }
        Pageable page = PageRequest.of(from / size, size, Sort.by("created"));
        List<ItemRequestDto> listItemRequest = repository.findAll().stream()
                .filter(obj -> obj.getUserId() != userId)
                .map(ItemRequestMapper::convertToItemRequestDto).collect(Collectors.toList());
        for (ItemRequestDto itemRequestDto : listItemRequest) {
            itemRequestDto.setItems(itemRepository.findAllByRequestId(itemRequestDto.getId()).stream()
                    .map(ItemMapper::convertToDto).collect(Collectors.toList()));
        }
        return listItemRequest;
    }

    @Override
    public List<ItemRequestDto> getByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Нет пользователя");
        }
        List<ItemRequestDto> listItemRequest = repository.findByUserId(userId).stream()
                .map(ItemRequestMapper::convertToItemRequestDto).collect(Collectors.toList());
        for (ItemRequestDto itemRequestDto : listItemRequest) {
            itemRequestDto.setItems(itemRepository.findAllByRequestId(itemRequestDto.getId()).stream()
                    .map(ItemMapper::convertToDto).collect(Collectors.toList()));
        }
        return listItemRequest;
    }

    @Override
    public ItemRequestDto getById(Long id, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Нет пользователя");
        }
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("Нет запроса");
        }
        ItemRequestDto itemRequest = ItemRequestMapper.convertToItemRequestDto(repository.getById(id));
        itemRequest.setItems(itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                .map(ItemMapper::convertToDto).collect(Collectors.toList()));
        return itemRequest;
    }
}
