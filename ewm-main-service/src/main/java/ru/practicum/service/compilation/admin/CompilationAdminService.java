package ru.practicum.service.compilation.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.compilation.common.dto.CompilationRequestDto;
import ru.practicum.service.compilation.common.dto.CompilationResponseDto;
import ru.practicum.service.compilation.common.dto.UpdateCompilationRequestDto;
import ru.practicum.service.compilation.common.entity.Compilation;
import ru.practicum.service.compilation.common.entity.EventCompilation;
import ru.practicum.service.compilation.common.mapper.CompilationMapper;
import ru.practicum.service.compilation.common.repository.CompilationRepository;
import ru.practicum.service.compilation.common.repository.EventCompilationRepository;
import ru.practicum.service.event.common.EventService;
import ru.practicum.service.event.common.dto.EventShortResponseDto;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class CompilationAdminService {
    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final EventService eventService;

    public CompilationResponseDto addCompilation(CompilationRequestDto requestDto) {
        log.info("Пришел запрос от админа на создание новой подборки с title = {}", requestDto.getTitle());
        Compilation compilation = CompilationMapper.mapToCompilation(requestDto);
        compilation = compilationRepository.save(compilation);
        List<EventShortResponseDto> eventsList = new ArrayList<>();
        if (requestDto.getEvents() != null && !requestDto.getEvents().isEmpty()) {
            for (Long eventId : requestDto.getEvents()) {
                saveEventCompilation(eventId, eventsList, compilation);
            }
        }

        return CompilationMapper.mapToCompilationDto(compilation, eventsList);
    }

    public CompilationResponseDto updateCompilation(Long id, UpdateCompilationRequestDto requestDto) {
        log.info("Пришел запрос от админа на редактирование подборки с id = {}", id);
        Compilation compilation = getCompilation(id);
        compilation = CompilationMapper.updateMapToCompilation(requestDto, compilation);
        compilation = compilationRepository.save(compilation);
        List<EventShortResponseDto> eventsList = new ArrayList<>();
        if (requestDto.getEvents() != null && !requestDto.getEvents().isEmpty()) {
            for (Long eventId : requestDto.getEvents()) {
                if (eventCompilationRepository.findByEventIdAndCompilationId(eventId, id).isEmpty()) {
                    saveEventCompilation(eventId, eventsList, compilation);
                }
            }
        }

        return CompilationMapper.mapToCompilationDto(compilation, eventsList);
    }

    public void deleteCompilation(Long id) {
        log.info("Пришел запрос от админа на удаление подборки с id = {}", id);
        Compilation compilation = getCompilation(id);
        compilationRepository.delete(compilation);
    }

    private void saveEventCompilation(Long eventId, List<EventShortResponseDto> eventsList,
                                      Compilation compilation) {
        Event event = eventService.getEvent(eventId);
        eventsList.add(eventService.getEventShortDtoWithViews(event));
        EventCompilation eventCompilation = new EventCompilation(
                new EventCompilation.EventCompilationId(eventId, compilation.getId()), compilation, event);
        eventCompilationRepository.save(eventCompilation);
    }

    private Compilation getCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id " + id + " не найдена"));
    }
}
