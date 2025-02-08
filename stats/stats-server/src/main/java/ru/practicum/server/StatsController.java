package ru.practicum.server;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.service.StatsService;
import ru.practicum.stats.dto.HitRequestDto;
import ru.practicum.stats.dto.StatResponseDto;

import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    StatsService statsService;

    @PostMapping("/hit")
    public void save(@RequestBody HitRequestDto requestDto) {
        statsService.save(requestDto);
    }

    @GetMapping("/stats")
    public List<StatResponseDto> get(@RequestParam String start,
                                     @RequestParam String end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return statsService.get(start, end, uris, unique);
    }
}
