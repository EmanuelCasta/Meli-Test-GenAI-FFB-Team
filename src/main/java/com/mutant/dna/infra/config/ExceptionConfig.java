package com.mutant.dna.infra.config;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionConfig {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {

        if (ex.getStatusCode().value() == HttpStatus.FORBIDDEN.value()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", ex.getStatusCode().value());
        body.put("error", ex.getStatusCode().isError());
        body.put("message", ex.getReason());
        body.put("path", "/mutant/ - /stat/");

        return new ResponseEntity<>(body, ex.getStatusCode());
    }
}

