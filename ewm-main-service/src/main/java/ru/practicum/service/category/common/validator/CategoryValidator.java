package ru.practicum.service.category.common.validator;

import ru.practicum.service.category.common.entity.Category;
import ru.practicum.service.exception.ConflictException;

import java.util.Optional;

public class CategoryValidator {

    public static void isCorrectName(Optional<Category> user) {
        if (user.isPresent()) {
            throw new ConflictException("Такое имя для категории уже используется");
        }
    }

    public static Boolean isCorrectName(Category category, Optional<Category> categoryOpt) {
        if (categoryOpt.isPresent()) {
            if (category.getName().equals(categoryOpt.get().getName())) {
                return false;
            } else {
                throw new ConflictException("Такое имя для категории уже используется");
            }
        }
        return true;
    }
}
