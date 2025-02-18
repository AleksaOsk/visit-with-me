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
    protected String annotation;
    @Positive(message = "categoryId должно быть больше 0")
    protected Long categoryId;
    @Size(min = 20, max = 7000, message = "Некорректная длина description")
    protected String description;
    protected Location location;
    @Size(min = 3, max = 120, message = "Некорректная длина title")
    protected String title;
    protected Boolean paid;
    @Positive(message = "participantLimit должно быть больше 0")
    protected Integer participantLimit;
    protected Boolean requestModeration;
    private State stateAction;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;
}
