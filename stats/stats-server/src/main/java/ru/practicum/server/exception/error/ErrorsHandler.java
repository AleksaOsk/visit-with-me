package ru.practicum.server.exception.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.server.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
class ErrorsHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValidException(final ValidationException e) {
        log.error("Пользователь указал некорректные данные. " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Указаны некорректные данные",
                e.getReason(), now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAnnotationsObject(MethodArgumentNotValidException e) { //исключение при срабатывании аннотации на объектах
        String response = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("Пользователь указал некорректные данные." + response);
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Указаны некорректные данные",
                response, now.format(formatter));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse handleMissingParams(MissingServletRequestParameterException e) {
        String parameterName = Objects.requireNonNull(e.getParameterName());
        log.error("Пользователь не указал обязательный параметр: " + parameterName);
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                "Отсутствует обязательный параметр", parameterName + " не указано",
                now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Произошла ошибка на сервере. INTERNAL_SERVER_ERROR: " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Произошла ошибка на сервере",
                "INTERNAL_SERVER_ERROR", now.format(formatter));
    }
}
