package com.challenge.faire.config;

import com.challenge.faire.FaireChallengeApplication;
import com.challenge.faire.gateway.FaireProxy;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import feign.Feign;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Created by weberling on 23/03/19.
 */
@Configuration
@ComponentScan(basePackageClasses = FaireChallengeApplication.class)
public class Config {

    @Value("${url}")
    private String baseUrl;

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false)
                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).build();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public FaireProxy connectFaire(ObjectMapper objectMapper) {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .decoder(new JacksonDecoder(objectMapper))
                .encoder(new JacksonEncoder(objectMapper))
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .target(FaireProxy.class, baseUrl);
    }
}
