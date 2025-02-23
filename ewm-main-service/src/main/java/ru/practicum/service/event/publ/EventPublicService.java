package ru.practicum.service.event.publ;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.comment.dto.CommentEventResponseDto;
import ru.practicum.service.event.common.EventMapper;
import ru.practicum.service.event.common.EventRepository;
import ru.practicum.service.event.common.dto.EventCommentsResponseDto;
import ru.practicum.service.event.common.dto.EventResponseDto;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.event.common.entity.State;
import ru.practicum.service.event.common.statistic.StatisticService;
import ru.practicum.service.event.common.validator.EventValidator;
import ru.practicum.service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
class EventPublicService {
    private EventRepository eventRepository;
    private EntityManager entityManager;
    private StatisticService statisticService;
    private CommentService commentService;

    public List<EventResponseDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                                  String start, String end, Boolean onlyAvailable, Sort sort, int from,
                                                  int size, HttpServletRequest httpServletRequest) {
        log.info("Пришел запрос на получение списка мероприятий");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (text != null && !text.isBlank()) {
            text = "%" + text.toLowerCase() + "%";
            Predicate annotationPredicate = builder
                    .like(builder.lower(root.get("annotation")), text);
            Predicate descriptionPredicate = builder
                    .like(builder.lower(root.get("description")), text);
            predicates.add(builder.or(annotationPredicate, descriptionPredicate));
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(root.get("category").get("id").in(categories));
        }

        if (paid != null) {
            predicates.add(builder.equal(root.get("paid"), paid));
        }

        predicates.add(EventValidator.checkRangeDates(start, end, builder, root));

        if (Boolean.TRUE.equals(onlyAvailable)) {
            predicates.add(builder.or(builder.isNull(root.get("participantLimit")),
                    builder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))));
        }

        query.select(root).where(predicates.toArray(new Predicate[0]));

        if (sort != null && sort == Sort.EVENT_DATE) {
            query.orderBy(builder.asc(root.get("eventDate")));
        }

        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        List<Event> eventsResult = typedQuery.getResultList();

        List<EventResponseDto> eventResponseDtoList = statisticService.getViewListFullDto(eventsResult);
        if (sort != null && sort == Sort.VIEWS) {
            eventResponseDtoList = eventResponseDtoList.stream()
                    .sorted(Comparator.comparing(EventResponseDto::getViews))
                    .toList();
        }
        statisticService.sendStatList(eventsResult, httpServletRequest);
        return eventResponseDtoList;
    }

    public EventCommentsResponseDto getEventById(Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Пришел запрос на получение мероприятия с id = {}", eventId);
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено с id = " + eventId));
        long view = statisticService.getView(event);
        statisticService.sendStat(httpServletRequest);
        List<CommentEventResponseDto> comments = commentService.getComments(event);
        return EventMapper.mapToEventDto(event, view, comments);
    }
}
