package pe.gob.cofopri.infrastructure.repository.jpa;

import pe.gob.cofopri.infrastructure.entity.SolicitudEntity;
import pe.gob.cofopri.infrastructure.entity.SolicitudEntity.EstadoSolicitud;
import pe.gob.cofopri.infrastructure.entity.SolicitudEntity.TipoSolicitud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz JPA Repository para SolicitudEntity.
 */
public interface SolicitudJpaRepositoryInterface extends JpaRepository<SolicitudEntity, String> {
    
    List<SolicitudEntity> findByDniSolicitante(String dni);
    List<SolicitudEntity> findByEstado(EstadoSolicitud estado);
    List<SolicitudEntity> findByTipo(TipoSolicitud tipo);
    List<SolicitudEntity> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);
    Optional<SolicitudEntity> findByNumeroExpediente(String numeroExpediente);
    List<SolicitudEntity> findByDireccionPredioContaining(String direccion);
    List<SolicitudEntity> findByPrioridad(Integer prioridad);
    List<SolicitudEntity> findByEstadoAndPrioridadOrderByFechaRegistroAsc(EstadoSolicitud estado, Integer prioridad);
    long countByEstado(EstadoSolicitud estado);
    long countByDniSolicitante(String dni);
    boolean existsByNumeroExpediente(String numeroExpediente);
    
    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado NOT IN (:estadosExcluidos) AND s.fechaRegistro <= :fechaLimite")
    List<SolicitudEntity> findSolicitudesQueRequierenAtencion(@Param("estadosExcluidos") List<EstadoSolicitud> estadosExcluidos, @Param("fechaLimite") LocalDateTime fechaLimite);
    
    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado = ?1 ORDER BY s.fechaRegistro ASC")
    List<SolicitudEntity> findSolicitudesMasAntiguasPorEstado(EstadoSolicitud estado);
}