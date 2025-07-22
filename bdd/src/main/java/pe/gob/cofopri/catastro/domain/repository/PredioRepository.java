package pe.gob.cofopri.catastro.domain.repository;

import pe.gob.cofopri.catastro.domain.model.Predio;
import pe.gob.cofopri.catastro.domain.model.PredioId;
import pe.gob.cofopri.catastro.domain.model.Coordenadas;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface para el agregado Predio.
 * 
 * Define las operaciones de persistencia necesarias para manejar
 * predios en el sistema catastral de COFOPRI.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
public interface PredioRepository {

    /**
     * Guarda un predio en el repositorio
     * 
     * @param predio El predio a guardar
     * @return El predio guardado
     */
    Predio save(Predio predio);

    /**
     * Busca un predio por su ID
     * 
     * @param id El ID del predio
     * @return Optional conteniendo el predio si existe
     */
    Optional<Predio> findById(PredioId id);

    /**
     * Busca todos los predios de un propietario
     * 
     * @param propietario Nombre del propietario
     * @return Lista de predios del propietario
     */
    List<Predio> findByPropietario(String propietario);

    /**
     * Busca predios por estado
     * 
     * @param estado Estado del predio
     * @return Lista de predios con el estado especificado
     */
    List<Predio> findByEstado(Predio.EstadoPredio estado);

    /**
     * Busca predios en un radio específico alrededor de unas coordenadas
     * 
     * @param coordenadas Coordenadas centrales
     * @param radioEnMetros Radio de búsqueda en metros
     * @return Lista de predios dentro del radio
     */
    List<Predio> findByCoordenadasEnRadio(Coordenadas coordenadas, double radioEnMetros);

    /**
     * Busca predios por departamento (extraído del código de predio)
     * 
     * @param codigoDepartamento Código del departamento (2 dígitos)
     * @return Lista de predios en el departamento
     */
    List<Predio> findByDepartamento(String codigoDepartamento);

    /**
     * Busca predios por provincia (extraído del código de predio)
     * 
     * @param codigoDepartamento Código del departamento (2 dígitos)
     * @param codigoProvincia Código de la provincia (2 dígitos)
     * @return Lista de predios en la provincia
     */
    List<Predio> findByProvincia(String codigoDepartamento, String codigoProvincia);

    /**
     * Busca predios por distrito (extraído del código de predio)
     * 
     * @param codigoDepartamento Código del departamento (2 dígitos)
     * @param codigoProvincia Código de la provincia (2 dígitos)
     * @param codigoDistrito Código del distrito (2 dígitos)
     * @return Lista de predios en el distrito
     */
    List<Predio> findByDistrito(String codigoDepartamento, String codigoProvincia, String codigoDistrito);

    /**
     * Obtiene todos los predios
     * 
     * @return Lista de todos los predios
     */
    List<Predio> findAll();

    /**
     * Verifica si existe un predio con el ID especificado
     * 
     * @param id El ID del predio
     * @return true si existe, false en caso contrario
     */
    boolean existsById(PredioId id);

    /**
     * Elimina un predio por su ID
     * 
     * @param id El ID del predio a eliminar
     */
    void deleteById(PredioId id);

    /**
     * Cuenta el número total de predios
     * 
     * @return Número total de predios
     */
    long count();

    /**
     * Cuenta el número de predios por estado
     * 
     * @param estado Estado del predio
     * @return Número de predios con el estado especificado
     */
    long countByEstado(Predio.EstadoPredio estado);
}