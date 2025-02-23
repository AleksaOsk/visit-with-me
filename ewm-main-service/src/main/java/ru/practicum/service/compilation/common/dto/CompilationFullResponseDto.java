package ru.practicum.service.compilation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.service.comment.dto.CommentCompilationResponseDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationFullResponseDto extends CompilationResponseDto {
    private List<CommentCompilationResponseDto> comments;
}
