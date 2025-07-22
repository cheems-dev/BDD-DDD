package pe.gob.cofopri.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity para persistir solicitudes en la base de datos.
 */
@Entity
@Table(name = "solicitudes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 20)
    private String id;

    @Column(name = "dni_solicitante", nullable = false, length = 8)
    private String dniSolicitante;

    @Column(name = "nombre_solicitante", nullable = false, length = 100)
    private String nombreSolicitante;

    @Column(name = "direccion_predio", nullable = false, length = 200)
    private String direccionPredio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoSolicitud estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoSolicitud tipo;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @ElementCollection
    @CollectionTable(name = "solicitud_documentos", joinColumns = @JoinColumn(name = "solicitud_id"))
    @Column(name = "documento")
    @Builder.Default
    private List<String> documentosAdjuntos = new ArrayList<>();

    @Column(name = "numero_expediente", length = 20)
    private String numeroExpediente;

    @Column(name = "prioridad")
    private Integer prioridad;

    public enum EstadoSolicitud {
        RECIBIDA, EN_EVALUACION, INSPECCION_PENDIENTE, INSPECCION_REALIZADA,
        ANALISIS_JURIDICO, TITULO_GENERADO, ENVIADO_SUNARP, INSCRITO,
        TITULO_ENTREGADO, RECHAZADA, ARCHIVADA, EN_SUBSANACION
    }

    public enum TipoSolicitud {
        TITULACION_INDIVIDUAL, TITULACION_COLECTIVA, ACTUALIZACION_CATASTRAL,
        SUBDIVISION, FUSION
    }
}