package pe.gob.cofopri.infrastructure.repository.jpa;

import pe.gob.cofopri.infrastructure.entity.SolicitudEntity;
import pe.gob.cofopri.infrastructure.entity.SolicitudEntity.EstadoSolicitud;
import pe.gob.cofopri.infrastructure.entity.SolicitudEntity.TipoSolicitud;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud;
import pe.gob.cofopri.solicitudes.domain.model.SolicitudId;
import pe.gob.cofopri.solicitudes.domain.repository.SolicitudRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación JPA del repository de Solicitud.
 */

@Repository
@RequiredArgsConstructor
public class SolicitudJpaRepository implements SolicitudRepository {

    private final SolicitudJpaRepositoryInterface jpaRepository;

    @Override
    public Solicitud save(Solicitud solicitud) {
        SolicitudEntity entity = toEntity(solicitud);
        SolicitudEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Solicitud> findById(SolicitudId id) {
        return jpaRepository.findById(id.getCodigo()).map(this::toDomain);
    }

    @Override
    public List<Solicitud> findByDniSolicitante(String dni) {
        return jpaRepository.findByDniSolicitante(dni)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Solicitud> findByEstado(pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud estado) {
        EstadoSolicitud estadoEntity = EstadoSolicitud.valueOf(estado.name());
        return jpaRepository.findByEstado(estadoEntity)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Solicitud> findByTipo(pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud tipo) {
        TipoSolicitud tipoEntity = TipoSolicitud.valueOf(tipo.name());
        return jpaRepository.findByTipo(tipoEntity)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Solicitud> findByFechaRegistroBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return jpaRepository.findByFechaRegistroBetween(fechaInicio, fechaFin)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Solicitud> findByNumeroExpediente(String numeroExpediente) {
        return jpaRepository.findByNumeroExpediente(numeroExpediente).map(this::toDomain);
    }

    @Override
    public List<Solicitud> findByDireccionPredioContaining(String direccion) {
        return jpaRepository.findByDireccionPredioContaining(direccion)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Solicitud> findByPrioridad(Integer prioridad) {
        return jpaRepository.findByPrioridad(prioridad)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Solicitud> findByEstadoAndPrioridadOrderByFechaRegistroAsc(
            pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud estado, Integer prioridad) {
        EstadoSolicitud estadoEntity = EstadoSolicitud.valueOf(estado.name());
        return jpaRepository.findByEstadoAndPrioridadOrderByFechaRegistroAsc(estadoEntity, prioridad)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Solicitud> findAll() {
        return jpaRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Solicitud> findAll(int page, int size) {
        return jpaRepository.findAll()
                .stream().skip((long) page * size).limit(size)
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByEstado(pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud estado) {
        EstadoSolicitud estadoEntity = EstadoSolicitud.valueOf(estado.name());
        return jpaRepository.countByEstado(estadoEntity);
    }

    @Override
    public long countByDniSolicitante(String dni) {
        return jpaRepository.countByDniSolicitante(dni);
    }

    @Override
    public boolean deleteById(SolicitudId id) {
        if (jpaRepository.existsById(id.getCodigo())) {
            jpaRepository.deleteById(id.getCodigo());
            return true;
        }
        return false;
    }

    @Override
    public boolean existsById(SolicitudId id) {
        return jpaRepository.existsById(id.getCodigo());
    }

    @Override
    public boolean existsByNumeroExpediente(String numeroExpediente) {
        return jpaRepository.existsByNumeroExpediente(numeroExpediente);
    }

    @Override
    public List<Solicitud> findSolicitudesQueRequierenAtencion(int diasMinimos) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasMinimos);
        List<EstadoSolicitud> estadosExcluidos = List.of(
            EstadoSolicitud.TITULO_ENTREGADO,
            EstadoSolicitud.RECHAZADA,
            EstadoSolicitud.ARCHIVADA
        );
        return jpaRepository.findSolicitudesQueRequierenAtencion(estadosExcluidos, fechaLimite)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Solicitud> findSolicitudesMasAntiguasPorEstado(
            pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud estado, int limit) {
        EstadoSolicitud estadoEntity = EstadoSolicitud.valueOf(estado.name());
        return jpaRepository.findSolicitudesMasAntiguasPorEstado(estadoEntity)
                .stream().limit(limit).map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Map<pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud, Long> getEstadisticasPorEstado() {
        // Implementación básica - en producción usar consulta GROUP BY
        return Map.of(
            pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud.RECIBIDA, 
            countByEstado(pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud.RECIBIDA)
        );
    }

    @Override
    public Map<pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud, Long> getEstadisticasPorTipo() {
        // Implementación básica
        return Map.of(
            pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud.TITULACION_INDIVIDUAL, 0L
        );
    }

    private SolicitudEntity toEntity(Solicitud solicitud) {
        return SolicitudEntity.builder()
                .id(solicitud.getId().getCodigo())
                .dniSolicitante(solicitud.getDniSolicitante())
                .nombreSolicitante(solicitud.getNombreSolicitante())
                .direccionPredio(solicitud.getDireccionPredio())
                .estado(EstadoSolicitud.valueOf(solicitud.getEstado().name()))
                .tipo(TipoSolicitud.valueOf(solicitud.getTipo().name()))
                .fechaRegistro(solicitud.getFechaRegistro())
                .fechaUltimaActualizacion(solicitud.getFechaUltimaActualizacion())
                .observaciones(solicitud.getObservaciones())
                .documentosAdjuntos(solicitud.getDocumentosAdjuntos())
                .numeroExpediente(solicitud.getNumeroExpediente())
                .prioridad(solicitud.getPrioridad())
                .build();
    }

    private Solicitud toDomain(SolicitudEntity entity) {
        return Solicitud.builder()
                .id(SolicitudId.of(entity.getId()))
                .dniSolicitante(entity.getDniSolicitante())
                .nombreSolicitante(entity.getNombreSolicitante())
                .direccionPredio(entity.getDireccionPredio())
                .estado(pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud.valueOf(entity.getEstado().name()))
                .tipo(pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud.valueOf(entity.getTipo().name()))
                .fechaRegistro(entity.getFechaRegistro())
                .fechaUltimaActualizacion(entity.getFechaUltimaActualizacion())
                .observaciones(entity.getObservaciones())
                .documentosAdjuntos(entity.getDocumentosAdjuntos())
                .numeroExpediente(entity.getNumeroExpediente())
                .prioridad(entity.getPrioridad())
                .build();
    }
}