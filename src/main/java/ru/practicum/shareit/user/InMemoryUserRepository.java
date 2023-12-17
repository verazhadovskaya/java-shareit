package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errors.ObjectFoundException;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    Long id = 1L;

    @Override
    public User save(@Valid User user) {
        for (User oneUser : users.values()) {
            if (oneUser.getEmail().equals(user.getEmail())) {
                throw new ObjectFoundException("Пользователь уже существует");
            }
        }
        user.setId(id);
        users.put(user.getId(), user);
        id++;
        return user;
    }

    @Override
    public User update(@Valid User currentUser, User newUser, Long id) {
        if (users.containsKey(id)) {
            if (newUser.getName() != null) {
                currentUser.setName(newUser.getName());
            }
            if (newUser.getEmail() != null) {
                for (User oneUser : users.values()) {
                    if (oneUser.getEmail().equals(newUser.getEmail()) && !oneUser.getId().equals(id)) {
                        throw new ObjectFoundException("Пользователь уже существует");
                    }
                }
                currentUser.setEmail(newUser.getEmail());
            }
            users.put(id, currentUser);
        } else {
            throw new ObjectNotFoundException("Нет пользователя для обновления");
        }
        return currentUser;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public boolean checkExistUser(Long id) {
        boolean isExist = false;
        if (users.containsKey(id)) {
            isExist = true;
        }
        return isExist;
    }
}
