package ru.practicum.service.event.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.service.comment.dto.CommentEventResponseDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCommentsResponseDto extends EventResponseDto {
    private List<CommentEventResponseDto> comments;
}
