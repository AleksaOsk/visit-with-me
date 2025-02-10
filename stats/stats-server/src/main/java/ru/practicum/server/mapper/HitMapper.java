package ru.practicum.server.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.server.model.Hit;
import ru.practicum.stats.dto.HitRequestDto;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {
    public static Hit mapToHit(HitRequestDto request, LocalDateTime time) {
        Hit hit = new Hit();
        hit.setApp(request.getApp());
        hit.setUri(request.getUri());
        hit.setIp(request.getIp());
        hit.setTimestamp(time);
        return hit;
    }
}
