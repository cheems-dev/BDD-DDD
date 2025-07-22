package pe.gob.cofopri.catastro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Command DTO para actualizar datos de un predio existente.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPredioCommand {

    @NotBlank(message = "El código de predio es obligatorio")
    @Pattern(regexp = "^[0-9]{6}-[0-9]{3}-[0-9]{3}-[0-9]{3}$", 
             message = "El código de predio debe tener el formato XXXXXX-XXX-XXX-XXX")
    private String codigoPredio;

    private String propietario;
    private String observaciones;
}

/**
 * Command DTO para cambiar el estado de un predio.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CambiarEstadoPredioCommand {

    @NotBlank(message = "El código de predio es obligatorio")
    @Pattern(regexp = "^[0-9]{6}-[0-9]{3}-[0-9]{3}-[0-9]{3}$", 
             message = "El código de predio debe tener el formato XXXXXX-XXX-XXX-XXX")
    private String codigoPredio;

    @NotBlank(message = "El nuevo estado es obligatorio")
    private String nuevoEstado;
}