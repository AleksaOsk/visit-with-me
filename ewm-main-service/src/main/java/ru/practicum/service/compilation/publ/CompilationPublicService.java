package ru.practicum.service.compilation.publ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.service.compilation.common.dto.CompilationResponseDto;
import ru.practicum.service.compilation.common.entity.Compilation;
import ru.practicum.service.compilation.common.repository.CompilationRepository;
import ru.practicum.service.compilation.common.repository.EventCompilationRepository;
import ru.practicum.service.compilation.common.service.CompilationService;
import ru.practicum.service.event.common.EventService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
class CompilationPublicService extends CompilationService {
    @Autowired
    public CompilationPublicService(CompilationRepository compilationRepository,
                                    EventCompilationRepository eventCompilationRepository, EventService eventService) {
        super(compilationRepository, eventCompilationRepository, eventService);
    }

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
        Compilation compilation = getCompilation(compId);
        return getCompilationResponseDto(compilation);
    }
}
