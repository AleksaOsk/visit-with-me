package ru.practicum.service.compilation.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.service.compilation.common.dto.CompilationRequestDto;
import ru.practicum.service.compilation.common.dto.CompilationResponseDto;
import ru.practicum.service.compilation.common.dto.UpdateCompilationRequestDto;
import ru.practicum.service.compilation.common.entity.Compilation;
import ru.practicum.service.event.common.dto.EventShortResponseDto;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationMapper {
    public static Compilation mapToCompilation(CompilationRequestDto request) {
        Compilation compilation = new Compilation();
        compilation.setTitle(request.getTitle());
        compilation.setPinned(request.isPinned());
        return compilation;
    }

    public static Compilation updateMapToCompilation(UpdateCompilationRequestDto request, Compilation compilation) {
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            compilation.setTitle(request.getTitle());
        }
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        return compilation;
    }

    public static CompilationResponseDto mapToCompilationDto(Compilation compil, List<EventShortResponseDto> events) {
        CompilationResponseDto responseDto = new CompilationResponseDto();
        responseDto.setId(compil.getId());
        responseDto.setTitle(compil.getTitle());
        responseDto.setPinned(compil.isPinned());
        responseDto.setEvents(events);
        return responseDto;
    }
}
