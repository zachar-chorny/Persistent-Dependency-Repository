package com.example.dependencytreeproject.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import javax.annotation.PostConstruct;

@Configuration
@AllArgsConstructor
public class MongoConfig{
    private final MappingMongoConverter mongoConverter;

    @PostConstruct
    public void setUpMongoConverter() {
        mongoConverter.setMapKeyDotReplacement(".");
    }
}
