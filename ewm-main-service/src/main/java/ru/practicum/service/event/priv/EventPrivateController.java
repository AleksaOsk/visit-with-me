package ru.practicum.service.event.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.common.dto.EventCommentsResponseDto;
import ru.practicum.service.event.common.dto.EventRequestDto;
import ru.practicum.service.event.common.dto.EventResponseDto;
import ru.practicum.service.event.common.dto.UpdateEvenRequestDto;
import ru.practicum.service.participation_request.priv.dto.EventRequestStatusUpdateRequest;
import ru.practicum.service.participation_request.priv.dto.EventRequestStatusUpdateResult;
import ru.practicum.service.participation_request.priv.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@AllArgsConstructor
@Validated
public class EventPrivateController {
    private EventPrivateService eventPrivateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto addNewEvent(
            @Positive(message = "некорректное значение userId") @PathVariable("userId") Long userId,
            @Valid @RequestBody EventRequestDto eventRequestDto) {
        return eventPrivateService.addNewEvent(userId, eventRequestDto);
    }

    @GetMapping
    public List<EventResponseDto> getUserEvents(
            @Positive(message = "некорректное значение userId") @PathVariable("userId") Long userId,
            @PositiveOrZero(message = "некорректное значение from") @RequestParam(defaultValue = "0") int from,
            @Positive(message = "некорректное значение size") @RequestParam(defaultValue = "10") int size) {
        return eventPrivateService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventCommentsResponseDto getUserEvent(
            @Positive(message = "некорректное значение userId") @PathVariable("userId") Long userId,
            @Positive(message = "некорректное значение eventId") @PathVariable("eventId") Long eventId) {
        return eventPrivateService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventCommentsResponseDto updateEvent(
            @Positive(message = "некорректное значение userId") @PathVariable("userId") Long userId,
            @Positive(message = "некорректное значение eventId") @PathVariable("eventId") Long eventId,
            @Valid @RequestBody UpdateEvenRequestDto requestDto) {
        return eventPrivateService.updateEvent(userId, eventId, requestDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId) {
        return eventPrivateService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequests(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest requestDto) {
        return eventPrivateService.updateEventRequests(userId, eventId, requestDto);
    }
}
