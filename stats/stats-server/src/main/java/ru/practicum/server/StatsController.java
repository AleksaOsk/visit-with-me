package ru.practicum.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.service.StatsService;
import ru.practicum.stats.dto.HitRequestDto;
import ru.practicum.stats.dto.StatResponseDto;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class StatsController {
    private StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
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
