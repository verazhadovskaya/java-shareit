package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public Item save(Item item) {
        if (item.getAvailable() == null || item.getName() == null
                || item.getDescription() == null || item.getName() == "") {
            throw new ValidationException("Не заполнено обязательное поле");
        }
        return itemRepository.save(item);
    }

    public Item update(Item currentItem, Item newItem, Long id) {
        if (!currentItem.getUserId().equals(newItem.getUserId())) {
            throw new ObjectNotFoundException("Нельзя обновить пользователя");
        }
        return itemRepository.update(currentItem, newItem, id);
    }

    public void delete(Long id) {
        itemRepository.delete(id);
    }

    public Item get(Long id) {
        return itemRepository.getById(id);
    }

    public List<Item> getAll(Long userId) {
        return itemRepository.getAll(userId);
    }

    public List<Item> getByText(String text) {
        if (!StringUtils.hasText(text)) {
            text = "-1";
        }
        return itemRepository.getByText(text);
    }
}
