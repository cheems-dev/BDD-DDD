package pe.gob.cofopri.solicitudes.domain.repository;

import pe.gob.cofopri.solicitudes.domain.model.Solicitud;
import pe.gob.cofopri.solicitudes.domain.model.SolicitudId;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface para la entidad Solicitud siguiendo principios DDD.
 * 
 * Define las operaciones de persistencia para las solicitudes de titulación
 * sin exponer detalles de implementación de la infraestructura.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
public interface SolicitudRepository {

    /**
     * Guarda una nueva solicitud o actualiza una existente
     * 
     * @param solicitud Solicitud a guardar
     * @return Solicitud guardada
     * @throws IllegalArgumentException si la solicitud es nula
     */
    Solicitud save(Solicitud solicitud);

    /**
     * Busca una solicitud por su identificador único
     * 
     * @param id Identificador de la solicitud
     * @return Optional con la solicitud si existe, vacío si no existe
     */
    Optional<Solicitud> findById(SolicitudId id);

    /**
     * Busca solicitudes por DNI del solicitante
     * 
     * @param dni DNI del solicitante (8 dígitos)
     * @return Lista de solicitudes del ciudadano
     * @throws IllegalArgumentException si el DNI no tiene formato válido
     */
    List<Solicitud> findByDniSolicitante(String dni);

    /**
     * Busca solicitudes por estado
     * 
     * @param estado Estado de las solicitudes a buscar
     * @return Lista de solicitudes en el estado especificado
     */
    List<Solicitud> findByEstado(EstadoSolicitud estado);

    /**
     * Busca solicitudes por tipo
     * 
     * @param tipo Tipo de solicitud
     * @return Lista de solicitudes del tipo especificado
     */
    List<Solicitud> findByTipo(TipoSolicitud tipo);

    /**
     * Busca solicitudes por rango de fechas
     * 
     * @param fechaInicio Fecha de inicio (inclusive)
     * @param fechaFin Fecha de fin (inclusive)
     * @return Lista de solicitudes en el rango de fechas
     */
    List<Solicitud> findByFechaRegistroBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Busca solicitudes por número de expediente
     * 
     * @param numeroExpediente Número de expediente
     * @return Optional con la solicitud si existe
     */
    Optional<Solicitud> findByNumeroExpediente(String numeroExpediente);

    /**
     * Busca solicitudes por dirección del predio (búsqueda parcial)
     * 
     * @param direccion Dirección o parte de la dirección a buscar
     * @return Lista de solicitudes que coinciden con la dirección
     */
    List<Solicitud> findByDireccionPredioContaining(String direccion);

    /**
     * Busca solicitudes por prioridad
     * 
     * @param prioridad Nivel de prioridad (1=Normal, 2=Alta, 3=Urgente)
     * @return Lista de solicitudes con la prioridad especificada
     */
    List<Solicitud> findByPrioridad(Integer prioridad);

    /**
     * Busca solicitudes por estado y prioridad ordenadas por fecha de registro
     * 
     * @param estado Estado de la solicitud
     * @param prioridad Prioridad de la solicitud
     * @return Lista ordenada de solicitudes
     */
    List<Solicitud> findByEstadoAndPrioridadOrderByFechaRegistroAsc(EstadoSolicitud estado, Integer prioridad);

    /**
     * Obtiene todas las solicitudes
     * 
     * @return Lista de todas las solicitudes
     */
    List<Solicitud> findAll();

    /**
     * Obtiene todas las solicitudes con paginación
     * 
     * @param page Número de página (iniciando en 0)
     * @param size Tamaño de página
     * @return Lista de solicitudes paginada
     */
    List<Solicitud> findAll(int page, int size);

    /**
     * Cuenta el total de solicitudes
     * 
     * @return Número total de solicitudes
     */
    long count();

    /**
     * Cuenta solicitudes por estado
     * 
     * @param estado Estado a contar
     * @return Número de solicitudes en el estado especificado
     */
    long countByEstado(EstadoSolicitud estado);

    /**
     * Cuenta solicitudes por DNI del solicitante
     * 
     * @param dni DNI del solicitante
     * @return Número de solicitudes del ciudadano
     */
    long countByDniSolicitante(String dni);

    /**
     * Elimina una solicitud por su ID
     * 
     * @param id Identificador de la solicitud a eliminar
     * @return true si se eliminó, false si no existía
     */
    boolean deleteById(SolicitudId id);

    /**
     * Verifica si existe una solicitud con el ID especificado
     * 
     * @param id Identificador de la solicitud
     * @return true si existe, false si no existe
     */
    boolean existsById(SolicitudId id);

    /**
     * Verifica si existe una solicitud con el número de expediente especificado
     * 
     * @param numeroExpediente Número de expediente
     * @return true si existe, false si no existe
     */
    boolean existsByNumeroExpediente(String numeroExpediente);

    /**
     * Busca solicitudes que requieren atención (estados no terminales y con antigüedad)
     * 
     * @param diasMinimos Días mínimos transcurridos desde el registro
     * @return Lista de solicitudes que requieren seguimiento
     */
    List<Solicitud> findSolicitudesQueRequierenAtencion(int diasMinimos);

    /**
     * Busca las solicitudes más antiguas en un estado específico
     * 
     * @param estado Estado de las solicitudes
     * @param limit Número máximo de resultados
     * @return Lista de solicitudes más antiguas en el estado
     */
    List<Solicitud> findSolicitudesMasAntiguasPorEstado(EstadoSolicitud estado, int limit);

    /**
     * Obtiene estadísticas de solicitudes por estado
     * 
     * @return Mapa con la cantidad de solicitudes por cada estado
     */
    java.util.Map<EstadoSolicitud, Long> getEstadisticasPorEstado();

    /**
     * Obtiene estadísticas de solicitudes por tipo
     * 
     * @return Mapa con la cantidad de solicitudes por cada tipo
     */
    java.util.Map<TipoSolicitud, Long> getEstadisticasPorTipo();
}