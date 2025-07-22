package pe.gob.cofopri.solicitudes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad de dominio que representa una Solicitud de Titulación en COFOPRI.
 * 
 * Una solicitud es el documento principal que inicia el proceso de formalización
 * de propiedad predial urbana, conteniendo información del ciudadano, predio
 * y documentos requeridos.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@Builder
@AllArgsConstructor
public class Solicitud {

    @NotNull(message = "El ID de la solicitud no puede ser nulo")
    private final SolicitudId id;

    @NotBlank(message = "El DNI del solicitante no puede estar vacío")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener exactamente 8 dígitos")
    private final String dniSolicitante;

    @NotBlank(message = "El nombre del solicitante no puede estar vacío")
    private final String nombreSolicitante;

    @NotBlank(message = "La dirección del predio no puede estar vacía")
    private final String direccionPredio;

    @NotNull(message = "El estado de la solicitud no puede ser nulo")
    private final EstadoSolicitud estado;

    @NotNull(message = "El tipo de solicitud no puede ser nulo")
    private final TipoSolicitud tipo;

    @NotNull(message = "La fecha de registro no puede ser nula")
    private final LocalDateTime fechaRegistro;

    private final LocalDateTime fechaUltimaActualizacion;

    private final String observaciones;

    @Builder.Default
    private final List<String> documentosAdjuntos = new ArrayList<>();

    private final String numeroExpediente;

    private final Integer prioridad;

    /**
     * Enumeration que define los posibles estados de una solicitud
     */
    public enum EstadoSolicitud {
        RECIBIDA("Recibida - En mesa de partes"),
        EN_EVALUACION("En Evaluación - Revisión de documentos"),
        INSPECCION_PENDIENTE("Inspección Pendiente - Requiere visita técnica"),
        INSPECCION_REALIZADA("Inspección Realizada - Catastro completado"),
        ANALISIS_JURIDICO("Análisis Jurídico - Revisión legal"),
        TITULO_GENERADO("Título Generado - Listo para entrega"),
        ENVIADO_SUNARP("Enviado a SUNARP - En proceso de inscripción"),
        INSCRITO("Inscrito - Registrado en SUNARP"),
        TITULO_ENTREGADO("Título Entregado - Proceso completado"),
        RECHAZADA("Rechazada - No cumple requisitos"),
        ARCHIVADA("Archivada - Proceso cancelado"),
        EN_SUBSANACION("En Subsanación - Requiere documentos adicionales");

        private final String descripcion;

        EstadoSolicitud(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }

        /**
         * Verifica si es un estado terminal (no permite más transiciones)
         */
        public boolean esTerminal() {
            return this == TITULO_ENTREGADO || this == RECHAZADA || this == ARCHIVADA;
        }

        /**
         * Verifica si el estado requiere acción del ciudadano
         */
        public boolean requiereAccionCiudadano() {
            return this == EN_SUBSANACION;
        }
    }

    /**
     * Enumeration que define los tipos de solicitud disponibles
     */
    public enum TipoSolicitud {
        TITULACION_INDIVIDUAL("Titulación Individual"),
        TITULACION_COLECTIVA("Titulación Colectiva"),
        ACTUALIZACION_CATASTRAL("Actualización Catastral"),
        SUBDIVISION("Subdivisión de Predio"),
        FUSION("Fusión de Predios");

        private final String descripcion;

        TipoSolicitud(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Factory method para crear una nueva solicitud
     * 
     * @param id Identificador de la solicitud
     * @param dniSolicitante DNI del solicitante (8 dígitos)
     * @param nombreSolicitante Nombre completo del solicitante
     * @param direccionPredio Dirección del predio a titular
     * @param tipo Tipo de solicitud
     * @param observaciones Observaciones iniciales (opcional)
     * @return Nueva instancia de Solicitud
     */
    public static Solicitud crear(SolicitudId id, String dniSolicitante, String nombreSolicitante,
                                 String direccionPredio, TipoSolicitud tipo, String observaciones) {
        
        // Validación de DNI peruano
        if (!dniSolicitante.matches("^[0-9]{8}$")) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos");
        }

        return Solicitud.builder()
                .id(id)
                .dniSolicitante(dniSolicitante.trim())
                .nombreSolicitante(nombreSolicitante.trim())
                .direccionPredio(direccionPredio.trim())
                .tipo(tipo)
                .estado(EstadoSolicitud.RECIBIDA)
                .fechaRegistro(LocalDateTime.now())
                .observaciones(observaciones != null ? observaciones.trim() : null)
                .prioridad(1) // Prioridad normal por defecto
                .build();
    }

    /**
     * Cambia el estado de la solicitud validando transiciones permitidas
     * 
     * @param nuevoEstado Nuevo estado de la solicitud
     * @param observaciones Observaciones del cambio de estado
     * @return Nueva instancia de la solicitud con el estado actualizado
     * @throws IllegalStateException si la transición no es permitida
     */
    public Solicitud cambiarEstado(EstadoSolicitud nuevoEstado, String observaciones) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        if (this.estado.esTerminal()) {
            throw new IllegalStateException(String.format(
                "No se puede cambiar el estado desde %s porque es un estado terminal", this.estado));
        }

        if (!esTransicionValida(this.estado, nuevoEstado)) {
            throw new IllegalStateException(String.format(
                "Transición no permitida de %s a %s", this.estado, nuevoEstado));
        }

        return Solicitud.builder()
                .id(this.id)
                .dniSolicitante(this.dniSolicitante)
                .nombreSolicitante(this.nombreSolicitante)
                .direccionPredio(this.direccionPredio)
                .tipo(this.tipo)
                .estado(nuevoEstado)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .observaciones(observaciones != null ? observaciones.trim() : this.observaciones)
                .documentosAdjuntos(this.documentosAdjuntos)
                .numeroExpediente(this.numeroExpediente)
                .prioridad(this.prioridad)
                .build();
    }

