package ru.practicum.service.compilation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.service.event.common.dto.EventShortResponseDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDto {
    private long id;
    private boolean pinned;
    private String title;
    private List<EventShortResponseDto> events;
}
