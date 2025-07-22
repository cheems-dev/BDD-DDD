package pe.gob.cofopri.catastro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import java.math.BigDecimal;

/**
 * Command DTO para crear un nuevo predio.
 * 
 * Representa los datos necesarios para registrar un predio
 * en el sistema catastral de COFOPRI.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearPredioCommand {

    @NotBlank(message = "El código de predio es obligatorio")
    @Pattern(regexp = "^[0-9]{6}-[0-9]{3}-[0-9]{3}-[0-9]{3}$", 
             message = "El código de predio debe tener el formato XXXXXX-XXX-XXX-XXX")
    private String codigoPredio;

    @NotBlank(message = "El nombre del propietario es obligatorio")
    private String propietario;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-18.5", message = "La latitud debe estar dentro del territorio peruano")
    @DecimalMax(value = "0.0", message = "La latitud debe estar dentro del territorio peruano")
    private BigDecimal latitud;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-81.5", message = "La longitud debe estar dentro del territorio peruano")
    @DecimalMax(value = "-68.0", message = "La longitud debe estar dentro del territorio peruano")
    private BigDecimal longitud;

    @NotNull(message = "El área es obligatoria")
    @Positive(message = "El área debe ser un valor positivo")
    private BigDecimal area;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String observaciones;
}