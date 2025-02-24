package ru.practicum.service.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comment.dto.CommentCompilationResponseDto;
import ru.practicum.service.comment.dto.CommentEventResponseDto;
import ru.practicum.service.comment.dto.CommentRequestDto;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/comments")
public class CommentController {
    private CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentEventResponseDto addEventComment(@Positive(message = "userId не может быть отрицательным")
                                                   @PathVariable("userId") Long userId,
                                                   @Positive(message = "eventId не может быть отрицательным")
                                                   @PathVariable("eventId") Long eventId,
                                                   @Valid @RequestBody CommentRequestDto requestDto) {
        return commentService.addEventComment(userId, eventId, requestDto);
    }

    @PostMapping("/compilation/{compId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentCompilationResponseDto addCompComment(@Positive(message = "userId не может быть отрицательным")
                                                        @PathVariable("userId") Long userId,
                                                        @Positive(message = "compId не может быть отрицательным")
                                                        @PathVariable("compId") Long compId,
                                                        @Valid @RequestBody CommentRequestDto requestDto) {
        return commentService.addCompComment(userId, compId, requestDto);
    }

    @PatchMapping("/{commentId}")
    public Object updateComment(@Positive(message = "userId не может быть отрицательным")
                                @PathVariable("userId") Long userId,
                                @Positive(message = "commentId не может быть отрицательным")
                                @PathVariable("commentId") Long commentId,
                                @Valid @RequestBody CommentRequestDto requestDto) {
        return commentService.updateComment(userId, commentId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Positive(message = "userId не может быть отрицательным")
                              @PathVariable("userId") Long userId,
                              @Positive(message = "commentId не может быть отрицательным")
                              @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(userId, commentId);
    }
}
