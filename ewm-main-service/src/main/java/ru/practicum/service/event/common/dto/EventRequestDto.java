package ru.practicum.service.event.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.service.event.common.entity.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {
    @Size(min = 20, max = 2000, message = "Некорректная длина annotation")
    @NotBlank(message = "annotation должно быть указано")
    private String annotation;

    @Positive(message = "category должно быть больше 0")
    @NotNull(message = "category должно быть указано")
    private Long category;

    @Size(min = 20, max = 7000, message = "Некорректная длина description")
    @NotBlank(message = "description должно быть указано")
    private String description;

    @NotNull(message = "eventDate должно быть указано")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "location должно быть указано")
    private Location location;

    @Size(min = 3, max = 120, message = "Некорректная длина title")
    @NotBlank(message = "title должно быть указано")
    private String title;

    private boolean paid;
    @PositiveOrZero(message = "participantLimit не должно быть меньше 0")
    private int participantLimit;
    private Boolean requestModeration;
}
