package ru.practicum.service.user.admin.valid;

import ru.practicum.service.exception.ConflictException;
import ru.practicum.service.user.admin.entity.User;

import java.util.Optional;

public class UserValidator {
    public static void checkEmail(Optional<User> user) {
        if (user.isPresent()) {
            throw new ConflictException("Имейл уже используется");
        }
    }
}
