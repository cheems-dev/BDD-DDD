package pe.gob.cofopri.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA Entity para persistir ciudadanos en la base de datos.
 */
@Entity
@Table(name = "ciudadanos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CiudadanoEntity {

    @Id
    @Column(name = "dni", nullable = false, unique = true, length = 8)
    private String dni;

    @Column(name = "nombres", nullable = false, length = 50)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 50)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil", nullable = false)
    private EstadoCivil estadoCivil;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private Sexo sexo;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "distrito", length = 50)
    private String distrito;

    @Column(name = "provincia", length = 50)
    private String provincia;

    @Column(name = "departamento", length = 50)
    private String departamento;

    @Column(name = "telefono", length = 9)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_verificacion", nullable = false)
    private EstadoVerificacion estadoVerificacion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "fecha_ultima_verificacion")
    private LocalDateTime fechaUltimaVerificacion;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    public enum EstadoCivil {
        SOLTERO, CASADO, VIUDO, DIVORCIADO, UNION_DE_HECHO
    }

    public enum Sexo {
        MASCULINO, FEMENINO
    }

    public enum EstadoVerificacion {
        PENDIENTE, VERIFICADO, ERROR_VERIFICACION, BLOQUEADO
    }
}