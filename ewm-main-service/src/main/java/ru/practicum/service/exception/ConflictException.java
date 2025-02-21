package ru.practicum.service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConflictException extends RuntimeException {
    private String reason;

    public ConflictException(String reason) {
        this.reason = reason;
    }
}
