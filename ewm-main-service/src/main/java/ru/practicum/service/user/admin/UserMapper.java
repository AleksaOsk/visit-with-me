package ru.practicum.service.user.admin;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.service.user.admin.dto.UserRequestDto;
import ru.practicum.service.user.admin.dto.UserResponseDto;
import ru.practicum.service.user.admin.dto.UserShortResponseDto;
import ru.practicum.service.user.admin.entity.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User mapToUser(UserRequestDto request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        return user;
    }

    public static UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setName(user.getName());
        return userResponseDto;
    }

    public static UserShortResponseDto mapToUserShortResponseDto(User user) {
        UserShortResponseDto userShortResponseDto = new UserShortResponseDto();
        userShortResponseDto.setId(user.getId());
        userShortResponseDto.setName(user.getName());
        return userShortResponseDto;
    }
}
