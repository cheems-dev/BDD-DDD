package pe.gob.cofopri.solicitudes.application.dto;

import pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Command DTO para crear una nueva solicitud de titulación.
 * 
 * Encapsula todos los datos necesarios para crear una solicitud,
 * incluyendo validaciones de entrada.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearSolicitudCommand {

    @NotBlank(message = "El DNI del solicitante es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    private String dniSolicitante;

    @NotBlank(message = "El nombre del solicitante es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$", 
             message = "El nombre solo puede contener letras, tildes, ñ y espacios")
    private String nombreSolicitante;

    @NotBlank(message = "La dirección del predio es obligatoria")
    @Size(min = 10, max = 200, message = "La dirección debe tener entre 10 y 200 caracteres")
    private String direccionPredio;

    @NotNull(message = "El tipo de solicitud es obligatorio")
    private TipoSolicitud tipo;

    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres")
    private String observaciones;

    /**
     * Limpia y valida los datos de entrada
     */
    public void limpiarDatos() {
        if (dniSolicitante != null) {
            dniSolicitante = dniSolicitante.trim();
        }
        if (nombreSolicitante != null) {
            nombreSolicitante = nombreSolicitante.trim();
        }
        if (direccionPredio != null) {
            direccionPredio = direccionPredio.trim();
        }
        if (observaciones != null) {
            observaciones = observaciones.trim();
            if (observaciones.isEmpty()) {
                observaciones = null;
            }
        }
    }

    /**
     * Validaciones adicionales específicas del negocio
     */
    public void validarDatosNegocio() {
        limpiarDatos();

        // Validación adicional para titulación colectiva
        if (tipo == TipoSolicitud.TITULACION_COLECTIVA) {
            String direccionLower = direccionPredio.toLowerCase();
            if (!direccionLower.contains("mz") && 
                !direccionLower.contains("manzana") &&
                !direccionLower.contains("conjunto") &&
                !direccionLower.contains("asociación") &&
                !direccionLower.contains("agrupación")) {
                throw new IllegalArgumentException(
                    "Para titulación colectiva, la dirección debe especificar manzana, conjunto o asociación");
            }
        }

        // Validación para subdivisión
        if (tipo == TipoSolicitud.SUBDIVISION) {
            String direccionLower = direccionPredio.toLowerCase();
            if (!direccionLower.contains("lote") && !direccionLower.contains("lt")) {
                throw new IllegalArgumentException(
                    "Para subdivisión, la dirección debe especificar el lote");
            }
        }
    }
}