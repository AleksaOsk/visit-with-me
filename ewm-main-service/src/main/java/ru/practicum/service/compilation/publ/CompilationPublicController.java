package ru.practicum.service.compilation.publ;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.compilation.common.dto.CompilationResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@AllArgsConstructor
@Validated
public class CompilationPublicController {
    private CompilationPublicService publicService;

    @GetMapping
    public List<CompilationResponseDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero(message = "некорректное значение from")
            @RequestParam(required = false, defaultValue = "0") int from,
            @Positive(message = "некорректное значение size")
            @RequestParam(required = false, defaultValue = "10") int size) {

        return publicService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationResponseDto getCompilationById(@Positive(message = "некорректное значение compId")
                                                     @PathVariable Long compId) {
        return publicService.getCompilationById(compId);
    }
}
