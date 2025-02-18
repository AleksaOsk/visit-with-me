package ru.practicum.service.category.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.category.common.dto.CategoryRequestDto;
import ru.practicum.service.category.common.dto.CategoryResponseDto;
import ru.practicum.service.category.common.entity.Category;
import ru.practicum.service.category.common.mapper.CategoryMapper;
import ru.practicum.service.category.common.repository.CategoryRepository;
import ru.practicum.service.category.common.service.CategoryService;
import ru.practicum.service.category.common.validator.CategoryValidator;
import ru.practicum.service.event.common.EventService;
import ru.practicum.service.exception.ConflictException;

@Service
@Slf4j
class CategoryAdminService extends CategoryService {
    private final EventService eventService;

    public CategoryAdminService(CategoryRepository categoryRepository, EventService eventService) {
        super(categoryRepository);
        this.eventService = eventService;
    }

    public CategoryResponseDto addNewCategory(CategoryRequestDto requestDto) {
        log.info("Пришел запрос на создание новой категории = '{}'", requestDto.getName());
        String name = requestDto.getName();
        CategoryValidator.isCorrectName(categoryRepository.findByNameIgnoreCase(name));
        Category category = CategoryMapper.mapToCategory(requestDto);
        category = categoryRepository.save(category);

        return CategoryMapper.mapToCategoryDto(category);
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        log.info("Пришел запрос на обновление категории с id = {} и name = {}", id, requestDto.getName());
        String name = requestDto.getName();
        Category category = getCategory(id);
        if (CategoryValidator.isCorrectName(category, categoryRepository.findByNameIgnoreCase(name))) {
            category.setName(name);
            category = categoryRepository.save(category);
        }
        return CategoryMapper.mapToCategoryDto(category);
    }

    public void deleteCategory(Long id) {
        log.info("Пришел запрос на удаление категории с id = {}", id);
        if (eventService.getEventByCategoryId(id).isPresent()) {
            throw new ConflictException("Удалить категорию с id " + id + " невозможно, существуют " +
                                        "мероприятия с таким categoryID");
        }
        categoryRepository.deleteById(id);
    }
}
