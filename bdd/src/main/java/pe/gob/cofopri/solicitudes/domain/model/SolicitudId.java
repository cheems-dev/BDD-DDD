package pe.gob.cofopri.solicitudes.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Value Object que representa el identificador único de una Solicitud en COFOPRI.
 * 
 * Sigue el formato: SOL-YYYY-NNNNNN (ejemplo: SOL-2024-000001)
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@ToString
@EqualsAndHashCode
public final class SolicitudId {

    private static final String FORMATO_PATRON = "^SOL-\\d{4}-\\d{6}$";
    private static final String PREFIJO = "SOL-";

    @NotBlank(message = "El código de solicitud no puede estar vacío")
    @Pattern(regexp = FORMATO_PATRON, message = "El formato del código debe ser SOL-YYYY-NNNNNN")
    private final String codigo;

    private SolicitudId(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de solicitud no puede estar vacío");
        }
        
        if (!codigo.matches(FORMATO_PATRON)) {
            throw new IllegalArgumentException("El formato del código debe ser SOL-YYYY-NNNNNN (ejemplo: SOL-2024-000001)");
        }
        
        this.codigo = codigo.trim().toUpperCase();
    }

    /**
     * Factory method para crear un SolicitudId desde un código
     * 
     * @param codigo Código en formato SOL-YYYY-NNNNNN
     * @return Nueva instancia de SolicitudId
     * @throws IllegalArgumentException si el formato es inválido
     */
    public static SolicitudId of(String codigo) {
        return new SolicitudId(codigo);
    }

    /**
     * Factory method para generar un nuevo SolicitudId
     * 
     * @param year Año de la solicitud
     * @param numero Número secuencial (6 dígitos)
     * @return Nueva instancia de SolicitudId
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    public static SolicitudId generar(int year, long numero) {
        if (year < 2020 || year > 2100) {
            throw new IllegalArgumentException("El año debe estar entre 2020 y 2100");
        }
        
        if (numero < 1 || numero > 999999) {
            throw new IllegalArgumentException("El número debe estar entre 1 y 999999");
        }
        
        String codigo = String.format("%s%d-%06d", PREFIJO, year, numero);
        return new SolicitudId(codigo);
    }

    /**
     * Extrae el año del código de solicitud
     * 
     * @return El año de la solicitud
     */
    public int getYear() {
        String[] partes = codigo.split("-");
        return Integer.parseInt(partes[1]);
    }

    /**
     * Extrae el número secuencial del código de solicitud
     * 
     * @return El número secuencial
     */
    public long getNumero() {
        String[] partes = codigo.split("-");
        return Long.parseLong(partes[2]);
    }

    /**
     * Verifica si el SolicitudId es válido
     * 
     * @return true si cumple con el formato y reglas de validación
     */
    public boolean esValido() {
        try {
            return codigo.matches(FORMATO_PATRON) && 
                   getYear() >= 2020 && getYear() <= 2100 &&
                   getNumero() >= 1 && getNumero() <= 999999;
        } catch (Exception e) {
            return false;
        }
    }
}