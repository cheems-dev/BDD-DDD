package pe.gob.cofopri.ciudadanos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Entidad de dominio que representa un Ciudadano en el sistema COFOPRI.
 * 
 * Un ciudadano es una persona natural que puede solicitar servicios
 * de titulación predial, con información verificada en RENIEC.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@Builder
@AllArgsConstructor
public class Ciudadano {

    @NotNull(message = "El DNI no puede ser nulo")
    private final DNI dni;

    @NotBlank(message = "Los nombres no pueden estar vacíos")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]{2,50}$", 
             message = "Los nombres solo pueden contener letras, tildes, ñ y espacios")
    private final String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]{2,50}$", 
             message = "Los apellidos solo pueden contener letras, tildes, ñ y espacios")
    private final String apellidos;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private final LocalDate fechaNacimiento;

    @NotNull(message = "El estado civil no puede ser nulo")
    private final EstadoCivil estadoCivil;

    @NotNull(message = "El sexo no puede ser nulo")
    private final Sexo sexo;

    private final String direccion;

    private final String distrito;

    private final String provincia;

    private final String departamento;

    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private final String telefono;

    @Email(message = "El email debe tener formato válido")
    private final String email;

    @NotNull(message = "El estado de verificación no puede ser nulo")
    private final EstadoVerificacion estadoVerificacion;

    @NotNull(message = "La fecha de registro no puede ser nula")
    private final LocalDateTime fechaRegistro;

    private final LocalDateTime fechaUltimaActualizacion;

    private final LocalDateTime fechaUltimaVerificacion;

    private final String observaciones;

    /**
     * Enumeration para el estado civil
     */
    public enum EstadoCivil {
        SOLTERO("Soltero(a)"),
        CASADO("Casado(a)"),
        VIUDO("Viudo(a)"),
        DIVORCIADO("Divorciado(a)"),
        UNION_DE_HECHO("Unión de Hecho");

        private final String descripcion;

        EstadoCivil(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Enumeration para el sexo
     */
    public enum Sexo {
        MASCULINO("M", "Masculino"),
        FEMENINO("F", "Femenino");

        private final String codigo;
        private final String descripcion;

        Sexo(String codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public static Sexo fromCodigo(String codigo) {
            for (Sexo sexo : values()) {
                if (sexo.codigo.equals(codigo)) {
                    return sexo;
                }
            }
            throw new IllegalArgumentException("Código de sexo inválido: " + codigo);
        }
    }

    /**
     * Enumeration para el estado de verificación con RENIEC
     */
    public enum EstadoVerificacion {
        PENDIENTE("Pendiente - No verificado en RENIEC"),
        VERIFICADO("Verificado - Datos confirmados en RENIEC"),
        ERROR_VERIFICACION("Error - Datos no coinciden con RENIEC"),
        BLOQUEADO("Bloqueado - Problemas en verificación");

        private final String descripcion;

        EstadoVerificacion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public boolean esValido() {
            return this == VERIFICADO;
        }
    }

    /**
     * Factory method para crear un nuevo ciudadano
     * 
     * @param dni DNI del ciudadano
     * @param nombres Nombres del ciudadano
     * @param apellidos Apellidos del ciudadano
     * @param fechaNacimiento Fecha de nacimiento
     * @param estadoCivil Estado civil
     * @param sexo Sexo
     * @return Nueva instancia de Ciudadano
     */
    public static Ciudadano crear(DNI dni, String nombres, String apellidos,
                                 LocalDate fechaNacimiento, EstadoCivil estadoCivil, Sexo sexo) {
        
        // Validación de edad mínima (18 años)
        if (Period.between(fechaNacimiento, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("El ciudadano debe ser mayor de edad (18 años)");
        }

        return Ciudadano.builder()
                .dni(dni)
                .nombres(nombres.trim().toUpperCase())
                .apellidos(apellidos.trim().toUpperCase())
                .fechaNacimiento(fechaNacimiento)
                .estadoCivil(estadoCivil)
                .sexo(sexo)
                .estadoVerificacion(EstadoVerificacion.PENDIENTE)
                .fechaRegistro(LocalDateTime.now())
                .build();
    }

    /**
     * Actualiza los datos de contacto del ciudadano
     * 
     * @param nuevaDireccion Nueva dirección
     * @param nuevoTelefono Nuevo teléfono
     * @param nuevoEmail Nuevo email
     * @return Nueva instancia con datos actualizados
     */
    public Ciudadano actualizarDatosContacto(String nuevaDireccion, String nuevoTelefono, String nuevoEmail) {
        return Ciudadano.builder()
                .dni(this.dni)
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .fechaNacimiento(this.fechaNacimiento)
                .estadoCivil(this.estadoCivil)
                .sexo(this.sexo)
                .direccion(nuevaDireccion != null ? nuevaDireccion.trim() : null)
                .distrito(this.distrito)
                .provincia(this.provincia)
                .departamento(this.departamento)
                .telefono(nuevoTelefono != null ? nuevoTelefono.trim() : null)
                .email(nuevoEmail != null ? nuevoEmail.trim().toLowerCase() : null)
                .estadoVerificacion(this.estadoVerificacion)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .fechaUltimaVerificacion(this.fechaUltimaVerificacion)
                .observaciones(this.observaciones)
                .build();
    }

    /**
     * Actualiza la ubicación geográfica del ciudadano
     * 
     * @param distrito Distrito
     * @param provincia Provincia
     * @param departamento Departamento
     * @return Nueva instancia con ubicación actualizada
     */
    public Ciudadano actualizarUbicacion(String distrito, String provincia, String departamento) {
        return Ciudadano.builder()
                .dni(this.dni)
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .fechaNacimiento(this.fechaNacimiento)
                .estadoCivil(this.estadoCivil)
                .sexo(this.sexo)
                .direccion(this.direccion)
                .distrito(distrito != null ? distrito.trim().toUpperCase() : null)
                .provincia(provincia != null ? provincia.trim().toUpperCase() : null)
                .departamento(departamento != null ? departamento.trim().toUpperCase() : null)
                .telefono(this.telefono)
                .email(this.email)
                .estadoVerificacion(this.estadoVerificacion)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .fechaUltimaVerificacion(this.fechaUltimaVerificacion)
                .observaciones(this.observaciones)
                .build();
    }

    /**
     * Marca el ciudadano como verificado en RENIEC
     * 
     * @param observaciones Observaciones de la verificación
     * @return Nueva instancia con estado verificado
     */
    public Ciudadano marcarComoVerificado(String observaciones) {
        return Ciudadano.builder()
                .dni(this.dni)
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .fechaNacimiento(this.fechaNacimiento)
                .estadoCivil(this.estadoCivil)
                .sexo(this.sexo)
                .direccion(this.direccion)
                .distrito(this.distrito)
                .provincia(this.provincia)
                .departamento(this.departamento)
                .telefono(this.telefono)
                .email(this.email)
                .estadoVerificacion(EstadoVerificacion.VERIFICADO)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .fechaUltimaVerificacion(LocalDateTime.now())
                .observaciones(observaciones)
                .build();
    }

    /**
     * Marca el ciudadano con error de verificación
     * 
     * @param motivoError Motivo del error de verificación
     * @return Nueva instancia con estado de error
     */
    public Ciudadano marcarConErrorVerificacion(String motivoError) {
        return Ciudadano.builder()
                .dni(this.dni)
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .fechaNacimiento(this.fechaNacimiento)
                .estadoCivil(this.estadoCivil)
                .sexo(this.sexo)
                .direccion(this.direccion)
                .distrito(this.distrito)
                .provincia(this.provincia)
                .departamento(this.departamento)
                .telefono(this.telefono)
                .email(this.email)
                .estadoVerificacion(EstadoVerificacion.ERROR_VERIFICACION)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .fechaUltimaVerificacion(LocalDateTime.now())
                .observaciones(motivoError)
                .build();
    }

    /**
     * Actualiza las observaciones del ciudadano
     * 
     * @param nuevasObservaciones Nuevas observaciones
     * @return Nueva instancia con observaciones actualizadas
     */
    public Ciudadano actualizarObservaciones(String nuevasObservaciones) {
        return Ciudadano.builder()
                .dni(this.dni)
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .fechaNacimiento(this.fechaNacimiento)
                .estadoCivil(this.estadoCivil)
                .sexo(this.sexo)
                .direccion(this.direccion)
                .distrito(this.distrito)
                .provincia(this.provincia)
                .departamento(this.departamento)
                .telefono(this.telefono)
                .email(this.email)
                .estadoVerificacion(this.estadoVerificacion)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .fechaUltimaVerificacion(this.fechaUltimaVerificacion)
                .observaciones(nuevasObservaciones != null ? nuevasObservaciones.trim() : null)
                .build();
    }

    /**
     * Obtiene el nombre completo del ciudadano
     * 
     * @return Nombres y apellidos concatenados
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Calcula la edad actual del ciudadano
     * 
     * @return Edad en años
     */
    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    /**
     * Verifica si el ciudadano es mayor de edad
     * 
     * @return true si tiene 18 años o más
     */
    public boolean esMayorDeEdad() {
        return getEdad() >= 18;
    }

    /**
     * Verifica si el ciudadano está verificado en RENIEC
     * 
     * @return true si está verificado
     */
    public boolean estaVerificado() {
        return estadoVerificacion == EstadoVerificacion.VERIFICADO;
    }

    /**
     * Verifica si necesita reverificación (más de 90 días sin verificar)
     * 
     * @return true si necesita reverificación
     */
    public boolean necesitaReverificacion() {
        if (fechaUltimaVerificacion == null) {
            return estadoVerificacion == EstadoVerificacion.PENDIENTE;
        }
        
        return fechaUltimaVerificacion.isBefore(LocalDateTime.now().minusDays(90));
    }

    /**
     * Verifica si tiene datos de contacto completos
     * 
     * @return true si tiene teléfono o email
     */
    public boolean tieneDatosContactoCompletos() {
        return (telefono != null && !telefono.trim().isEmpty()) || 
               (email != null && !email.trim().isEmpty());
    }

    /**
     * Obtiene la dirección completa formateada
     * 
     * @return Dirección completa
     */
    public String getDireccionCompleta() {
        StringBuilder direccionCompleta = new StringBuilder();
        
        if (direccion != null && !direccion.trim().isEmpty()) {
            direccionCompleta.append(direccion);
        }
        
        if (distrito != null && !distrito.trim().isEmpty()) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(", ");
            direccionCompleta.append(distrito);
        }
        
        if (provincia != null && !provincia.trim().isEmpty()) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(", ");
            direccionCompleta.append(provincia);
        }
        
        if (departamento != null && !departamento.trim().isEmpty()) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(", ");
            direccionCompleta.append(departamento);
        }
        
        return direccionCompleta.toString();
    }

    @Override
    public String toString() {
        return String.format("Ciudadano[dni=%s, nombre=%s, verificacion=%s]", 
                dni.getNumeroEnmascarado(), getNombreCompleto(), estadoVerificacion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ciudadano ciudadano = (Ciudadano) obj;
        return dni.equals(ciudadano.dni);
    }

    @Override
    public int hashCode() {
        return dni.hashCode();
    }
}