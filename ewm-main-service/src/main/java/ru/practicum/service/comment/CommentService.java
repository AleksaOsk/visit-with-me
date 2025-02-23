package ru.practicum.service.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.service.comment.dto.CommentCompilationResponseDto;
import ru.practicum.service.comment.dto.CommentEventResponseDto;
import ru.practicum.service.comment.dto.CommentRequestDto;
import ru.practicum.service.comment.entity.Comment;
import ru.practicum.service.compilation.common.CompilationService;
import ru.practicum.service.compilation.common.entity.Compilation;
import ru.practicum.service.event.common.EventService;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.user.admin.entity.User;
import ru.practicum.service.user.admin.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private UserServiceImpl userService;
    private EventService eventService;
    private CompilationService compilationService;

    Object updateComment(@Positive(message = "userId не может быть отрицательным")
                         @PathVariable("userId") Long userId,
                         @Positive(message = "commentId не может быть отрицательным")
                         @PathVariable("commentId") Long commentId,
                         @Valid @RequestBody CommentRequestDto requestDto) {
        log.info("Пришел запрос на изменение комментария с id = {}", commentId);
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найдено с id = " + commentId +
                                                         " или вы не являетесь его владельцем"));
        if (!comment.getText().equals(requestDto.getText())) {
            comment.setText(requestDto.getText());
        }
        if (comment.getEvent() != null) {
            return CommentMapper.mapToEventCommentDto(comment);
        } else {
            return CommentMapper.mapToCompilationCommentDto(comment);
        }
    }

    CommentEventResponseDto addEventComment(Long userId, Long eventId, CommentRequestDto requestDto) {
        log.info("Пришел запрос на добавление комментария к мероприятию с id = {} от пользователя с id = {}",
                eventId, userId);
        User author = userService.getUser(userId);
        Event event = eventService.getPublishedEvent(eventId);
        Comment comment = CommentMapper.mapToComment(requestDto, event, author, LocalDateTime.now());
        comment = commentRepository.save(comment);
        return CommentMapper.mapToEventCommentDto(comment);
    }

    CommentCompilationResponseDto addCompComment(Long userId, Long compId, CommentRequestDto requestDto) {
        log.info("Пришел запрос на добавление комментария к подборке с id = {} от пользователя с id = {}",
                compId, userId);
        User author = userService.getUser(userId);
        Compilation compilation = compilationService.getCompilationById(compId);
        Comment comment = CommentMapper.mapToComment(requestDto, compilation, author, LocalDateTime.now());
        comment = commentRepository.save(comment);
        return CommentMapper.mapToCompilationCommentDto(comment);
    }

    void deleteComment(Long userId, Long commentId) {
        log.info("Пришел запрос на удаление комментария с id = {} от пользователя с id = {}", commentId, userId);
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найдено с id = " + commentId +
                                                         " или вы не являетесь его владельцем"));
        commentRepository.delete(comment);
    }

    public List<CommentEventResponseDto> getComments(Event event) {
        List<Comment> comments = commentRepository.findAllByEventId(event.getId());
        return comments.stream().map(CommentMapper::mapToEventCommentDto).toList();
    }

    public List<CommentCompilationResponseDto> getComments(Compilation compilation) {
        List<Comment> comments = commentRepository.findAllByCompilationId(compilation.getId());
        return comments.stream().map(CommentMapper::mapToCompilationCommentDto).toList();
    }
}
