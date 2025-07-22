package pe.gob.cofopri.infrastructure.repository.jpa;

import pe.gob.cofopri.infrastructure.entity.CiudadanoEntity;
import pe.gob.cofopri.infrastructure.entity.CiudadanoEntity.EstadoVerificacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz JPA Repository para CiudadanoEntity.
 */
public interface CiudadanoJpaRepositoryInterface extends JpaRepository<CiudadanoEntity, String> {
    
    Optional<CiudadanoEntity> findByEmail(String email);
    List<CiudadanoEntity> findByEstadoVerificacion(EstadoVerificacion estado);
    List<CiudadanoEntity> findByNombresContaining(String nombres);
    List<CiudadanoEntity> findByDepartamento(String departamento);
    List<CiudadanoEntity> findByFechaNacimientoBetween(LocalDate inicio, LocalDate fin);
    boolean existsByEmail(String email);
    long countByEstadoVerificacion(EstadoVerificacion estado);
    
    @Query("SELECT c FROM CiudadanoEntity c WHERE c.estadoVerificacion = :estadoPendiente OR c.fechaUltimaVerificacion < :fechaLimite")
    List<CiudadanoEntity> findCiudadanosQueNecesitanReverificacion(@Param("estadoPendiente") EstadoVerificacion estadoPendiente, @Param("fechaLimite") LocalDateTime fechaLimite);
}