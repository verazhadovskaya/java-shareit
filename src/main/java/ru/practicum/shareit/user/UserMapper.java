package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public User convert(UserDto user) {
        return new User()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail());
    }

    public UserDto convert(User user) {
        return new UserDto()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail());
    }

}
