package ru.practicum.service.category.publ;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.category.common.dto.CategoryResponseDto;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(path = "/categories")
public class CategoryPublicController {
    private CategoryPublicService categoryPublicService;

    @GetMapping
    public List<CategoryResponseDto> getCategories(
            @PositiveOrZero(message = "некорректное значение from") @RequestParam(defaultValue = "0") int from,
            @Positive(message = "некорректное значение size") @RequestParam(defaultValue = "10") int size) {
        return categoryPublicService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryResponseDto getCategory(
            @Positive(message = "некорректное значение catId") @PathVariable("catId") Long id) {
        return categoryPublicService.getCategoryDto(id);
    }
}
