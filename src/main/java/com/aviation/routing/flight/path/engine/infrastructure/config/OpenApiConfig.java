package com.aviation.routing.flight.path.engine.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Flight Path Engine API")
                        .version("1.0.0")
                        .description("Aviation routing and transportation management system API documentation.")
                        .contact(new Contact()
                                .name("Kadir")
                                .email("isik.kadr@gmail.com")));
    }
}