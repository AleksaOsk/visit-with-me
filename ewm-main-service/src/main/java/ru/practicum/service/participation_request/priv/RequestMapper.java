package ru.practicum.service.participation_request.priv;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.participation_request.priv.dto.ParticipationRequestDto;
import ru.practicum.service.participation_request.priv.entity.ParticipationRequest;
import ru.practicum.service.user.admin.entity.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestMapper {
    public static ParticipationRequest mapToParRequest(ParticipationRequestDto requestDto, User user, Event event) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setCreated(requestDto.getCreated());
        participationRequest.setStatus(requestDto.getStatus());
        participationRequest.setRequester(user);
        participationRequest.setEvent(event);
        return participationRequest;
    }

    public static ParticipationRequestDto mapToParRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setId(participationRequest.getId());
        requestDto.setCreated(participationRequest.getCreated());
        requestDto.setStatus(participationRequest.getStatus());
        requestDto.setRequester(participationRequest.getRequester().getId());
        requestDto.setEvent(participationRequest.getEvent().getId());
        return requestDto;
    }
}
