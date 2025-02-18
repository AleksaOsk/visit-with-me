package ru.practicum.server.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
class ErrorResponse {
    private final String status;
    private final String reason;
    private String message;
    private final String timestamp;
}
