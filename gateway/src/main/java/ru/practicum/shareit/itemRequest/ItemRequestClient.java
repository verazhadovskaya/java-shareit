package ru.practicum.shareit.itemRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

public class ItemRequestClient extends BaseClient {
    public ItemRequestClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> save(ItemRequestDto itemRequestDto, long userId) {
        return post("", userId, itemRequestDto);
    }


    public ResponseEntity<Object> getAll(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getByUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getById(Long id, long userId) {
        return get("/" + id, userId);
    }
}
