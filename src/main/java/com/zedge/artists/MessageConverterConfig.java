package com.zedge.artists;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

@Configuration
public class MessageConverterConfig implements ApplicationContextAware {

    /*
     * Nasty but the easiest and the cleanest way to add a new media type
     * without overriding preconfigured default json message converter.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        var converter = applicationContext.getBean(MappingJackson2HttpMessageConverter.class);

        ArrayList<MediaType> mediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        mediaTypes.add(new MediaType("text", "javascript"));

        converter.setSupportedMediaTypes(mediaTypes);
    }
}
