package ru.practicum.service.category.common.dto;

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
public class CategoryRequestDto {
    @Size(max = 50, message = "Некорректная длина названия категории")
    @NotBlank(message = "Название категории должно быть указано")
    private String name;
}
