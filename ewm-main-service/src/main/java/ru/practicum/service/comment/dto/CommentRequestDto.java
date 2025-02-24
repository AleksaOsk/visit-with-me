package ru.practicum.service.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @Size(min = 1, max = 2000, message = "Некорректная длина text")
    @NotBlank(message = "text должен быть указан")
    private String text;
}
