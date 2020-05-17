package com.zedge.artists._integration._config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("test-db")
@PropertySource("classpath:test-database.properties")
public class DatabaseTestConfiguration {

}
