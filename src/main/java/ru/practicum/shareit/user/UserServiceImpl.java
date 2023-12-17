package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errors.ObjectFoundException;
import ru.practicum.shareit.errors.ObjectNotFoundException;
import ru.practicum.shareit.errors.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        User user = UserMapper.convertToUser(userDto);
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Емейл должен быть заполнен и содержать @");
        }
        return UserMapper.convertToUserDto(repository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long id) {
        User currentUser = repository.getById(id);
        User newUser = UserMapper.convertToUser(userDto);
        if (repository.existsById(id)) {
            if (newUser.getName() != null) {
                currentUser.setName(newUser.getName());
            }
            if (newUser.getEmail() != null) {
                for (User oneUser : repository.findAll()) {
                    if (oneUser.getEmail().equals(newUser.getEmail()) && !oneUser.getId().equals(id)) {
                        throw new ObjectFoundException("Пользователь уже существует");
                    }
                }
                currentUser.setEmail(newUser.getEmail());
            }
            repository.save(currentUser);
        } else {
            throw new ObjectNotFoundException("Нет пользователя для обновления");
        }
        return UserMapper.convertToUserDto(repository.save(currentUser));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = repository.getById(id);
        repository.delete(user);
    }

    @Override
    @Transactional
    public UserDto get(Long id) {
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("Нет пользователя для обновления");
        }
        return UserMapper.convertToUserDto(repository.getById(id));
    }

    @Override
    @Transactional
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::convertToUserDto).collect(Collectors.toList());
    }
}
