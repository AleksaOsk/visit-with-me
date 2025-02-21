package ru.practicum.service.exception.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.service.exception.ConflictException;
import ru.practicum.service.exception.DuplicatedException;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.exception.ValidationException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
class ErrorHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValidException(final ValidationException e) {
        log.error("Пользователь указал некорректные данные. " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Указаны некорректные данные",
                e.getReason(), now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleParameterConflictException(final ConflictException e) {
        log.error("Пользователь указал некорректные данные. " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), "Указаны некорректные данные",
                e.getReason(), now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("Пользователь указал некорректные данные. " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Указаны некорректные данные",
                e.getReason(), now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedDataException(final DuplicatedException e) {
        log.error("Пользователь указал некорректные данные. " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), "Указаны некорректные данные",
                e.getReason(), now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse handleAnnotationsField(ConstraintViolationException e) { //исключение при срабатывании аннотации на отдельных полях (переменные пути)
        String response = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        log.error("Пользователь указал некорректные данные. " + response);
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Указаны некорректные данные",
                response, now.format(formatter));
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(final UnsupportedEncodingException e) {
        log.error("Пользователь указал некорректные данные.");
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Даты указаны некорректно",
                "Необходим формат 'yyyy-MM-dd HH:mm:ss'", now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Произошла ошибка на сервере. INTERNAL_SERVER_ERROR: " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Произошла ошибка на сервере",
                now.format(formatter));
    }
}
