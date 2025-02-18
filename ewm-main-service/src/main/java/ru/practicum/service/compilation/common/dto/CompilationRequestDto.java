package ru.practicum.service.compilation.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationRequestDto {
    private boolean pinned;
    @Size(min = 1, max = 50, message = "Некорректная длина title")
    @NotBlank(message = "title должно быть указано")
    private String title;
    private List<Long> events;
}
