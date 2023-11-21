package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

interface UserService {
    User save(User user);

    User update(User currentUser, User newUser, Long id);

    void delete(Long id);

    User get(Long id);

    List<User> getAll();

}
