package ru.practicum.service.event.common.statistic;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.service.event.common.EventMapper;
import ru.practicum.service.event.common.dto.EventResponseDto;
import ru.practicum.service.event.common.dto.EventShortResponseDto;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.stats.dto.HitRequestDto;
import ru.practicum.stats.dto.StatResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class StatisticService {
    private StatsClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String requestURI = "/events/";

    public void sendStatList(List<Event> events, HttpServletRequest httpServletRequest) {
        log.info("Отправляется запрос на сохранение списка из {} запросов мероприятий", events.size());
        HitRequestDto hitDto = new HitRequestDto();
        hitDto.setApp("ewm-main-service");
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setTimestamp((LocalDateTime.now()));
        for (Event event : events) {
            hitDto.setUri(httpServletRequest.getRequestURI() + "/" + event.getId());
            statsClient.addHit(hitDto);
        }
        //информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, сохраняем в сервисе статистики
        hitDto.setUri(httpServletRequest.getRequestURI());
        statsClient.addHit(hitDto);
    }

    public void sendStat(HttpServletRequest httpServletRequest) {
        log.info("Отправляется запрос на сохранение запроса мероприятия uri = {}", httpServletRequest.getRequestURI());
        HitRequestDto hitDto = new HitRequestDto();
        hitDto.setApp("ewm-main-service");
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setTimestamp(LocalDateTime.now());
        statsClient.addHit(hitDto);
    }

    public Map<String, Long> getViewList(List<Event> events, String start) {
        String end = LocalDateTime.now().format(formatter);
        List<String> uris = events
                .stream()
                .map(eventEntity -> requestURI + eventEntity.getId())
                .toList();

        List<StatResponseDto> statDtos = statsClient.getStats(
                start,
                end,
                uris,
                true);

        return statDtos.stream()
                .collect(Collectors.toMap(StatResponseDto::getUri, StatResponseDto::getHits));
    }

    public List<EventResponseDto> getViewListFullDto(List<Event> events) {
        log.info("Отправляется запрос на получение статистики просмотров для {} мероприятий", events.size());
        List<EventResponseDto> responseDtoList = events.stream().map(EventMapper::mapToEventDto).toList();
        String start = events.stream().min(Comparator.comparing(Event::getCreatedOn))
                .orElseThrow().getCreatedOn().format(formatter);
        Map<String, Long> viewMap = getViewList(events, start);
        for (EventResponseDto event : responseDtoList) {
            String uri = requestURI + event.getId();
            long views = viewMap.getOrDefault(uri, 0L);
            event.setViews(views);
        }
        return responseDtoList;
    }

    public List<EventShortResponseDto> getViewListShortDto(List<Event> events) {
        log.info("Отправляется запрос на получение статистики просмотров для {} мероприятий", events.size());
        List<EventShortResponseDto> responseDtoList = events.stream().map(EventMapper::mapToEventShortDto).toList();
        String start = events.stream().min(Comparator.comparing(Event::getCreatedOn))
                .orElseThrow().getCreatedOn().format(formatter);
        Map<String, Long> viewMap = getViewList(events, start);
        for (EventShortResponseDto event : responseDtoList) {
            String uri = requestURI + event.getId();
            long views = viewMap.getOrDefault(uri, 0L);
            event.setViews(views);
        }
        return responseDtoList;
    }

    public long getView(Event event) {
        log.info("Отправляется запрос на получение статистики просмотров мероприятия с id = {}", event.getId());
        String start = event.getCreatedOn().format(formatter);
        String end = LocalDateTime.now().format(formatter);
        String uris = requestURI + event.getId();

        List<StatResponseDto> statDtos = statsClient.getStats(
                start,
                end,
                List.of(uris),
                true);
        if (statDtos == null || statDtos.isEmpty()) {
            return 0L;
        }
        return statDtos.getFirst().getHits();
    }
}
