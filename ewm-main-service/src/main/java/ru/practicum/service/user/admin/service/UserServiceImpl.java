package ru.practicum.service.user.admin.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.exception.ValidationException;
import ru.practicum.service.user.admin.UserMapper;
import ru.practicum.service.user.admin.UserRepository;
import ru.practicum.service.user.admin.dto.UserRequestDto;
import ru.practicum.service.user.admin.dto.UserResponseDto;
import ru.practicum.service.user.admin.entity.User;
import ru.practicum.service.user.admin.valid.UserValidator;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public UserResponseDto addNewUser(UserRequestDto requestDto) {
        log.info("Пришел запрос на создание нового пользователя с email = {}", requestDto.getEmail());
        String email = requestDto.getEmail();
        UserValidator.checkEmail(userRepository.findByEmailIgnoreCase(email));
        User user = UserMapper.mapToUser(requestDto);
        user = userRepository.save(user);

        return UserMapper.mapToUserResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getUsers(int from, int size, List<Integer> ids) {
        log.info("Пришел запрос на получение пользователей с from = {}, size = {}, ids = {}", from, size, ids);
        List<User> list;
        if (ids == null || ids.isEmpty()) {
            list = userRepository.findAllWithOutListOfIds(size, from);
        } else {
            list = userRepository.findAllWithListOfIds(ids, size, from);
        }
        return list.stream()
                .map(UserMapper::mapToUserResponseDto)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Пришел запрос на удаление пользователя с id = {}", id);
        userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Пользователь с id " + id + " не найден"));
    }
}
