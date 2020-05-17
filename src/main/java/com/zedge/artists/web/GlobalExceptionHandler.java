package com.zedge.artists.web;

import com.zedge.artists.error.HttpClientException;
import com.zedge.artists.error.ResourceNotFound;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientException(HttpClientException ex, WebRequest request) {
        log.error(ex);
        return ResponseEntity.status(ex.getStatus())
                .body(messageBuilder(ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFound ex, WebRequest request) {
        log.error(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(messageBuilder("Resource not found"));
    }

    private Map<String, Object> messageBuilder(String message) {
        return Map.of("error", message);
    }
}
