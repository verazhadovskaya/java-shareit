package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;

public class ItemClient extends BaseClient {
    public ItemClient(RestTemplate rest) {
        super(rest);
    }


    public ResponseEntity<Object> create(long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> update(long id, long userId, ItemDto itemDto) {
        return patch("/" + id, userId, itemDto);
    }

    public ResponseEntity<Object> get(long userId, Long id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getAll(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> delete(long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> getByText(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> saveComment(long id, long userId, CommentDto commentDto) {
        return post("/" + id + "/comment", userId, commentDto);
    }
}
