package com.example.business.rest;

import com.example.domain.common.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class BusinessControllerAdvice {

    private final Map<BaseException.Type, HttpStatus> codeMap = new HashMap<BaseException.Type, HttpStatus>() {{
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
}
