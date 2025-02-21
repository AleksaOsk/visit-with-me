package ru.practicum.service.user.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Size(min = 2, max = 250, message = "Некорректная длина имени")
    @NotBlank(message = "Имя должно быть указано")
    private String name;
    @Size(min = 6, max = 254, message = "Некорректная длина email")
    @NotBlank(message = "Email должен быть указан")
    @Email(message = "Некорректное указание E-mail")
    private String email;
}
