package pe.gob.cofopri.ciudadanos.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Value Object que representa un DNI peruano válido.
 * 
 * Encapsula las reglas de validación específicas del DNI peruano
 * y garantiza que solo se puedan crear instancias con valores válidos.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@ToString
@EqualsAndHashCode
public final class DNI {

    private static final String PATRON_DNI = "^[0-9]{8}$";

    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = PATRON_DNI, message = "El DNI debe tener exactamente 8 dígitos numéricos")
    private final String numero;

    private DNI(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        }
        
        String dniLimpio = numero.trim();
        
        if (!dniLimpio.matches(PATRON_DNI)) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos");
        }
        
        // Validación adicional: el DNI no puede ser todos ceros o números repetidos obvios
        if (dniLimpio.equals("00000000") || dniLimpio.equals("11111111") || 
            dniLimpio.equals("22222222") || dniLimpio.equals("33333333") ||
            dniLimpio.equals("44444444") || dniLimpio.equals("55555555") ||
            dniLimpio.equals("66666666") || dniLimpio.equals("77777777") ||
            dniLimpio.equals("88888888") || dniLimpio.equals("99999999") ||
            dniLimpio.equals("12345678") || dniLimpio.equals("87654321")) {
            throw new IllegalArgumentException("El DNI proporcionado no es válido");
        }
        
        this.numero = dniLimpio;
    }

    /**
     * Factory method para crear un DNI desde un string
     * 
     * @param numero String con el número de DNI
     * @return Nueva instancia de DNI
     * @throws IllegalArgumentException si el DNI no es válido
     */
    public static DNI of(String numero) {
        return new DNI(numero);
    }

    /**
     * Verifica si el DNI es válido según las reglas peruanas
     * 
     * @param numero String a validar
     * @return true si es un DNI válido
     */
    public static boolean esValido(String numero) {
        try {
            of(numero);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Obtiene el dígito verificador del DNI (último dígito)
     * 
     * @return Dígito verificador
     */
    public int getDigitoVerificador() {
        return Integer.parseInt(numero.substring(7, 8));
    }

    /**
     * Obtiene los primeros 7 dígitos del DNI
     * 
     * @return Primeros 7 dígitos como string
     */
    public String getPrimerosSieteDigitos() {
        return numero.substring(0, 7);
    }

    /**
     * Genera una versión enmascarada del DNI para mostrar en logs o UI
     * Formato: 12****78
     * 
     * @return DNI enmascarado
     */
    public String getNumeroEnmascarado() {
        return numero.substring(0, 2) + "****" + numero.substring(6);
    }

    /**
     * Verifica si el DNI corresponde a un rango de años específico
     * (estimación muy básica basada en los primeros dígitos)
     * 
     * @return true si probablemente corresponde a una persona adulta
     */
    public boolean esProbablementeAdulto() {
        // DNIs que empiezan con números más bajos suelen ser más antiguos
        int primerosDosDigitos = Integer.parseInt(numero.substring(0, 2));
        return primerosDosDigitos <= 60; // Heurística simple
    }

    /**
     * Formatea el DNI para presentación
     * Formato: 12.345.678
     * 
     * @return DNI formateado con puntos
     */
    public String getNumeroFormateado() {
        return numero.substring(0, 2) + "." + 
               numero.substring(2, 5) + "." + 
               numero.substring(5);
    }
}