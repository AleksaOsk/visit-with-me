package ru.practicum.service.event.common.validator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.exception.ConflictException;
import ru.practicum.service.exception.ValidationException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventValidator {

    public static Predicate checkRangeDates(String start, String end, CriteriaBuilder builder,
                                            Root<Event> root) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;
        if (start != null && end != null) {
            rangeStart = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter);
            rangeEnd = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter);
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidationException("rangeEnd не может быть раньше rangeStart");
            }
            return builder.between(root.get("eventDate"), rangeStart, rangeEnd);
        } else if (start != null) {
            rangeStart = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter);
            return builder.greaterThan(root.get("eventDate"), rangeStart);
        } else if (end != null) {
            rangeEnd = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter);
            return builder.lessThan(root.get("eventDate"), rangeEnd);
        } else {
            return builder.greaterThan(root.get("eventDate"), LocalDateTime.now());
        }
    }

    public static void checkCorrectTime(LocalDateTime publishedOn, LocalDateTime eventTime) {
        LocalDateTime timeMinusHour = publishedOn.minusHours(1);
        if (eventTime.isBefore(timeMinusHour)) {
            throw new ConflictException("дата начала события должна быть не ранее чем за час от даты публикации");
        }
    }

    public static void checkTimeInFuture(LocalDateTime createdOn, LocalDateTime eventTime) {
        LocalDateTime timePlusTwoHour = createdOn.plusHours(2);
        if (eventTime.isBefore(timePlusTwoHour)) {
            throw new ValidationException("дата начала события должна быть не ранее чем через 2 часа от даты создания");
        }
    }
}
