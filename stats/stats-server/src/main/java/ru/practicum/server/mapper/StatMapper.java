package ru.practicum.server.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.server.model.Stat;
import ru.practicum.stats.dto.StatResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StatMapper {
    public static StatResponseDto mapToStatDto(Stat stat) {
        StatResponseDto statResponseDto = new StatResponseDto();
        statResponseDto.setApp(stat.getApp());
        statResponseDto.setUri(stat.getUri());
        statResponseDto.setHits(stat.getHits());
        return statResponseDto;
    }
}
