package ru.practicum.service.category.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.category.common.dto.CategoryRequestDto;
import ru.practicum.service.category.common.dto.CategoryResponseDto;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private CategoryAdminService categoryAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto add(@RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryAdminService.addNewCategory(requestDto);
    }

    @PatchMapping("/{catId}")
    public CategoryResponseDto update(@Positive(message = "некорректное значение catId") @PathVariable("catId") Long id,
                                      @RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryAdminService.updateCategory(id, requestDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive(message = "некорректное значение catId") @PathVariable("catId") Long id) {
        categoryAdminService.deleteCategory(id);
    }
}
