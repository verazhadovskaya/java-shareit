package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.InMemoryUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    Long id = 1L;

    @Qualifier("inMemoryUserRepository")
    private final InMemoryUserRepository inMemoryUserRepository;

    @Override
    public Item save(Item item) {
        if (inMemoryUserRepository.checkExistUser(item.getUserId())) {
            item.setId(id);
            items.put(id, item);
            id++;
        } else {
            throw new ObjectNotFoundException("Нет переданного пользователя");

        }
        return item;
    }

    @Override
    public Item update(Item currentItem, Item newItem, Long id) {
        if (newItem.getName() != null) {
            currentItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            currentItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            currentItem.setAvailable(newItem.getAvailable());
        }
        if (newItem.getUserId() != null) {
            currentItem.setUserId(newItem.getUserId());
        }
        return currentItem;
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }

    @Override
    public Item getById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAll(Long userId) {
        List<Item> allItems = new ArrayList<>(items.values());
        List<Item> itemsByUser = allItems.stream()
                .filter(c -> c.getUserId().equals(userId))
                .collect(Collectors.toList());
        return itemsByUser;
    }

    @Override
    public List<Item> getByText(String text) {
        List<Item> allItems = new ArrayList<>(items.values());
        List<Item> itemsByUser = allItems.stream()
                .filter(c -> c.getDescription().toUpperCase().indexOf(text.toUpperCase()) != -1)
                .filter(c -> c.getAvailable())
                .collect(Collectors.toList());
        return itemsByUser;
    }
}
