package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;

public class UserClient extends BaseClient {

    public UserClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> update(UserDto userDto, long id) {
        return patch("/" + id, id, null, userDto);
    }


    public ResponseEntity<Object> save(UserDto userDto) {
        return post("", null, null, userDto);
    }

    public ResponseEntity<Object> getAll() {
        return get("");
    }

    public ResponseEntity<Object> get(long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> removeUser(long id) {
        return delete("/" + id);
    }
}
