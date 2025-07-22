package pe.gob.cofopri.infrastructure.repository.jpa;

import pe.gob.cofopri.infrastructure.entity.PredioEntity;
import pe.gob.cofopri.infrastructure.entity.PredioEntity.EstadoPredio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz JPA Repository para PredioEntity.
 */
public interface PredioJpaRepositoryInterface extends JpaRepository<PredioEntity, String> {
    
    List<PredioEntity> findByPropietarioContaining(String propietario);
    List<PredioEntity> findByEstado(EstadoPredio estado);
    List<PredioEntity> findByDireccionContaining(String direccion);
    List<PredioEntity> findByAreaBetween(BigDecimal areaMin, BigDecimal areaMax);
    List<PredioEntity> findByCoordenadaXBetweenAndCoordenadaYBetween(
        BigDecimal xMin, BigDecimal xMax, BigDecimal yMin, BigDecimal yMax);
    long countByEstado(EstadoPredio estado);
    
    @Query("SELECT p FROM PredioEntity p WHERE p.estado = 'ACTIVO' OR p.estado = 'EN_REVISION'")
    List<PredioEntity> findPrediosActivos();
    
    @Query("SELECT p FROM PredioEntity p WHERE p.area >= ?1")
    List<PredioEntity> findPrediosConAreaMinima(BigDecimal areaMinima);
}