package com.example.dependencytreeproject.config;


import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class AppConfig {

    @Bean
    public MavenXpp3Reader getMavenReader() {
        return new MavenXpp3Reader();
    }

}
