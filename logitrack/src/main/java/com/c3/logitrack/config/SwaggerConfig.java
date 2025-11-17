package com.c3.logitrack.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.logitrack.com");
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setName("Equipo de Soporte LogiTrack");
        contact.setEmail("soporte@logitrack.com");
        contact.setUrl("https://www.logitrack.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("API REST LogiTrack")
                .version("1.0.0")
                .description("Sistema de gestión y auditoría de bodegas e inventario. " +
                        "Esta API proporciona endpoints para la administración de productos, bodegas, movimientos de inventario y auditoría.")
                .termsOfService("https://www.logitrack.com/terms")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese un token JWT válido para acceder a los endpoints protegidos. " +
                                                "Obtenga el token en el endpoint /auth/login")
                        )
                );
    }
}