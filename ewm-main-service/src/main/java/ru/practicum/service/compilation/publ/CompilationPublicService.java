package ru.practicum.service.compilation.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.compilation.common.CompilationService;
import ru.practicum.service.compilation.common.dto.CompilationFullResponseDto;
import ru.practicum.service.compilation.common.dto.CompilationResponseDto;
import ru.practicum.service.compilation.common.entity.Compilation;
import ru.practicum.service.compilation.common.mapper.CompilationMapper;
import ru.practicum.service.compilation.common.repository.CompilationRepository;
import ru.practicum.service.compilation.common.repository.EventCompilationRepository;
import ru.practicum.service.event.common.EventService;
import ru.practicum.service.event.common.dto.EventShortResponseDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class CompilationPublicService {
    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final EventService eventService;
    private final CommentService commentService;
    private final CompilationService compilationService;

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
            CompilationResponseDto respDto = CompilationMapper.mapToCompilationDto(compilation,
                    getEventList(compilation));
            compilationResponseDtos.add(respDto);
        }

        return compilationResponseDtos;
    }

    public CompilationFullResponseDto getCompilationDto(Long compId) {
        log.info("Пришел запрос на получение подборки с id = {}", compId);
        Compilation compilation = compilationService.getCompilationById(compId);
        return CompilationMapper.mapToCompilationDto(compilation,
                getEventList(compilation), commentService.getComments(compilation));
    }

    private List<EventShortResponseDto> getEventList(Compilation compilation) {
        List<Long> eventIds = eventCompilationRepository.findAllEventIdByCompilationId(compilation.getId());
        return eventService.getEventShortDtoListWithViews(eventIds);
    }
}
