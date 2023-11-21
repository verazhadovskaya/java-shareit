package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Item update(Item currentItem, Item newItem, Long id) {
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
        return itemRepository.getByText(text);
    }
}
