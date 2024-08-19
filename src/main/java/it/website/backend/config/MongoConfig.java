package it.website.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDatabaseFactory, MongoMappingContext context) {
        MappingMongoConverter converter = new MappingMongoConverter(mongoDatabaseFactory, context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // Remove _class field
        return converter;
    }
}