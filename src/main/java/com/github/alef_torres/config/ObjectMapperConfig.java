package com.github.alef_torres.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleFilterProvider filterProviderPerson = new SimpleFilterProvider()
                .addFilter("PersonFilter", SimpleBeanPropertyFilter.serializeAllExcept(
                        "sensitiveData"));
        SimpleFilterProvider filterProviderBook = new SimpleFilterProvider()
                .addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAllExcept(
                        "sensitiveData"));
        objectMapper.setFilters(filterProviderPerson);
        return objectMapper;
    }
}

