package ru.practicum.service.compilation.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.compilation.common.entity.Compilation;
import ru.practicum.service.compilation.common.repository.CompilationRepository;
import ru.practicum.service.exception.NotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;

    public Compilation getCompilationById(Long compId) {
        log.info("Пришел запрос на получение подборки с id = {}", compId);
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id " + compId + " не найдена"));
    }
}
