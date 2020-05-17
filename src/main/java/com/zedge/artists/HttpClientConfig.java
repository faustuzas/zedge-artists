package com.zedge.artists;

import com.zedge.artists.error.HttpClientException;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
@EnableFeignClients
public class HttpClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> new HttpClientException(
                response.reason() + ": " + response.request().url(),
                response.status()
        );
    }
}
