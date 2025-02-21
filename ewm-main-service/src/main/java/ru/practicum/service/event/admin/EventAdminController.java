package ru.practicum.service.event.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.common.dto.EventResponseDto;
import ru.practicum.service.event.common.dto.UpdateEvenRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@AllArgsConstructor
@Validated
public class EventAdminController {
    private EventAdminService eventAdminService;

    @GetMapping
    public List<EventResponseDto> getAllEvents(@RequestParam(required = false) List<Long> users,
                                               @RequestParam(required = false) List<String> states,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @PositiveOrZero(message = "некорректное значение from")
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @Positive(message = "некорректное значение size")
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return eventAdminService.getAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventResponseDto updateEvent(@Positive(message = "некорректное значение eventId") @PathVariable Long eventId,
                                        @Valid @RequestBody UpdateEvenRequestDto requestDto) {
        return eventAdminService.updateEvent(eventId, requestDto);
    }
}
