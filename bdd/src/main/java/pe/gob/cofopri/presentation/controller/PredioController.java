package pe.gob.cofopri.presentation.controller;

import pe.gob.cofopri.catastro.application.service.CatastroApplicationService;
import pe.gob.cofopri.catastro.application.dto.*;
import pe.gob.cofopri.catastro.application.service.EstadisticasPrediosDTO;

import io.swagger.v3.oas.annotations.Operation;
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
 * REST Controller para gestión de predios y catastro.
 * Implementa endpoints BDD para el módulo de Catastro.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@RestController
@RequestMapping("/api/predios")
@RequiredArgsConstructor
@Validated
@Tag(name = "Predios", description = "Administración del catastro predial urbano")
public class PredioController {

    private final CatastroApplicationService catastroService;

    /**
     * GET /api/predios - Listar todos los predios
     * 
     * Feature: Como funcionario necesito ver todos los predios registrados
     * Scenario: Obtener listado completo de predios
     */
    @GetMapping
    @Operation(summary = "Listar todos los predios", description = "Obtiene el listado completo de predios registrados en el catastro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de predios obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<PredioDTO>> listarTodos() {
        List<PredioDTO> predios = catastroService.listarTodos();
        return ResponseEntity.ok(predios);
    }

    /**
     * POST /api/predios - Crear nuevo predio
     * 
     * Feature: Como funcionario quiero registrar un nuevo predio
     * Scenario: Crear predio con datos válidos
     */
    @PostMapping
    public ResponseEntity<PredioDTO> crearPredio(@Valid @RequestBody CrearPredioCommand command) {
        PredioDTO predio = catastroService.registrarPredio(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(predio);
    }

    /**
     * GET /api/predios/{codigo} - Obtener predio por código catastral
     * 
     * Feature: Como usuario quiero consultar información de un predio
     * Scenario: Predio encontrado por código válido
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<PredioDTO> obtenerPredio(@PathVariable String codigo) {
        return catastroService.buscarPredio(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/predios/{codigo} - Actualizar información del predio
     * 
     * Feature: Como funcionario quiero actualizar datos catastrales
     * Scenario: Actualización exitosa de predio
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<PredioDTO> actualizarPredio(@PathVariable String codigo,
                                                     @Valid @RequestBody ActualizarPredioCommand command) {
        // El comando ya debe incluir el código del predio
        PredioDTO predio = catastroService.actualizarPredio(command);
        return ResponseEntity.ok(predio);
    }

    /**
     * GET /api/predios/propietario/{propietario} - Buscar predios por propietario
     * 
     * Feature: Como funcionario quiero buscar predios de un propietario
     * Scenario: Predios encontrados por nombre de propietario
     */
    @GetMapping("/propietario/{propietario}")
    public ResponseEntity<List<PredioDTO>> buscarPorPropietario(@PathVariable String propietario) {
        List<PredioDTO> predios = catastroService.buscarPrediosPorPropietario(propietario);
        return ResponseEntity.ok(predios);
    }

    /**
     * GET /api/predios/ubicacion - Buscar predios por ubicación
     * 
     * Feature: Como funcionario quiero buscar predios por ubicación geográfica
     * Scenario: Predios encontrados en área específica
     */
    @GetMapping("/ubicacion")
    public ResponseEntity<List<PredioDTO>> buscarPorUbicacion(@RequestParam double latitud, 
                                                             @RequestParam double longitud, 
                                                             @RequestParam(defaultValue = "1000") double radio) {
        List<PredioDTO> predios = catastroService.buscarPrediosCercanos(latitud, longitud, radio);
        return ResponseEntity.ok(predios);
    }

    /**
     * GET /api/predios/estadisticas - Obtener estadísticas de predios
     * 
     * Feature: Como administrador quiero ver estadísticas del catastro
     * Scenario: Generar reporte estadístico de predios
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasPrediosDTO> obtenerEstadisticas() {
        EstadisticasPrediosDTO estadisticas = catastroService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
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