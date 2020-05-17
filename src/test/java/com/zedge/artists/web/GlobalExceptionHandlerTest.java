package com.zedge.artists.web;

import com.zedge.artists.error.HttpClientException;
import com.zedge.artists.error.ResourceNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;


class GlobalExceptionHandlerTest {

    GlobalExceptionHandler target = new GlobalExceptionHandler();

    @Test
    void handleHttpClientException_ReturnCorrectResponseEntity() {
        String errorCause = "Request badly formatted";
        int status = HttpStatus.BAD_REQUEST.value();

        var result = target.handleHttpClientException(new HttpClientException(errorCause, status), null);

        assertNotNull(result.getBody());
        assertEquals(errorCause, result.getBody().get("error"));
        assertEquals(status, result.getStatusCodeValue());
    }

    @Test
    void handleResourceNotFound_ReturnCorrectResponseEntity() {
        var result = target.handleResourceNotFound(new ResourceNotFound(), null);

        assertNotNull(result.getBody());
        assertEquals("Resource not found", result.getBody().get("error"));
        assertEquals(404, result.getStatusCodeValue());
    }
}