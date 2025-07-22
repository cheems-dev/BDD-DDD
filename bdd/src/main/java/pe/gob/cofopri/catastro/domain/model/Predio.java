package pe.gob.cofopri.catastro.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un Predio en el sistema catastral de COFOPRI.
 * 
 * Un predio es una unidad catastral que representa una porción de terreno
 * con características específicas de ubicación, área y propietario.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@Builder
@AllArgsConstructor
public class Predio {

    @NotNull(message = "El ID del predio no puede ser nulo")
    private final PredioId id;

    @NotBlank(message = "El nombre del propietario no puede estar vacío")
    private final String propietario;

    @NotNull(message = "Las coordenadas del predio no pueden ser nulas")
    private final Coordenadas coordenadas;

    @NotNull(message = "El área del predio no puede ser nula")
    @Positive(message = "El área del predio debe ser positiva")
    private final BigDecimal area;

    @NotBlank(message = "La dirección del predio no puede estar vacía")
    private final String direccion;

    @NotNull(message = "El estado del predio no puede ser nulo")
    private final EstadoPredio estado;

    @NotNull(message = "La fecha de registro no puede ser nula")
    private final LocalDateTime fechaRegistro;

    private final LocalDateTime fechaUltimaActualizacion;

    private final String observaciones;

    /**
     * Enumeration que define los posibles estados de un predio
     */
    public enum EstadoPredio {
        ACTIVO("Activo - En proceso de formalización"),
        FORMALIZADO("Formalizado - Título otorgado"),
        SUSPENDIDO("Suspendido - Proceso detenido temporalmente"),
        INACTIVO("Inactivo - Proceso cancelado"),
        EN_REVISION("En Revisión - Requiere verificación adicional");

        private final String descripcion;

        EstadoPredio(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Factory method para crear un nuevo predio
     * 
     * @param id Identificador del predio
     * @param propietario Nombre del propietario
     * @param coordenadas Coordenadas geográficas
     * @param area Área en metros cuadrados
     * @param direccion Dirección del predio
     * @param observaciones Observaciones adicionales (opcional)
     * @return Nueva instancia de Predio
     */
    public static Predio crear(PredioId id, String propietario, Coordenadas coordenadas, 
                              BigDecimal area, String direccion, String observaciones) {
        return Predio.builder()
                .id(id)
                .propietario(propietario.trim())
                .coordenadas(coordenadas)
                .area(area)
                .direccion(direccion.trim())
                .estado(EstadoPredio.ACTIVO)
                .fechaRegistro(LocalDateTime.now())
                .observaciones(observaciones != null ? observaciones.trim() : null)
                .build();
    }

    /**
     * Actualiza el propietario del predio
     * 
     * @param nuevoPropietario Nuevo nombre del propietario
     * @return Nueva instancia del predio con el propietario actualizado
     */
    public Predio actualizarPropietario(String nuevoPropietario) {
        if (nuevoPropietario == null || nuevoPropietario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del propietario no puede estar vacío");
        }

        return Predio.builder()
                .id(this.id)
                .propietario(nuevoPropietario.trim())
                .coordenadas(this.coordenadas)
                .area(this.area)
                .direccion(this.direccion)
                .estado(this.estado)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .observaciones(this.observaciones)
                .build();
    }

    /**
     * Cambia el estado del predio
     * 
     * @param nuevoEstado Nuevo estado del predio
     * @return Nueva instancia del predio con el estado actualizado
     */
    public Predio cambiarEstado(EstadoPredio nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado del predio no puede ser nulo");
        }

        return Predio.builder()
                .id(this.id)
                .propietario(this.propietario)
                .coordenadas(this.coordenadas)
                .area(this.area)
                .direccion(this.direccion)
                .estado(nuevoEstado)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .observaciones(this.observaciones)
                .build();
    }

    /**
     * Actualiza las observaciones del predio
     * 
     * @param nuevasObservaciones Nuevas observaciones
     * @return Nueva instancia del predio con las observaciones actualizadas
     */
    public Predio actualizarObservaciones(String nuevasObservaciones) {
        return Predio.builder()
                .id(this.id)
                .propietario(this.propietario)
                .coordenadas(this.coordenadas)
                .area(this.area)
                .direccion(this.direccion)
                .estado(this.estado)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .observaciones(nuevasObservaciones != null ? nuevasObservaciones.trim() : null)
                .build();
    }

    /**
     * Verifica si el predio está activo
     * 
     * @return true si el estado es ACTIVO o EN_REVISION
     */
    public boolean estaActivo() {
        return estado == EstadoPredio.ACTIVO || estado == EstadoPredio.EN_REVISION;
    }

    /**
     * Verifica si el predio está formalizado
     * 
     * @return true si el estado es FORMALIZADO
     */
    public boolean estaFormalizado() {
        return estado == EstadoPredio.FORMALIZADO;
    }

    /**
     * Calcula el área en hectáreas
     * 
     * @return Área en hectáreas
     */
    public BigDecimal getAreaEnHectareas() {
        return area.divide(BigDecimal.valueOf(10000), 4, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return String.format("Predio[id=%s, propietario=%s, area=%.2fm², estado=%s]", 
                id.getCodigo(), propietario, area, estado);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Predio predio = (Predio) obj;
        return id.equals(predio.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}