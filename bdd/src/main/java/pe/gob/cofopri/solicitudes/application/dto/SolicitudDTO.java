package pe.gob.cofopri.solicitudes.application.dto;

import pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferir datos de Solicitud entre capas de la aplicación.
 * 
 * Representa la información completa de una solicitud de titulación
 * incluyendo datos calculados y de estado.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitudDTO {

    @NotBlank(message = "El ID de la solicitud no puede estar vacío")
    private String id;

    @NotBlank(message = "El DNI del solicitante no puede estar vacío")
    private String dniSolicitante;

    @NotBlank(message = "El nombre del solicitante no puede estar vacío")
    private String nombreSolicitante;

    @NotBlank(message = "La dirección del predio no puede estar vacía")
    private String direccionPredio;

    @NotNull(message = "El estado no puede ser nulo")
    private EstadoSolicitud estado;

    @NotNull(message = "El tipo no puede ser nulo")
    private TipoSolicitud tipo;

    @NotNull(message = "La fecha de registro no puede ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaUltimaActualizacion;

    private String observaciones;

    @Builder.Default
    private List<String> documentosAdjuntos = List.of();

    private String numeroExpediente;

    private Integer prioridad;

    // Campos calculados
    private Long diasTranscurridos;

    private Boolean estaEnProceso;

    private String mensajeAlerta;

    private Boolean tieneDocumentosMinimos;

    // Información adicional para la UI
    private String estadoDescripcion;

    private String tipoDescripcion;

    private String prioridadDescripcion;

    /**
     * Obtiene la descripción del estado
     */
    public String getEstadoDescripcion() {
        return estado != null ? estado.getDescripcion() : null;
    }

    /**
     * Obtiene la descripción del tipo
     */
    public String getTipoDescripcion() {
        return tipo != null ? tipo.getDescripcion() : null;
    }

    /**
     * Obtiene la descripción de la prioridad
     */
    public String getPrioridadDescripcion() {
        if (prioridad == null) return "No asignada";
        
        return switch (prioridad) {
            case 1 -> "Normal";
            case 2 -> "Alta";
            case 3 -> "Urgente";
            default -> "Desconocida";
        };
    }

    /**
     * Verifica si la solicitud tiene alertas
     */
    public Boolean tieneAlertas() {
        return mensajeAlerta != null && !mensajeAlerta.trim().isEmpty();
    }

    /**
     * Obtiene el número de documentos adjuntos
     */
    public Integer getNumeroDocumentos() {
        return documentosAdjuntos != null ? documentosAdjuntos.size() : 0;
    }

    /**
     * Verifica si la solicitud está en estado terminal
     */
    public Boolean esEstadoTerminal() {
        return estado != null && estado.esTerminal();
    }

    /**
     * Verifica si requiere acción del ciudadano
     */
    public Boolean requiereAccionCiudadano() {
        return estado != null && estado.requiereAccionCiudadano();
    }
}