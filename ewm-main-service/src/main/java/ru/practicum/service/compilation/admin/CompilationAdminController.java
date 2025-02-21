package ru.practicum.service.compilation.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.compilation.common.dto.CompilationRequestDto;
import ru.practicum.service.compilation.common.dto.CompilationResponseDto;
import ru.practicum.service.compilation.common.dto.UpdateCompilationRequestDto;

@RestController
@RequestMapping(path = "/admin/compilations")
@AllArgsConstructor
@Validated
public class CompilationAdminController {
    private CompilationAdminService adminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto addCompilation(@RequestBody @Valid CompilationRequestDto requestDto) {
        return adminService.addCompilation(requestDto);
    }

    @PatchMapping("/{compId}")
    public CompilationResponseDto updateCompilation(@Positive(message = "некорректное значение compId")
                                                    @PathVariable("compId") Long id,
                                                    @RequestBody @Valid UpdateCompilationRequestDto requestDto) {
        return adminService.updateCompilation(id, requestDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive(message = "некорректное значение compId") @PathVariable("compId") Long id) {
        adminService.deleteCompilation(id);
    }
}
