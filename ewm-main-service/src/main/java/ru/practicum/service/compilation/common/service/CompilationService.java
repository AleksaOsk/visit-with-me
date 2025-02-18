package ru.practicum.service.compilation.common.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.compilation.common.dto.CompilationResponseDto;
import ru.practicum.service.compilation.common.entity.Compilation;
import ru.practicum.service.compilation.common.mapper.CompilationMapper;
import ru.practicum.service.compilation.common.repository.CompilationRepository;
import ru.practicum.service.compilation.common.repository.EventCompilationRepository;
import ru.practicum.service.event.common.EventService;
import ru.practicum.service.event.common.dto.EventShortResponseDto;
import ru.practicum.service.exception.NotFoundException;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CompilationService {
    protected CompilationRepository compilationRepository;
    protected EventCompilationRepository eventCompilationRepository;
    protected EventService eventService;

    protected Compilation getCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id " + id + " не найдена"));
    }

    protected List<Long> getEventIdsByCompilationId(Long compilationId) {
        return eventCompilationRepository.findAllEventIdByCompilationId(compilationId);
    }

    protected CompilationResponseDto getCompilationResponseDto(Compilation compilation) {
        List<Long> eventIds = getEventIdsByCompilationId(compilation.getId());
        List<EventShortResponseDto> eventShortResponseDtos = eventService.getEventShortDtoListWithViews(eventIds);
        return CompilationMapper.mapToCompilationDto(compilation, eventShortResponseDtos);
    }
}
