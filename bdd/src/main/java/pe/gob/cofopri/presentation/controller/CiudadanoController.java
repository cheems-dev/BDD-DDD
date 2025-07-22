package pe.gob.cofopri.presentation.controller;

import pe.gob.cofopri.ciudadanos.application.service.CiudadanoApplicationService;
import pe.gob.cofopri.ciudadanos.application.dto.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller para gestión de ciudadanos.
 * Implementa endpoints BDD para el módulo de Ciudadanos.
 */
@RestController
@RequestMapping("/api/ciudadanos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Ciudadanos", description = "Gestión de ciudadanos y verificación con RENIEC")
public class CiudadanoController {

    private final CiudadanoApplicationService ciudadanoService;

    /**
     * GET /api/ciudadanos - Listar todos los ciudadanos
     * 
     * Feature: Como funcionario necesito ver todos los ciudadanos registrados
     * Scenario: Obtener listado completo de ciudadanos
     */
    @GetMapping
    @Operation(summary = "Listar todos los ciudadanos", description = "Obtiene el listado completo de ciudadanos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ciudadanos obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<CiudadanoDTO>> listarTodos() {
        List<CiudadanoDTO> ciudadanos = ciudadanoService.listarTodos();
        return ResponseEntity.ok(ciudadanos);
    }

    /**
     * POST /api/ciudadanos - Crear ciudadano
     * 
     * Feature: Como sistema necesito registrar ciudadanos
     * Scenario: Crear ciudadano con datos válidos
     */
    @PostMapping
    public ResponseEntity<CiudadanoDTO> crearCiudadano(@Valid @RequestBody CrearCiudadanoCommand command) {
        CiudadanoDTO ciudadano = ciudadanoService.crearCiudadano(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadano);
    }

    /**
     * GET /api/ciudadanos/dni/{dni} - Buscar por DNI
     * 
     * Feature: Como funcionario necesito buscar un ciudadano por DNI
     * Scenario: Ciudadano encontrado por DNI válido
     */
    @GetMapping("/dni/{dni}")
    public ResponseEntity<CiudadanoDTO> buscarPorDNI(@PathVariable String dni) {
        CiudadanoDTO ciudadano = ciudadanoService.obtenerPorDNI(dni);
        return ResponseEntity.ok(ciudadano);
    }

    /**
     * POST /api/ciudadanos/dni/{dni}/verificar - Verificar identidad RENIEC
     * 
     * Feature: Como sistema necesito verificar identidad en RENIEC
     * Scenario: Verificación exitosa con RENIEC
     */
    @PostMapping("/dni/{dni}/verificar")
    public ResponseEntity<CiudadanoDTO> verificarIdentidad(@PathVariable String dni) {
        CiudadanoDTO ciudadano = ciudadanoService.verificarIdentidad(dni);
        return ResponseEntity.ok(ciudadano);
    }

    /**
     * Manejo de errores específicos
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("DATOS_INVALIDOS", e.getMessage()));
    }

    private record ErrorResponse(String codigo, String mensaje) {}
}