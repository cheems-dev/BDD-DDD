package pe.gob.cofopri.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Entity para persistir predios en la base de datos.
 */
@Entity
@Table(name = "predios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredioEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 20)
    private String id;

    @Column(name = "propietario", nullable = false, length = 100)
    private String propietario;

    @Column(name = "coordenada_x", nullable = false, precision = 10, scale = 2)
    private BigDecimal coordenadaX;

    @Column(name = "coordenada_y", nullable = false, precision = 10, scale = 2)
    private BigDecimal coordenadaY;

    @Column(name = "area", nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPredio estado;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    public enum EstadoPredio {
        ACTIVO, FORMALIZADO, SUSPENDIDO, INACTIVO, EN_REVISION
    }
}