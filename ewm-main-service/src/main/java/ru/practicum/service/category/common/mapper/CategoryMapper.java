package ru.practicum.service.category.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.service.category.common.dto.CategoryRequestDto;
import ru.practicum.service.category.common.dto.CategoryResponseDto;
import ru.practicum.service.category.common.entity.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {
    public static Category mapToCategory(CategoryRequestDto request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public static CategoryResponseDto mapToCategoryDto(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        return categoryResponseDto;
    }
}
