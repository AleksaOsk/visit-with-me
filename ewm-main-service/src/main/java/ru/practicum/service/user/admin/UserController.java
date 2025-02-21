package ru.practicum.service.user.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.user.admin.dto.UserRequestDto;
import ru.practicum.service.user.admin.dto.UserResponseDto;
import ru.practicum.service.user.admin.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@AllArgsConstructor
@Validated
public class UserController {
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addNewUser(@RequestBody @Valid UserRequestDto requestDto) {
        return userService.addNewUser(requestDto);
    }

    @GetMapping
    public List<UserResponseDto> getUsers(
            @PositiveOrZero(message = "некорректное значение from") @RequestParam(defaultValue = "0") int from,
            @Positive(message = "некорректное значение size") @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<Integer> ids) {
        return userService.getUsers(from, size, ids);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive(message = "некорректное значение userId") @PathVariable("userId") Long id) {
        userService.deleteUser(id);
    }
}
