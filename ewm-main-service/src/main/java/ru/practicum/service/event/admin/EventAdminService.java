package ru.practicum.service.event.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.category.common.entity.Category;
import ru.practicum.service.category.common.service.CategoryService;
import ru.practicum.service.event.common.EventMapper;
import ru.practicum.service.event.common.EventRepository;
import ru.practicum.service.event.common.dto.EventResponseDto;
import ru.practicum.service.event.common.dto.UpdateEvenRequestDto;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.event.common.entity.State;
import ru.practicum.service.event.common.statistic.StatisticService;
import ru.practicum.service.event.common.validator.EventValidator;
import ru.practicum.service.exception.ConflictException;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
class EventAdminService {
    private EventRepository eventRepository;
    private EntityManager entityManager;
    private StatisticService statisticService;
    private CategoryService categoryService;

    public List<EventResponseDto> getAllEvents(List<Long> users, List<String> states, List<Long> categories,
                                               String start, String end, int from, int size) {
        log.info("Пришел запрос от админа на получение всех событий: users = {}, states = {}, categories = {}, " +
                 "start = {}, end = {}, from = {}, size = {}, ", users, states, categories, start, end, from, size);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            predicates.add(root.get("initiator").get("id").in(users));
        }

        if (states != null && !states.isEmpty()) {
            List<State> stateList = states.stream().map(State::fromString).filter(Objects::nonNull).toList();
            if (!stateList.isEmpty()) {
                CriteriaBuilder.In<String> statesPredicate = builder.in(root.get("state"));
                for (State state : stateList) {
                    statesPredicate.value(state.toString());
                }
                predicates.add(statesPredicate);
            }
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(root.get("category").get("id").in(categories));
        }

        predicates.add(EventValidator.checkRangeDates(start, end, builder, root));

        query.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        List<Event> eventsResult = typedQuery.getResultList();

        if (eventsResult.isEmpty()) {
            return List.of();
        }
        return statisticService.getViewListFullDto(eventsResult);
    }

    public EventResponseDto updateEvent(Long eventId, UpdateEvenRequestDto requestDto) {
        log.info("Пришел запрос от админа редактирование мероприятия и его статуса eventId = {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ValidationException("Мероприятие не найдено с id = " + eventId));
        if (event.getState() == State.PUBLISHED && requestDto.getStateAction() == State.REJECT_EVENT) {
            throw new ConflictException("нельзя отменить уже опубликованное мероприятие");
        } else if (event.getState() == State.PUBLISHED && requestDto.getStateAction() == State.PUBLISH_EVENT) {
            throw new ConflictException("нельзя опубликовать опубликованное мероприятие");
        } else if (event.getState() == State.CANCELED && requestDto.getStateAction() == State.PUBLISH_EVENT) {
            throw new ConflictException("нельзя опубликовать отмененное мероприятие");
        }

        if (event.getPublishedOn() != null && requestDto.getEventDate() != null) {
            EventValidator.checkCorrectTime(event.getPublishedOn(), requestDto.getEventDate());
        }
        if (requestDto.getEventDate() != null) {
            EventValidator.checkTimeInFuture(event.getCreatedOn(), requestDto.getEventDate());
        }
        if (requestDto.getCategoryId() != null) {
            try {
                Category category = categoryService.getCategory(requestDto.getCategoryId());
                event.setCategory(category);
            } catch (NotFoundException ignored) {
            }
        }
        if (requestDto.getStateAction() == State.PUBLISH_EVENT) {
            requestDto.setStateAction(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (requestDto.getStateAction() == State.REJECT_EVENT) {
            requestDto.setStateAction(State.CANCELED);
        }
        event = EventMapper.updateMapToEvent(requestDto, event);
        event = eventRepository.save(event);
        long view = statisticService.getView(event);
        return EventMapper.mapToEventDto(event, view);
    }
}
