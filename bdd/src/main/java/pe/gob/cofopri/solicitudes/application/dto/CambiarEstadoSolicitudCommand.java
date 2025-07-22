package pe.gob.cofopri.solicitudes.application.dto;

import pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Command DTO para cambiar el estado de una solicitud.
 * 
 * Encapsula la información necesaria para realizar transiciones
 * de estado en solicitudes, incluyendo auditoría del usuario.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambiarEstadoSolicitudCommand {

    @NotBlank(message = "El ID de la solicitud es obligatorio")
    private String solicitudId;

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoSolicitud nuevoEstado;

    @NotBlank(message = "El usuario que realiza el cambio es obligatorio")
    @Size(min = 2, max = 50, message = "El usuario debe tener entre 2 y 50 caracteres")
    private String usuario;

    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres")
    private String observaciones;

    /**
     * Limpia los datos de entrada
     */
    public void limpiarDatos() {
        if (solicitudId != null) {
            solicitudId = solicitudId.trim();
        }
        if (usuario != null) {
            usuario = usuario.trim();
        }
        if (observaciones != null) {
            observaciones = observaciones.trim();
            if (observaciones.isEmpty()) {
                observaciones = null;
            }
        }
    }

    /**
     * Genera observaciones automáticas si no se proporcionan
     */
    public void generarObservacionesAutomaticas() {
        if (observaciones == null || observaciones.trim().isEmpty()) {
            observaciones = String.format("Estado cambiado a %s por %s", 
                                        nuevoEstado.getDescripcion(), 
                                        usuario);
        }
    }
}