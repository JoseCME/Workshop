package com.ejemplo.demo.config;

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
                .title("Workshop Spring Boot API")
                .version("1.0.0")
                .description("API de saludos construida " +
                              "durante el workshop de Spring Boot 3")
                .contact(new Contact()
                    .name("Jose Carlos")
                    .email("jmorataye1@miumg.edu.gt")
                    .url("https://github.com/JoseCME/Workshop"))
            );
    }
}