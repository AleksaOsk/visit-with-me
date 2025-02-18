package ru.practicum.service.user.admin.service;

import ru.practicum.service.user.admin.dto.UserRequestDto;
import ru.practicum.service.user.admin.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto addNewUser(UserRequestDto requestDto);

    List<UserResponseDto> getUsers(int from, int size, List<Integer> ids);

    void deleteUser(Long id);
}
