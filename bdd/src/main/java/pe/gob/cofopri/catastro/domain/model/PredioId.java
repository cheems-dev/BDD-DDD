package pe.gob.cofopri.catastro.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Value Object que representa el identificador único de un Predio
 * en el sistema catastral de COFOPRI.
 * 
 * Formato: UBI-DEPARTAMENTO-PROVINCIA-DISTRITO-SECTOR-MANZANA-LOTE
 * Ejemplo: "150101-001-001-001"
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PredioId {

    @NotBlank(message = "El código de predio no puede estar vacío")
    @Pattern(regexp = "^[0-9]{6}-[0-9]{3}-[0-9]{3}-[0-9]{3}$", 
             message = "El código de predio debe tener el formato XXXXXX-XXX-XXX-XXX")
    private final String codigo;

    /**
     * Crea un nuevo PredioId a partir de un código
     * 
     * @param codigo Código del predio
     * @return Nueva instancia de PredioId
     * @throws IllegalArgumentException Si el código no es válido
     */
    public static PredioId of(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de predio no puede ser nulo o vacío");
        }
        
        if (!codigo.matches("^[0-9]{6}-[0-9]{3}-[0-9]{3}-[0-9]{3}$")) {
            throw new IllegalArgumentException("El código de predio debe tener el formato XXXXXX-XXX-XXX-XXX");
        }
        
        return new PredioId(codigo.trim());
    }

    /**
     * Extrae el código de departamento del código de predio
     * 
     * @return Código de departamento (primeros 2 dígitos)
     */
    public String getDepartamento() {
        return codigo.substring(0, 2);
    }

    /**
     * Extrae el código de provincia del código de predio
     * 
     * @return Código de provincia (dígitos 3-4)
     */
    public String getProvincia() {
        return codigo.substring(2, 4);
    }

    /**
     * Extrae el código de distrito del código de predio
     * 
     * @return Código de distrito (dígitos 5-6)
     */
    public String getDistrito() {
        return codigo.substring(4, 6);
    }

    @Override
    public String toString() {
        return codigo;
    }
}