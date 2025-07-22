package pe.gob.cofopri.presentation.controller;

import pe.gob.cofopri.solicitudes.application.service.SolicitudApplicationService;
import pe.gob.cofopri.solicitudes.application.dto.*;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud;

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
 * REST Controller para gestión de solicitudes de titulación.
 * Implementa endpoints BDD para el módulo de Solicitudes.
 */
@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Solicitudes", description = "Procesamiento de solicitudes de titulación predial")
public class SolicitudController {

    private final SolicitudApplicationService solicitudService;

    /**
     * GET /api/solicitudes - Listar todas las solicitudes
     * 
     * Feature: Como funcionario necesito ver todas las solicitudes registradas
     * Scenario: Obtener listado completo de solicitudes
     */
    @GetMapping
    @Operation(summary = "Listar todas las solicitudes", description = "Obtiene el listado completo de solicitudes de titulación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<SolicitudDTO>> listarTodas() {
        List<SolicitudDTO> solicitudes = solicitudService.listarTodas();
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * POST /api/solicitudes - Crear nueva solicitud
     * 
     * Feature: Como ciudadano quiero crear una solicitud de titulación
     * Scenario: Crear solicitud exitosa con datos válidos
     */
    @PostMapping
    public ResponseEntity<SolicitudDTO> crearSolicitud(@Valid @RequestBody CrearSolicitudCommand command) {
        SolicitudDTO solicitud = solicitudService.crearSolicitud(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitud);
    }

    /**
     * GET /api/solicitudes/{id} - Obtener solicitud por ID
     * 
     * Feature: Como usuario quiero consultar una solicitud específica
     * Scenario: Obtener solicitud existente
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> obtenerSolicitud(@PathVariable String id) {
        SolicitudDTO solicitud = solicitudService.obtenerSolicitud(id);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * GET /api/solicitudes/dni/{dni} - Obtener solicitudes por DNI
     * 
     * Feature: Como ciudadano quiero ver todas mis solicitudes
     * Scenario: Consultar solicitudes por DNI
     */
    @GetMapping("/dni/{dni}")
    public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesPorDNI(@PathVariable String dni) {
        List<SolicitudDTO> solicitudes = solicitudService.obtenerSolicitudesPorDNI(dni);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * PUT /api/solicitudes/{id}/estado - Cambiar estado de solicitud
     * 
     * Feature: Como funcionario quiero cambiar el estado de una solicitud
     * Scenario: Cambio de estado válido
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<SolicitudDTO> cambiarEstado(@PathVariable String id, 
                                                     @Valid @RequestBody CambiarEstadoSolicitudCommand command) {
        command.setSolicitudId(id);
        SolicitudDTO solicitud = solicitudService.cambiarEstadoSolicitud(command);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * PUT /api/solicitudes/{id} - Actualizar solicitud
     * 
     * Feature: Como funcionario quiero actualizar datos de una solicitud
     * Scenario: Actualización exitosa
     */
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudDTO> actualizarSolicitud(@PathVariable String id,
                                                           @Valid @RequestBody ActualizarSolicitudCommand command) {
        command.setSolicitudId(id);
        SolicitudDTO solicitud = solicitudService.actualizarSolicitud(command);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * POST /api/solicitudes/{id}/documentos - Agregar documento
     * 
     * Feature: Como ciudadano quiero agregar documentos a mi solicitud
     * Scenario: Documento agregado exitosamente
     */
    @PostMapping("/{id}/documentos")
    public ResponseEntity<SolicitudDTO> agregarDocumento(@PathVariable String id,
                                                        @RequestParam String documento) {
        SolicitudDTO solicitud = solicitudService.agregarDocumento(id, documento);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * GET /api/solicitudes/estado/{estado} - Obtener por estado
     * 
     * Feature: Como funcionario quiero ver solicitudes por estado
     * Scenario: Listar solicitudes en estado específico
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudDTO>> obtenerPorEstado(@PathVariable EstadoSolicitud estado) {
        List<SolicitudDTO> solicitudes = solicitudService.obtenerSolicitudesPorEstado(estado);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * GET /api/solicitudes/alertas - Obtener solicitudes con alerta
     * 
     * Feature: Como funcionario quiero ver solicitudes que requieren atención
     * Scenario: Consultar solicitudes con alertas
     */
    @GetMapping("/alertas")
    public ResponseEntity<List<SolicitudDTO>> obtenerConAlertas() {
        List<SolicitudDTO> solicitudes = solicitudService.obtenerSolicitudesConAlerta();
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * GET /api/solicitudes/estadisticas - Obtener estadísticas
     * 
     * Feature: Como administrador quiero ver estadísticas de solicitudes
     * Scenario: Generar reporte estadístico
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasSolicitudesDTO> obtenerEstadisticas() {
        EstadisticasSolicitudesDTO estadisticas = solicitudService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * DELETE /api/solicitudes/{id} - Eliminar solicitud
     * 
     * Feature: Como funcionario quiero archivar una solicitud
     * Scenario: Solicitud archivada exitosamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable String id) {
        boolean eliminada = solicitudService.eliminarSolicitud(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Manejo de errores específicos
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("DATOS_INVALIDOS", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("OPERACION_INVALIDA", e.getMessage()));
    }

    private record ErrorResponse(String codigo, String mensaje) {}
}