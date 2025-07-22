package pe.gob.cofopri.ciudadanos.domain.repository;

import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano;
import pe.gob.cofopri.ciudadanos.domain.model.DNI;
import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.EstadoVerificacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface para la entidad Ciudadano siguiendo principios DDD.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
public interface CiudadanoRepository {

    Ciudadano save(Ciudadano ciudadano);

    Optional<Ciudadano> findByDni(DNI dni);

    Optional<Ciudadano> findByEmail(String email);

    List<Ciudadano> findByEstadoVerificacion(EstadoVerificacion estado);

    List<Ciudadano> findByNombresContaining(String nombres);

    List<Ciudadano> findByDepartamento(String departamento);

    List<Ciudadano> findByFechaNacimientoBetween(LocalDate inicio, LocalDate fin);

    List<Ciudadano> findCiudadanosQueNecesitanReverificacion();

    boolean existsByDni(DNI dni);

    boolean existsByEmail(String email);

    long count();

    long countByEstadoVerificacion(EstadoVerificacion estado);

    List<Ciudadano> findAll();
}