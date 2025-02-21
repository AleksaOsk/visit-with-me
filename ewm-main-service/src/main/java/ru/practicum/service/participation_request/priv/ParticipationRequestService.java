package ru.practicum.service.participation_request.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.event.common.EventService;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.event.common.entity.State;
import ru.practicum.service.exception.ConflictException;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.participation_request.priv.dto.ParticipationRequestDto;
import ru.practicum.service.participation_request.priv.entity.ParticipationRequest;
import ru.practicum.service.participation_request.priv.entity.Status;
import ru.practicum.service.user.admin.entity.User;
import ru.practicum.service.user.admin.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ParticipationRequestService {
    private ParticipationRequestRepository requestRepository;
    private UserServiceImpl userService;
    private EventService eventService;

    public List<ParticipationRequestDto> getAllUserRequests(Long userId) {
        log.info("Пришел запрос на получение всех запросов к чужим мероприятиям от пользователя с id = {}", userId);
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(RequestMapper::mapToParRequestDto)
                .toList();
    }

    public ParticipationRequestDto addNewRequest(Long userId, Long eventId) {
        log.info("Пришел запрос на добавление нового запроса к мероприятию с id = {} от пользователя с id = {}",
                eventId, userId);
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new ConflictException("Запрос для участия в данном мероприятии был отправлен ранее");
        }

        Event event = eventService.getEvent(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Нельзя отправить заявку на участие в неопубликованном мероприятии");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя отправить заявку на участие в своем же мероприятии");
        }

        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ConflictException("Лимит для участников в этом мероприятии исчерпан");
        }

        User user = userService.getUser(userId);
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(user);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(Status.CONFIRMED);
            eventService.saveNewConfirmedRequest(event);
        } else {
            participationRequest.setStatus(Status.PENDING);
        }
        participationRequest = requestRepository.save(participationRequest);

        return RequestMapper.mapToParRequestDto(participationRequest);
    }

    public ParticipationRequestDto canceledRequest(Long userId, Long requestId) {
        log.info("Пришел запрос на отмену своего запроса с id = {} от пользователя с id = {}", requestId, userId);
        ParticipationRequest participationRequest = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Запроса для отмены не найдено"));
        participationRequest.setStatus(Status.CANCELED);
        participationRequest = requestRepository.save(participationRequest);
        return RequestMapper.mapToParRequestDto(participationRequest);
    }

    //методы для EventPrivateService
    public List<ParticipationRequest> getListRequestsByEvent(Long eventId) {
        return requestRepository.findAllByEventId(eventId);
    }

    public List<ParticipationRequest> getRequests(Long eventId) {
        return requestRepository.findAllByEventId(eventId);
    }

    public void updateStatusByInitiator(ParticipationRequest request) {
        requestRepository.save(request);
    }
}
