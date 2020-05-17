package com.zedge.artists._integration._config;

import com.zedge.artists.search.itunes.ItunesHttpClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global mocks of HTTP clients.
 *
 * You can use them by injecting it to your test class
 * via {@code @Autowired} and configuring them with Mockito.
 */
@Configuration
public class HttpClientConfiguration {

    @Bean
    public ItunesHttpClient itunesHttpClient() {
        return Mockito.mock(ItunesHttpClient.class);
    }
}
