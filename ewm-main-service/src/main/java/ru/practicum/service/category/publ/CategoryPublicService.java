package ru.practicum.service.category.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.category.common.dto.CategoryResponseDto;
import ru.practicum.service.category.common.mapper.CategoryMapper;
import ru.practicum.service.category.common.repository.CategoryRepository;
import ru.practicum.service.category.common.service.CategoryService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class CategoryPublicService {
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public List<CategoryResponseDto> getCategories(int from, int size) {
        log.info("Пришел запрос на получение категорий с from = {}, size = {}", from, size);
        return categoryRepository.findAllWithLimitAndOffset(size, from)
                .stream()
                .map(CategoryMapper::mapToCategoryDto)
                .toList();
    }

    public CategoryResponseDto getCategoryDto(Long id) {
        log.info("Пришел запрос на получение категории с id = {}", id);
        return CategoryMapper.mapToCategoryDto(categoryService.getCategory(id));
    }
}
