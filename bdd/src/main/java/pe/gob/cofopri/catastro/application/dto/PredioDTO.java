package pe.gob.cofopri.catastro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para representar un predio en las respuestas de la aplicación.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredioDTO {

    private String codigoPredio;
    private String propietario;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private BigDecimal area;
    private BigDecimal areaEnHectareas;
    private String direccion;
    private String estado;
    private String descripcionEstado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimaActualizacion;
    private String observaciones;
    private String departamento;
    private String provincia;
    private String distrito;
}