package ru.practicum.server.service;

import ru.practicum.stats.dto.HitRequestDto;
import ru.practicum.stats.dto.StatResponseDto;

import java.util.List;

public interface StatsService {
    void save(HitRequestDto requestDto);

    List<StatResponseDto> get(String start, String end, List<String> uris, boolean unique);
}
