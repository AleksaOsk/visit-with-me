package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.stats.dto.HitRequestDto;
import ru.practicum.stats.dto.StatResponseDto;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class StatisticController {
    StatsClient statsClient;

    @PostMapping("/hits")
    public void addHit(@RequestBody HitRequestDto hitDto) {
        statsClient.addHit(hitDto);
    }

    @GetMapping("/stat")
    public List<StatResponseDto> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique
    ) {
        return statsClient.getStats(start, end, uris, unique);
    }
}
