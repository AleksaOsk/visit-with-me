package ru.practicum.service.event.publ;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.common.dto.EventResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
@AllArgsConstructor
@Validated
public class EventPublicController {
    private EventPublicService eventPublicService;

    @GetMapping
    public List<EventResponseDto> getPublicEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) Sort sort,
            @PositiveOrZero(message = "некорректное значение from")
            @RequestParam(required = false, defaultValue = "0") int from,
            @Positive(message = "некорректное значение size")
            @RequestParam(required = false, defaultValue = "10") int size,
            HttpServletRequest httpServletRequest) {

        return eventPublicService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, httpServletRequest);
    }

    @GetMapping("/{eventId}")
    public EventResponseDto getEventById(@Positive(message = "некорректное значение eventId")
                                         @PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        return eventPublicService.getEventById(eventId, httpServletRequest);
    }
}
