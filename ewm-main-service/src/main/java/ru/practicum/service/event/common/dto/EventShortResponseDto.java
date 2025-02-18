package ru.practicum.service.event.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.service.category.common.dto.CategoryResponseDto;
import ru.practicum.service.user.admin.dto.UserShortResponseDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortResponseDto {
    private Long id;
    private String annotation;
    private String title;
    private int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private boolean paid;
    private UserShortResponseDto initiator;
    private CategoryResponseDto category;
    private long views;
}
