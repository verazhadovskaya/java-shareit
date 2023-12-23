package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public static Comment convertToComment(CommentDto commentDto, Item item, User user) {
        Comment comment = new Comment();
        comment.setComment(commentDto.getText());
        comment.setItem(item);
        comment.setUserId(user.getId());
        return comment;
    }

    public static CommentDto convertToDto(Comment comment, User user) {
        return new CommentDto()
                .setId(comment.getId())
                .setText(comment.getComment())
                .setAuthorName(user.getName())
                .setCreated(LocalDateTime.now());

    }
}
