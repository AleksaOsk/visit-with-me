package ru.practicum.service.compilation.publ;

import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class CompilationPublicService {
    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final EventService eventService;

    public List<CompilationResponseDto> getCompilations(Boolean pinned, int from, int size) {
        log.info("Пришел запрос на получение подборок с pinned = {}, from = {}, size = {}", pinned, from, size);
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByParamsWithPinned(pinned, size, from);
        } else {
            compilations = compilationRepository.findAllByParams(size, from);
        }
        List<CompilationResponseDto> compilationResponseDtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            CompilationResponseDto respDto = getCompilationResponseDto(compilation);
            compilationResponseDtos.add(respDto);
        }

        return compilationResponseDtos;
    }

    public CompilationResponseDto getCompilationById(Long compId) {
        log.info("Пришел запрос на получение подборки с id = {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id " + compId + " не найдена"));
        return getCompilationResponseDto(compilation);
    }

    private CompilationResponseDto getCompilationResponseDto(Compilation compilation) {
        List<Long> eventIds = eventCompilationRepository.findAllEventIdByCompilationId(compilation.getId());
        List<EventShortResponseDto> eventShortResponseDtos = eventService.getEventShortDtoListWithViews(eventIds);
        return CompilationMapper.mapToCompilationDto(compilation, eventShortResponseDtos);
    }
}
