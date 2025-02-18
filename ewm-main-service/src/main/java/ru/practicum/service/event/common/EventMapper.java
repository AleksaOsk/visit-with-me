package ru.practicum.service.event.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.service.category.common.entity.Category;
import ru.practicum.service.category.common.mapper.CategoryMapper;
import ru.practicum.service.event.common.dto.EventRequestDto;
import ru.practicum.service.event.common.dto.EventResponseDto;
import ru.practicum.service.event.common.dto.EventShortResponseDto;
import ru.practicum.service.event.common.dto.UpdateEvenRequestDto;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.event.common.entity.State;
import ru.practicum.service.user.admin.UserMapper;
import ru.practicum.service.user.admin.entity.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {
    public static Event mapToEvent(EventRequestDto request, Category category, User user, LocalDateTime dateTime) {
        Event event = new Event();
        event.setAnnotation(request.getAnnotation());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setLocation(request.getLocation());
        event.setTitle(request.getTitle());
        event.setPaid(request.isPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setRequestModeration(request.getRequestModeration());
        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(dateTime);
        event.setState(State.PENDING);
        return event;
    }

    public static EventResponseDto mapToEventDto(Event event) {
        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(event.getId());
        eventResponseDto.setAnnotation(event.getAnnotation());
        eventResponseDto.setTitle(event.getTitle());
        eventResponseDto.setConfirmedRequests(event.getConfirmedRequests());
        eventResponseDto.setCreatedOn(event.getCreatedOn());
        eventResponseDto.setDescription(event.getDescription());
        eventResponseDto.setEventDate(event.getEventDate());
        eventResponseDto.setPublishedOn(event.getPublishedOn());
        eventResponseDto.setState(event.getState());
        eventResponseDto.setRequestModeration(event.isRequestModeration());
        eventResponseDto.setParticipantLimit(event.getParticipantLimit());
        eventResponseDto.setPaid(event.isPaid());
        eventResponseDto.setLocation(event.getLocation());
        eventResponseDto.setInitiator(UserMapper.mapToUserShortResponseDto(event.getInitiator()));
        eventResponseDto.setCategory(CategoryMapper.mapToCategoryDto(event.getCategory()));
        return eventResponseDto;
    }

    public static EventResponseDto mapToEventDto(Event event, long views) {
        EventResponseDto eventResponseDto = mapToEventDto(event);
        eventResponseDto.setViews(views);
        return eventResponseDto;
    }

    public static EventShortResponseDto mapToEventShortDto(Event event, long views) {
        EventShortResponseDto eventResponseDto = mapToEventShortDto(event);
        eventResponseDto.setViews(views);
        return eventResponseDto;
    }

    public static EventShortResponseDto mapToEventShortDto(Event event) {
        EventShortResponseDto eventResponseDto = new EventShortResponseDto();
        eventResponseDto.setId(event.getId());
        eventResponseDto.setAnnotation(event.getAnnotation());
        eventResponseDto.setTitle(event.getTitle());
        eventResponseDto.setConfirmedRequests(event.getConfirmedRequests());
        eventResponseDto.setEventDate(event.getEventDate());
        eventResponseDto.setPaid(event.isPaid());
        eventResponseDto.setInitiator(UserMapper.mapToUserShortResponseDto(event.getInitiator()));
        return eventResponseDto;
    }

    public static Event updateMapToEvent(UpdateEvenRequestDto request, Event event) {
        if (request.getAnnotation() != null && !request.getAnnotation().isBlank()) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            event.setDescription(request.getDescription());
        }
        if (request.getLocation() != null && request.getLocation().getLat() != null
            && request.getLocation().getLon() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            event.setTitle(request.getTitle());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getStateAction() != null) {
            event.setState(request.getStateAction());
        }
        return event;
    }
}
