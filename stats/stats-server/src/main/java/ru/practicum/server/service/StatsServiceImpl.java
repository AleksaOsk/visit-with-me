package ru.practicum.server.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.StatsRepository;
import ru.practicum.server.mapper.HitMapper;
import ru.practicum.server.mapper.StatMapper;
import ru.practicum.server.model.Hit;
import ru.practicum.server.model.Stat;
import ru.practicum.stats.dto.HitRequestDto;
import ru.practicum.stats.dto.StatResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    StatsRepository statsRepository;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void save(HitRequestDto requestDto) {
        log.info("Пришел запрос на сохранение записи запроса для {} от пользователя с ip {}",
                requestDto.getUri(), requestDto.getIp());
        LocalDateTime time = LocalDateTime.parse(requestDto.getTimestamp(), formatter);
        Hit hit = HitMapper.mapToHit(requestDto, time);
        statsRepository.save(hit);
    }

    @Override
    public List<StatResponseDto> get(String start, String end, List<String> uris, boolean unique) {
        log.info("Пришел запрос на получение статистики за даты с {} по {}, уникальность = {}", start, end, unique);
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
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
