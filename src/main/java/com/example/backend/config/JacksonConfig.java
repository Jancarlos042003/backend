package com.example.backend.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

// Para manejar las conversiones de fecha en tu API.
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapperJackson() {
        ObjectMapper mapper = new ObjectMapper();

        // Registrar el módulo de Java 8 Date/Time
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        // Configuración específica para fechas
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    // Agregar este bean también
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapperJackson());
        return converter;
    }
}
