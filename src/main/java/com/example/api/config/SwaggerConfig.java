package com.example.api.config;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {
 @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Contatos")
                .version("1.0")
                .description("Desafio técnico com Spring Boot + Angular"));
    }
}