    /**
     * Agrega un documento a la solicitud
     * 
     * @param nombreDocumento Nombre del documento adjunto
     * @return Nueva instancia con el documento agregado
     */
    public Solicitud agregarDocumento(String nombreDocumento) {
        if (nombreDocumento == null || nombreDocumento.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del documento no puede estar vacío");
        }

        List<String> nuevosDocumentos = new ArrayList<>(this.documentosAdjuntos);
        nuevosDocumentos.add(nombreDocumento.trim());

        return Solicitud.builder()
                .id(this.id)
                .dniSolicitante(this.dniSolicitante)
                .nombreSolicitante(this.nombreSolicitante)
                .direccionPredio(this.direccionPredio)
                .tipo(this.tipo)
                .estado(this.estado)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .observaciones(this.observaciones)
                .documentosAdjuntos(nuevosDocumentos)
                .numeroExpediente(this.numeroExpediente)
                .prioridad(this.prioridad)
                .build();
    }

    /**
     * Asigna un número de expediente a la solicitud
     * 
     * @param numeroExpediente Número de expediente asignado
     * @return Nueva instancia con el número de expediente
     */
    public Solicitud asignarExpediente(String numeroExpediente) {
        if (numeroExpediente == null || numeroExpediente.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de expediente no puede estar vacío");
        }

        return Solicitud.builder()
                .id(this.id)
                .dniSolicitante(this.dniSolicitante)
                .nombreSolicitante(this.nombreSolicitante)
                .direccionPredio(this.direccionPredio)
                .tipo(this.tipo)
                .estado(this.estado)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .observaciones(this.observaciones)
                .documentosAdjuntos(this.documentosAdjuntos)
                .numeroExpediente(numeroExpediente.trim())
                .prioridad(this.prioridad)
                .build();
    }

    /**
     * Cambia la prioridad de la solicitud
     * 
     * @param nuevaPrioridad Prioridad (1=Normal, 2=Alta, 3=Urgente)
     * @return Nueva instancia con la prioridad actualizada
     */
    public Solicitud cambiarPrioridad(int nuevaPrioridad) {
        if (nuevaPrioridad < 1 || nuevaPrioridad > 3) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 (Normal) y 3 (Urgente)");
        }

        return Solicitud.builder()
                .id(this.id)
                .dniSolicitante(this.dniSolicitante)
                .nombreSolicitante(this.nombreSolicitante)
                .direccionPredio(this.direccionPredio)
                .tipo(this.tipo)
                .estado(this.estado)
                .fechaRegistro(this.fechaRegistro)
                .fechaUltimaActualizacion(LocalDateTime.now())
                .observaciones(this.observaciones)
                .documentosAdjuntos(this.documentosAdjuntos)
                .numeroExpediente(this.numeroExpediente)
                .prioridad(nuevaPrioridad)
                .build();
    }

    /**
     * Verifica si la solicitud está en proceso activo
     * 
     * @return true si no está en estado terminal
     */
    public boolean estaEnProceso() {
        return !estado.esTerminal();
    }

    /**
     * Calcula los días transcurridos desde el registro
     * 
     * @return Número de días desde el registro
     */
    public long getDiasTranscurridos() {
        return java.time.temporal.ChronoUnit.DAYS.between(
            fechaRegistro.toLocalDate(), 
            LocalDateTime.now().toLocalDate()
        );
    }

    /**
     * Valida si una transición de estado es permitida
     */
    private boolean esTransicionValida(EstadoSolicitud estadoActual, EstadoSolicitud nuevoEstado) {
        return switch (estadoActual) {
            case RECIBIDA -> nuevoEstado == EstadoSolicitud.EN_EVALUACION || 
                           nuevoEstado == EstadoSolicitud.RECHAZADA;
            
            case EN_EVALUACION -> nuevoEstado == EstadoSolicitud.INSPECCION_PENDIENTE || 
                                nuevoEstado == EstadoSolicitud.EN_SUBSANACION ||
                                nuevoEstado == EstadoSolicitud.RECHAZADA;
            
            case INSPECCION_PENDIENTE -> nuevoEstado == EstadoSolicitud.INSPECCION_REALIZADA ||
                                       nuevoEstado == EstadoSolicitud.RECHAZADA;
            
            case INSPECCION_REALIZADA -> nuevoEstado == EstadoSolicitud.ANALISIS_JURIDICO;
            
            case ANALISIS_JURIDICO -> nuevoEstado == EstadoSolicitud.TITULO_GENERADO ||
                                    nuevoEstado == EstadoSolicitud.RECHAZADA;
            
            case TITULO_GENERADO -> nuevoEstado == EstadoSolicitud.ENVIADO_SUNARP;
            
            case ENVIADO_SUNARP -> nuevoEstado == EstadoSolicitud.INSCRITO ||
                                 nuevoEstado == EstadoSolicitud.RECHAZADA;
            
            case INSCRITO -> nuevoEstado == EstadoSolicitud.TITULO_ENTREGADO;
            
            case EN_SUBSANACION -> nuevoEstado == EstadoSolicitud.EN_EVALUACION ||
                                 nuevoEstado == EstadoSolicitud.ARCHIVADA;
            
            default -> false;
        };
    }

    @Override
    public String toString() {
        return String.format("Solicitud[id=%s, solicitante=%s, estado=%s, tipo=%s]", 
                id.getCodigo(), nombreSolicitante, estado, tipo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Solicitud solicitud = (Solicitud) obj;
        return id.equals(solicitud.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}