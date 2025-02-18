package ru.practicum.service.participation_request.priv;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.participation_request.priv.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@AllArgsConstructor
@Validated
public class ParticipationRequestController {
    private ParticipationRequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getAllUserRequests(
            @Positive(message = "некорректное значение userId") @PathVariable("userId") Long userId) {
        return requestService.getAllUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addNewRequest(
            @Positive(message = "некорректное значение userId") @PathVariable("userId") Long userId,
            @Validated @Positive(message = "некорректное значение eventId") @RequestParam Long eventId) {
        return requestService.addNewRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto canceledRequest(
            @Positive(message = "некорректное значение userId") @PathVariable("userId") Long userId,
            @Positive(message = "некорректное значение requestId") @PathVariable("requestId") Long requestId) {
        return requestService.canceledRequest(userId, requestId);
    }
}
