package pe.gob.cofopri.ciudadanos.application.dto;

import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.EstadoCivil;
import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.Sexo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearCiudadanoCommand {
    @NotBlank @Pattern(regexp = "^[0-9]{8}$")
    private String dni;
    @NotBlank
    private String nombres;
    @NotBlank
    private String apellidos;
    @NotNull
    private LocalDate fechaNacimiento;
    @NotNull
    private EstadoCivil estadoCivil;
    @NotNull
    private Sexo sexo;
    private String direccion;
    private String telefono;
    private String email;
}