package com.example.bdd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cofopriopenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("COFOPRI - API Sistema de Catastro")
                        .description("""
                                API REST para el Sistema de Gestión Catastral y Titulación Predial de COFOPRI.
                                
                                Esta API implementa los casos de uso principales para:
                                - Gestión de ciudadanos y verificación con RENIEC
                                - Administración del catastro predial urbano
                                - Procesamiento de solicitudes de titulación
                                
                                El sistema utiliza arquitectura DDD (Domain-Driven Design) y está
                                diseñado para soportar los procesos de formalización de la propiedad
                                predial en el Perú.
                                """)
                        .version("1.0.0-SNAPSHOT")
                        .contact(new Contact()
                                .name("COFOPRI - Equipo de Desarrollo")
                                .email("desarrollo@cofopri.gob.pe")
                                .url("https://www.cofopri.gob.pe"))
                        .license(new License()
                                .name("Gobierno del Perú")
                                .url("https://www.gob.pe")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.cofopri.gob.pe")
                                .description("Servidor de Producción")))
                .tags(List.of(
                        new Tag()
                                .name("Sistema")
                                .description("Endpoints de información general del sistema"),
                        new Tag()
                                .name("Ciudadanos")
                                .description("Gestión de ciudadanos y verificación RENIEC"),
                        new Tag()
                                .name("Predios")
                                .description("Administración del catastro predial urbano"),
                        new Tag()
                                .name("Solicitudes")
                                .description("Procesamiento de solicitudes de titulación")));
    }
}