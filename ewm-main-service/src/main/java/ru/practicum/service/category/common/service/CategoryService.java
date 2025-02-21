package ru.practicum.service.category.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.category.common.entity.Category;
import ru.practicum.service.category.common.repository.CategoryRepository;
import ru.practicum.service.exception.NotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена категория с id = " + id));
    }
}
