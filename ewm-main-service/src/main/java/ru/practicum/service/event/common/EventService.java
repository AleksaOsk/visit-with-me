package ru.practicum.service.event.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.event.common.dto.EventShortResponseDto;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.event.common.statistic.StatisticService;
import ru.practicum.service.exception.ValidationException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {
    protected EventRepository eventRepository;
    protected StatisticService statisticService;

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Событие с id " + id + " не найден"));
    }

    public EventShortResponseDto getEventShortDtoWithViews(Event event) {
        long views = statisticService.getView(event);
        return EventMapper.mapToEventShortDto(event, views);
    }

    public Optional<Event> getEventByCategoryId(Long categoryId) {
        return eventRepository.findByCategoryId(categoryId);
    }

    public void saveNewConfirmedRequest(Event event) {
        int pastCount = event.getConfirmedRequests();
        event.setConfirmedRequests(pastCount + 1);
        eventRepository.save(event);
    }

    public List<EventShortResponseDto> getEventShortDtoListWithViews(List<Long> eventIds) {
        List<Event> events = eventRepository.findAllByIdIn(eventIds);
        if (events == null || events.isEmpty()) {
            return List.of();
        }
        return statisticService.getViewListShortDto(events);
    }
}
