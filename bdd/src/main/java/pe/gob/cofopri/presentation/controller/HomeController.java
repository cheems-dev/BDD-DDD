package pe.gob.cofopri.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controlador principal para la página de inicio y información general del sistema.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@RestController
@Tag(name = "Sistema", description = "Endpoints de información general del sistema COFOPRI")
public class HomeController {

    /**
     * GET / - Página principal del sistema
     * 
     * Proporciona información básica sobre el sistema COFOPRI
     * y enlaces a los endpoints disponibles.
     */
    @Operation(
        summary = "Información principal del sistema",
        description = "Endpoint de bienvenida que proporciona información general del sistema COFOPRI y enlaces a las APIs disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información del sistema obtenida exitosamente")
    })
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
            "aplicacion", "COFOPRI - Sistema de Catastro con DDD",
            "version", "1.0.0-SNAPSHOT",
            "descripcion", "Sistema de gestión catastral y titulación predial para COFOPRI",
            "timestamp", LocalDateTime.now(),
            "estado", "ACTIVO",
            "endpoints", Map.of(
                "ciudadanos", "/api/ciudadanos",
                "solicitudes", "/api/solicitudes", 
                "predios", "/api/predios",
                "health", "/health",
                "info", "/info"
            ),
            "documentacion", Map.of(
                "swagger-ui", "http://localhost:8080/swagger-ui.html",
                "api-docs-json", "http://localhost:8080/v3/api-docs",
                "api-docs-yaml", "http://localhost:8080/v3/api-docs.yaml"
            ),
            "herramientas", Map.of(
                "h2-console", "http://localhost:8080/h2-console",
                "swagger-ui", "http://localhost:8080/swagger-ui.html"
            )
        ));
    }

    /**
     * GET /health - Estado de salud del sistema
     * 
     * Endpoint simple para verificar que el sistema está funcionando.
     */
    @Operation(
        summary = "Estado de salud del sistema",
        description = "Verifica que el sistema COFOPRI esté funcionando correctamente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sistema funcionando correctamente")
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().toString(),
            "message", "Sistema COFOPRI funcionando correctamente"
        ));
    }

    /**
     * GET /info - Información detallada del sistema
     * 
     * Proporciona información técnica y estadísticas del sistema.
     */
    @Operation(
        summary = "Información detallada del sistema",
        description = "Proporciona información técnica completa sobre el sistema COFOPRI, incluyendo módulos y características"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información detallada obtenida exitosamente")
    })
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
            "sistema", Map.of(
                "nombre", "COFOPRI - Sistema de Catastro",
                "institucion", "Organismo de Formalización de la Propiedad Informal",
                "pais", "Perú",
                "arquitectura", "DDD (Domain-Driven Design)",
                "framework", "Spring Boot 3.x"
            ),
            "modulos", Map.of(
                "ciudadanos", "Gestión de ciudadanos y verificación RENIEC",
                "catastro", "Gestión de predios y información catastral",
                "solicitudes", "Procesamiento de solicitudes de titulación",
                "infrastructure", "Persistencia JPA e integración externa"
            ),
            "caracteristicas", java.util.List.of(
                "Arquitectura DDD",
                "REST APIs",
                "Validación de DNI peruano",
                "Gestión de estados de solicitudes",
                "Integración con RENIEC",
                "Geolocalización de predios"
            )
        ));
    }
}