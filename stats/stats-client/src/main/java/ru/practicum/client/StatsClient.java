package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats.dto.HitRequestDto;
import ru.practicum.stats.dto.StatResponseDto;

import java.util.List;

@Slf4j
@Service
public class StatsClient {

    private final RestTemplate restTemplate;
    private final String serverUrl;


    @Autowired
    public StatsClient(@Value("${server.url}") String serverUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public void addHit(HitRequestDto hitDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HitRequestDto> request = new HttpEntity<>(hitDto, headers);

        restTemplate.postForEntity(serverUrl + "/hit", request, HitRequestDto.class);

    }

    public List<StatResponseDto> getStats(String start, String end, List<String> uris, boolean unique) {

        if (start.contains(" ") || end.contains(" ")) {
            start = start.replace(" ", "T");
            end = end.replace(" ", "T");
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl + "/stats")
                .queryParam("start", start)
                .queryParam("end", end);
        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", String.join(",", uris));
        }
        builder.queryParam("unique", unique);

        String url = builder.toUriString();

        log.trace("URL: {}", url);

        ResponseEntity<List<StatResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StatResponseDto>>() {
                });
        return response.getBody();
    }
}