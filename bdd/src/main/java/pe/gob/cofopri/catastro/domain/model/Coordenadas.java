package pe.gob.cofopri.catastro.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object que representa las coordenadas geográficas de un Predio
 * en el sistema catastral de COFOPRI.
 * 
 * Utiliza coordenadas UTM (Universal Transverse Mercator) para Perú.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Coordenadas {

    @NotNull(message = "La latitud no puede ser nula")
    @DecimalMin(value = "-18.5", message = "La latitud debe estar dentro del territorio peruano")
    @DecimalMax(value = "0.0", message = "La latitud debe estar dentro del territorio peruano")
    private final BigDecimal latitud;

    @NotNull(message = "La longitud no puede ser nula")
    @DecimalMin(value = "-81.5", message = "La longitud debe estar dentro del territorio peruano")
    @DecimalMax(value = "-68.0", message = "La longitud debe estar dentro del territorio peruano")
    private final BigDecimal longitud;

    /**
     * Crea nuevas coordenadas a partir de valores double
     * 
     * @param latitud Latitud en grados decimales
     * @param longitud Longitud en grados decimales
     * @return Nueva instancia de Coordenadas
     * @throws IllegalArgumentException Si las coordenadas están fuera del rango válido para Perú
     */
    public static Coordenadas of(double latitud, double longitud) {
        return of(BigDecimal.valueOf(latitud), BigDecimal.valueOf(longitud));
    }

    /**
     * Crea nuevas coordenadas a partir de valores BigDecimal
     * 
     * @param latitud Latitud en grados decimales
     * @param longitud Longitud en grados decimales
     * @return Nueva instancia de Coordenadas
     * @throws IllegalArgumentException Si las coordenadas están fuera del rango válido para Perú
     */
    public static Coordenadas of(BigDecimal latitud, BigDecimal longitud) {
        if (latitud == null || longitud == null) {
            throw new IllegalArgumentException("La latitud y longitud no pueden ser nulas");
        }

        // Validar rango para territorio peruano
        if (latitud.compareTo(BigDecimal.valueOf(-18.5)) < 0 || latitud.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("La latitud debe estar entre -18.5 y 0.0 grados (territorio peruano)");
        }

        if (longitud.compareTo(BigDecimal.valueOf(-81.5)) < 0 || longitud.compareTo(BigDecimal.valueOf(-68.0)) > 0) {
            throw new IllegalArgumentException("La longitud debe estar entre -81.5 y -68.0 grados (territorio peruano)");
        }

        // Redondear a 6 decimales para precisión catastral
        BigDecimal latitudRedondeada = latitud.setScale(6, RoundingMode.HALF_UP);
        BigDecimal longitudRedondeada = longitud.setScale(6, RoundingMode.HALF_UP);

        return new Coordenadas(latitudRedondeada, longitudRedondeada);
    }

    /**
     * Calcula la distancia en metros entre estas coordenadas y otras usando la fórmula de Haversine
     * 
     * @param otras Las otras coordenadas
     * @return Distancia en metros
     */
    public double calcularDistanciaEnMetros(Coordenadas otras) {
        if (otras == null) {
            throw new IllegalArgumentException("Las otras coordenadas no pueden ser nulas");
        }

        double lat1Rad = Math.toRadians(this.latitud.doubleValue());
        double lat2Rad = Math.toRadians(otras.latitud.doubleValue());
        double deltaLatRad = Math.toRadians(otras.latitud.subtract(this.latitud).doubleValue());
        double deltaLonRad = Math.toRadians(otras.longitud.subtract(this.longitud).doubleValue());

        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371000 * c; // Radio de la Tierra en metros
    }

    /**
     * Verifica si estas coordenadas están dentro de un radio específico de otras coordenadas
     * 
     * @param otras Otras coordenadas
     * @param radioEnMetros Radio en metros
     * @return true si están dentro del radio, false en caso contrario
     */
    public boolean estaEnRadio(Coordenadas otras, double radioEnMetros) {
        return calcularDistanciaEnMetros(otras) <= radioEnMetros;
    }

    @Override
    public String toString() {
        return String.format("Coordenadas(lat=%.6f, lon=%.6f)", 
                latitud.doubleValue(), longitud.doubleValue());
    }
}