package ru.practicum.service.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.service.comment.dto.CommentCompilationResponseDto;
import ru.practicum.service.comment.dto.CommentEventResponseDto;
import ru.practicum.service.comment.dto.CommentRequestDto;
import ru.practicum.service.comment.entity.Comment;
import ru.practicum.service.compilation.common.entity.Compilation;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.user.admin.entity.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {
    private static Comment mapToComment(CommentRequestDto requestDto, User author,
                                        LocalDateTime created) {
        Comment comment = new Comment();
        comment.setText(requestDto.getText());
        comment.setCreated(created);
        comment.setAuthor(author);
        return comment;
    }

    public static Comment mapToComment(CommentRequestDto requestDto, Event event, User author,
                                       LocalDateTime created) {
        Comment comment = mapToComment(requestDto, author, created);
        comment.setEvent(event);
        return comment;
    }

    public static Comment mapToComment(CommentRequestDto requestDto, Compilation compilation, User author,
                                       LocalDateTime created) {
        Comment comment = mapToComment(requestDto, author, created);
        comment.setCompilation(compilation);
        return comment;
    }

    public static CommentEventResponseDto mapToEventCommentDto(Comment eventComment) {
        CommentEventResponseDto commentResponseDto = new CommentEventResponseDto();
        commentResponseDto.setId(eventComment.getId());
        commentResponseDto.setText(eventComment.getText());
        commentResponseDto.setCreated(eventComment.getCreated());
        commentResponseDto.setAuthor(eventComment.getAuthor().getId());
        commentResponseDto.setEvent(eventComment.getEvent().getId());
        return commentResponseDto;
    }

    public static CommentCompilationResponseDto mapToCompilationCommentDto(Comment compilationComment) {
        CommentCompilationResponseDto commentResponseDto = new CommentCompilationResponseDto();
        commentResponseDto.setId(compilationComment.getId());
        commentResponseDto.setText(compilationComment.getText());
        commentResponseDto.setCreated(compilationComment.getCreated());
        commentResponseDto.setAuthor(compilationComment.getAuthor().getId());
        commentResponseDto.setCompilation(compilationComment.getCompilation().getId());
        return commentResponseDto;
    }
}
