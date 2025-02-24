package ru.practicum.service.event.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.category.common.entity.Category;
import ru.practicum.service.category.common.service.CategoryService;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.comment.dto.CommentEventResponseDto;
import ru.practicum.service.event.common.EventMapper;
import ru.practicum.service.event.common.EventRepository;
import ru.practicum.service.event.common.dto.EventCommentsResponseDto;
import ru.practicum.service.event.common.dto.EventRequestDto;
import ru.practicum.service.event.common.dto.EventResponseDto;
import ru.practicum.service.event.common.dto.UpdateEvenRequestDto;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.event.common.entity.State;
import ru.practicum.service.event.common.statistic.StatisticService;
import ru.practicum.service.event.common.validator.EventValidator;
import ru.practicum.service.exception.ConflictException;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.participation_request.priv.ParticipationRequestService;
import ru.practicum.service.participation_request.priv.RequestMapper;
import ru.practicum.service.participation_request.priv.dto.EventRequestStatusUpdateRequest;
import ru.practicum.service.participation_request.priv.dto.EventRequestStatusUpdateResult;
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
class EventPrivateService {
    private EventRepository eventRepository;
    private UserServiceImpl userService;
    private CategoryService categoryService;
    private StatisticService statisticService;
    private ParticipationRequestService participationRequestService;
    private CommentService commentService;

    public EventResponseDto addNewEvent(Long userId, EventRequestDto eventRequestDto) {
        log.info("Пришел запрос на создание нового мероприятия от пользователя c id = {}", userId);
        User user = userService.getUser(userId);
        Category category = categoryService.getCategory(eventRequestDto.getCategory());
        EventValidator.checkTimeInFuture(LocalDateTime.now(), eventRequestDto.getEventDate());
        if (eventRequestDto.getRequestModeration() == null) {
            eventRequestDto.setRequestModeration(true);
        }
        Event event = EventMapper.mapToEvent(eventRequestDto, category, user, LocalDateTime.now());
        event = eventRepository.save(event);

        return EventMapper.mapToEventDto(event);
    }

    public List<EventResponseDto> getUserEvents(Long userId, int from, int size) {
        log.info("Пришел запрос на получение всех мероприятий созданных пользователем c id = {}", userId);
        List<Event> eventResponseDtoList = eventRepository.findAllByInitiatorId(userId, size, from);
        return statisticService.getViewListFullDto(eventResponseDtoList);
    }

    public EventCommentsResponseDto getUserEvent(Long userId, Long eventId) {
        log.info("Пришел запрос на получение мероприятия созданного пользователем c id = {}", userId);
        Event event = getEventByIdAndInitiatorId(userId, eventId);
        long views = statisticService.getView(event);
        List<CommentEventResponseDto> comments = commentService.getComments(event);
        return EventMapper.mapToEventDto(event, views, comments);
    }

    public EventCommentsResponseDto updateEvent(Long userId, Long eventId, UpdateEvenRequestDto requestDto) {
        log.info("Пришел запрос на обновление мероприятия с id = {} от пользователя c id = {}", userId, eventId);
        Event event = getEventByIdAndInitiatorId(userId, eventId);
        if (requestDto.getEventDate() != null) {
            EventValidator.checkTimeInFuture(event.getCreatedOn(), requestDto.getEventDate());
        }
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("изменить можно только отмененные события или в состоянии ожидания модерации");
        } else if (requestDto.getStateAction() == State.SEND_TO_REVIEW) {
            requestDto.setStateAction(State.PENDING);
        } else if (requestDto.getStateAction() == State.CANCEL_REVIEW) {
            requestDto.setStateAction(State.CANCELED);
        }
        if (requestDto.getCategoryId() != null) {
            try {
                Category category = categoryService.getCategory(requestDto.getCategoryId());
                event.setCategory(category);
            } catch (NotFoundException ignored) {
            }
        }
        event = EventMapper.updateMapToEvent(requestDto, event);
        event = eventRepository.save(event);
        long views = statisticService.getView(event);
        List<CommentEventResponseDto> comments = commentService.getComments(event);
        return EventMapper.mapToEventDto(event, views, comments);
    }

    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("Пришел запрос на получение всех заявок к мероприятию с id = {}", eventId);
        getEventByIdAndInitiatorId(userId, eventId);
        return participationRequestService.getListRequestsByEvent(eventId)
                .stream()
                .map(RequestMapper::mapToParRequestDto)
                .toList();
    }

    public EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest request) {
        log.info("Пришел запрос на изменение заявок к мероприятию с id = {}", eventId);
        Event event = getEventByIdAndInitiatorId(userId, eventId);
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictException("У данного мероприятия исчерпан лимит принятых заявок");
        }
        List<ParticipationRequest> requests = participationRequestService.getRequests(eventId);
        int confReq = event.getConfirmedRequests();
        for (ParticipationRequest req : requests) {
            if (request.getRequestIds().contains(req.getId())) {
                if (req.getStatus() == Status.CONFIRMED && request.getStatus() == Status.REJECTED) {
                    throw new ConflictException("Нельзя отменить ранее принятую заявку");
                } else if (confReq < event.getParticipantLimit()) {
                    req.setStatus(request.getStatus());
                    participationRequestService.updateStatusByInitiator(req);
                    confReq++;
                }
            }
        }
        event.setConfirmedRequests(confReq);
        eventRepository.save(event);

        List<ParticipationRequestDto> confirmedRequests = requests.stream()
                .filter(parRequest -> parRequest.getStatus() == Status.CONFIRMED)
                .map(RequestMapper::mapToParRequestDto)
                .toList();
        List<ParticipationRequestDto> rejectedRequests = requests.stream()
                .filter(parRequest -> parRequest.getStatus() == Status.REJECTED)
                .map(RequestMapper::mapToParRequestDto)
                .toList();


        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private Event getEventByIdAndInitiatorId(Long userId, Long eventId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено с id = " + eventId +
                                                         " или вы не являетесь его владельцем"));
    }
}
