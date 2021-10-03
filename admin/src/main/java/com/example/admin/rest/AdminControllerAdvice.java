package com.example.admin.rest;

import com.example.domain.common.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class AdminControllerAdvice {

    private final Map<BaseException.Type, HttpStatus> codeMap = new HashMap<>() {{
        put(BaseException.Type.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        put(BaseException.Type.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        put(BaseException.Type.FORBIDDEN, HttpStatus.FORBIDDEN);
        put(BaseException.Type.NOT_FOUND, HttpStatus.NOT_FOUND);
        put(BaseException.Type.CONFLICT, HttpStatus.CONFLICT);
    }};

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, String>> handleBaseException(BaseException ex) {
        log.error("business exception：{}", ex.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return ResponseEntity.status(codeMap.get(ex.getType())).body(map);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<String> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> handleConstraintViolationException(
            ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath() + constraintViolation.getMessage())
                .collect(Collectors.toList());

        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
