package pe.gob.cofopri.solicitudes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

/**
 * Command DTO para actualizar una solicitud existente.
 * 
 * Permite modificar ciertos campos de una solicitud sin cambiar
 * su información fundamental (DNI, tipo, etc.).
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarSolicitudCommand {

    @NotBlank(message = "El ID de la solicitud es obligatorio")
    private String solicitudId;

    @Min(value = 1, message = "La prioridad mínima es 1 (Normal)")
    @Max(value = 3, message = "La prioridad máxima es 3 (Urgente)")
    private Integer nuevaPrioridad;

    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres")
    private String nuevasObservaciones;

    /**
     * Limpia los datos de entrada
     */
    public void limpiarDatos() {
        if (solicitudId != null) {
            solicitudId = solicitudId.trim();
        }
        if (nuevasObservaciones != null) {
            nuevasObservaciones = nuevasObservaciones.trim();
            if (nuevasObservaciones.isEmpty()) {
                nuevasObservaciones = null;
            }
        }
    }

    /**
     * Verifica si hay cambios para aplicar
     */
    public boolean tieneActualizaciones() {
        return nuevaPrioridad != null || nuevasObservaciones != null;
    }
}