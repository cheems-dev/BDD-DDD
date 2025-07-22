package pe.gob.cofopri.ciudadanos.application.dto;

import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.EstadoCivil;
import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.Sexo;
import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.EstadoVerificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CiudadanoDTO {
    private String dni;
    private String nombres;
    private String apellidos;
    private String nombreCompleto;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private Integer edad;
    private EstadoCivil estadoCivil;
    private Sexo sexo;
    private String direccion;
    private String direccionCompleta;
    private String telefono;
    private String email;
    private EstadoVerificacion estadoVerificacion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;
    private Boolean estaVerificado;
    private Boolean necesitaReverificacion;
}