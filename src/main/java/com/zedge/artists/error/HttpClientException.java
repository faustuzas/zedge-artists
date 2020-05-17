package com.zedge.artists.error;

import lombok.Getter;

@Getter
public class HttpClientException extends RuntimeException {

    private int status;

    public HttpClientException(String message, int status) {
        super(message);
        this.status = status;
    }
}
