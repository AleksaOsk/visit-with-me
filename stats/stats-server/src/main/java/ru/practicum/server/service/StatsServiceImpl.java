package ru.practicum.server.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.StatsRepository;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.mapper.HitMapper;
import ru.practicum.server.mapper.StatMapper;
import ru.practicum.server.model.Hit;
import ru.practicum.server.model.Stat;
import ru.practicum.stats.dto.HitRequestDto;
import ru.practicum.stats.dto.StatResponseDto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private StatsRepository statsRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void save(HitRequestDto requestDto) {
        log.info("Пришел запрос на сохранение записи запроса для {} от пользователя с ip {}",
                requestDto.getUri(), requestDto.getIp());
        Hit hit = HitMapper.mapToHit(requestDto);
        statsRepository.save(hit);
    }

    @Override
    public List<StatResponseDto> get(String start, String end, List<String> uris, boolean unique) {
        log.info("Пришел запрос на получение статистики за даты с {} по {}, уникальность = {}, list = {}",
                start, end, unique, uris);
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter);
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter);

        if (startTime.isAfter(endTime)) {
            throw new ValidationException("начало не может быть позже конца");
        }
        List<Stat> list;
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                list = statsRepository.findAllWithoutUrisUnique(startTime, endTime);
            } else {
                list = statsRepository.findAllWithoutUrisNotUnique(startTime, endTime);
            }
        } else {
            if (unique) {
                list = statsRepository.findAllWithUrisUnique(startTime, endTime, uris);

            } else {
                list = statsRepository.findAllWithUrisNotUnique(startTime, endTime, uris);
            }
        }
        return list.stream()
                .map(StatMapper::mapToStatDto)
                .toList();
    }
}
