package com.c3.logitrack.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        // Configuración para seguridad JWT (si usas en tu API)
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .info(new Info()
                        .title("API LogiTrack - Gestión de Bodegas")
                        .version("1.0")
                        .description("API REST para gestión y auditoría de bodegas")
                        .contact(new Contact()
                                .name("Equipo LogiTrack")
                                .email("soporte@logitrack.com")
                        )
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                );
    }
}