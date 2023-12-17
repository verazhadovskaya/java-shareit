package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Емейл не может быть пустой и емейл должен содержать @");
        }
        return userRepository.save(user);
    }

    public User update(User currentUser, User newUser, Long id) {
        return userRepository.update(currentUser, newUser, id);
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }

    public User get(Long id) {
        return userRepository.getById(id);
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }
}
