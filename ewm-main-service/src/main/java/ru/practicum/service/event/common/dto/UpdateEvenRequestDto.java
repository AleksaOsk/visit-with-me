package ru.practicum.service.event.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.service.event.common.entity.Location;
import ru.practicum.service.event.common.entity.State;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEvenRequestDto {
    @Size(min = 20, max = 2000, message = "Некорректная длина annotation")
    private String annotation;
    @Positive(message = "categoryId должно быть больше 0")
    private Long categoryId;
    @Size(min = 20, max = 7000, message = "Некорректная длина description")
    private String description;
    private Location location;
    @Size(min = 3, max = 120, message = "Некорректная длина title")
    private String title;
    private Boolean paid;
    @Positive(message = "participantLimit должно быть больше 0")
    private Integer participantLimit;
    private Boolean requestModeration;
    private State stateAction;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
}
