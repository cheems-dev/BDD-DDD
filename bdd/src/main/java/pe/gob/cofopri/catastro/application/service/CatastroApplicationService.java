package pe.gob.cofopri.catastro.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.cofopri.catastro.application.dto.CrearPredioCommand;
import pe.gob.cofopri.catastro.application.dto.PredioDTO;
import pe.gob.cofopri.catastro.application.dto.ActualizarPredioCommand;
import pe.gob.cofopri.catastro.domain.model.Predio;
import pe.gob.cofopri.catastro.domain.model.PredioId;
import pe.gob.cofopri.catastro.domain.model.Coordenadas;
import pe.gob.cofopri.catastro.domain.repository.PredioRepository;
import pe.gob.cofopri.catastro.domain.service.PredioValidationService;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para el módulo de Catastro.
 * 
 * Coordina los casos de uso relacionados con la gestión de predios
 * en el sistema catastral de COFOPRI.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CatastroApplicationService {

    private final PredioRepository predioRepository;
    private final PredioValidationService validationService;

    /**
     * Registra un nuevo predio en el sistema
     * 
     * @param command Datos del predio a crear
     * @return DTO del predio creado
     * @throws IllegalArgumentException Si los datos son inválidos
     * @throws IllegalStateException Si el predio no puede ser registrado
     */
    public PredioDTO registrarPredio(CrearPredioCommand command) {
        log.info("Iniciando registro de predio: {}", command.getCodigoPredio());

        try {
            // Crear objetos de dominio
            PredioId predioId = PredioId.of(command.getCodigoPredio());
            Coordenadas coordenadas = Coordenadas.of(command.getLatitud(), command.getLongitud());

            // Crear el predio
            Predio predio = Predio.crear(
                predioId,
                command.getPropietario(),
                coordenadas,
                command.getArea(),
                command.getDireccion(),
                command.getObservaciones()
            );

            // Validar el predio usando el servicio de dominio
            validationService.validarRegistroPredio(predio);

            // Guardar el predio
            Predio predioGuardado = predioRepository.save(predio);

            log.info("Predio registrado exitosamente: {}", predioGuardado.getId().getCodigo());
            
            return mapearAPredioDTO(predioGuardado);

        } catch (Exception e) {
            log.error("Error al registrar predio {}: {}", command.getCodigoPredio(), e.getMessage());
            throw e;
        }
    }

    /**
     * Busca un predio por su código
     * 
     * @param codigoPredio Código del predio
     * @return Optional con el predio si existe
     */
    @Transactional(readOnly = true)
    public Optional<PredioDTO> buscarPredio(String codigoPredio) {
        log.debug("Buscando predio: {} - MOCK DATA", codigoPredio);

        // Datos mock basados en el código solicitado
        if ("LIM-01-001-001".equals(codigoPredio)) {
            return Optional.of(PredioDTO.builder()
                .codigoPredio("LIM-01-001-001")
                .propietario("Juan Pérez García")
                .latitud(new java.math.BigDecimal("-12.046374"))
                .longitud(new java.math.BigDecimal("-77.042793"))
                .area(new java.math.BigDecimal("120.5"))
                .areaEnHectareas(new java.math.BigDecimal("0.012"))
                .direccion("Av. Abancay 123, Lima")
                .estado("ACTIVO")
                .descripcionEstado("Predio activo en el sistema")
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(30))
                .fechaUltimaActualizacion(java.time.LocalDateTime.now().minusDays(5))
                .observaciones("Predio registrado correctamente")
                .departamento("LIMA")
                .provincia("LIMA")
                .distrito("LIMA")
                .build());
        }
        return Optional.empty();
    }

    /**
     * Busca todos los predios de un propietario
     * 
     * @param propietario Nombre del propietario
     * @return Lista de predios del propietario
     */
    @Transactional(readOnly = true)
    public List<PredioDTO> buscarPrediosPorPropietario(String propietario) {
        log.debug("Buscando predios del propietario: {} - MOCK DATA", propietario);

        // Retornar datos mock filtrados por propietario
        if ("Juan Pérez García".equalsIgnoreCase(propietario)) {
            return List.of(
                PredioDTO.builder()
                    .codigoPredio("LIM-01-001-001")
                    .propietario("Juan Pérez García")
                    .latitud(new java.math.BigDecimal("-12.046374"))
                    .longitud(new java.math.BigDecimal("-77.042793"))
                    .area(new java.math.BigDecimal("120.5"))
                    .areaEnHectareas(new java.math.BigDecimal("0.012"))
                    .direccion("Av. Abancay 123, Lima")
                    .estado("ACTIVO")
                    .descripcionEstado("Predio activo en el sistema")
                    .fechaRegistro(java.time.LocalDateTime.now().minusDays(30))
                    .fechaUltimaActualizacion(java.time.LocalDateTime.now().minusDays(5))
                    .observaciones("Predio registrado correctamente")
                    .departamento("LIMA")
                    .provincia("LIMA")
                    .distrito("LIMA")
                    .build()
            );
        }
        return List.of();
    }

    /**
     * Busca predios por estado
     * 
     * @param estado Estado del predio
     * @return Lista de predios con el estado especificado
     */
    @Transactional(readOnly = true)
    public List<PredioDTO> buscarPrediosPorEstado(String estado) {
        log.debug("Buscando predios por estado: {}", estado);

        Predio.EstadoPredio estadoPredio = Predio.EstadoPredio.valueOf(estado.toUpperCase());
        return predioRepository.findByEstado(estadoPredio)
                .stream()
                .map(this::mapearAPredioDTO)
                .toList();
    }

    /**
     * Actualiza los datos de un predio
     * 
     * @param command Datos de actualización
     * @return DTO del predio actualizado
     * @throws IllegalArgumentException Si el predio no existe
     */
    public PredioDTO actualizarPredio(ActualizarPredioCommand command) {
        log.info("Actualizando predio: {}", command.getCodigoPredio());

        PredioId predioId = PredioId.of(command.getCodigoPredio());
        Predio predio = predioRepository.findById(predioId)
                .orElseThrow(() -> new IllegalArgumentException("Predio no encontrado: " + command.getCodigoPredio()));

        Predio predioActualizado = predio;

        // Actualizar propietario si se proporciona
        if (command.getPropietario() != null && !command.getPropietario().trim().isEmpty()) {
            predioActualizado = predioActualizado.actualizarPropietario(command.getPropietario());
        }

        // Actualizar observaciones si se proporcionan
        if (command.getObservaciones() != null) {
            predioActualizado = predioActualizado.actualizarObservaciones(command.getObservaciones());
        }

        Predio predioGuardado = predioRepository.save(predioActualizado);
        log.info("Predio actualizado exitosamente: {}", predioGuardado.getId().getCodigo());

        return mapearAPredioDTO(predioGuardado);
    }

    /**
     * Cambia el estado de un predio
     * 
     * @param codigoPredio Código del predio
     * @param nuevoEstado Nuevo estado
     * @return DTO del predio actualizado
     */
    public PredioDTO cambiarEstadoPredio(String codigoPredio, String nuevoEstado) {
        log.info("Cambiando estado del predio {} a {}", codigoPredio, nuevoEstado);

        PredioId predioId = PredioId.of(codigoPredio);
        Predio.EstadoPredio estadoPredio = Predio.EstadoPredio.valueOf(nuevoEstado.toUpperCase());

        // Validar el cambio de estado usando el servicio de dominio
        validationService.validarCambioEstado(predioId, estadoPredio);

        Predio predio = predioRepository.findById(predioId)
                .orElseThrow(() -> new IllegalArgumentException("Predio no encontrado: " + codigoPredio));

        Predio predioActualizado = predio.cambiarEstado(estadoPredio);
        Predio predioGuardado = predioRepository.save(predioActualizado);

        log.info("Estado del predio {} cambiado exitosamente a {}", codigoPredio, nuevoEstado);
        
        return mapearAPredioDTO(predioGuardado);
    }

    /**
     * Busca todos los predios
     * 
     * @return Lista de todos los predios
     */
    @Transactional(readOnly = true)
    public List<PredioDTO> listarTodos() {
        log.debug("Listando todos los predios - MOCK DATA");

        // Retornar datos mock para pruebas
        return List.of(
            PredioDTO.builder()
                .codigoPredio("LIM-01-001-001")
                .propietario("Juan Pérez García")
                .latitud(new java.math.BigDecimal("-12.046374"))
                .longitud(new java.math.BigDecimal("-77.042793"))
                .area(new java.math.BigDecimal("120.5"))
                .areaEnHectareas(new java.math.BigDecimal("0.012"))
                .direccion("Av. Abancay 123, Lima")
                .estado("ACTIVO")
                .descripcionEstado("Predio activo en el sistema")
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(30))
                .fechaUltimaActualizacion(java.time.LocalDateTime.now().minusDays(5))
                .observaciones("Predio registrado correctamente")
                .departamento("LIMA")
                .provincia("LIMA")
                .distrito("LIMA")
                .build(),
            PredioDTO.builder()
                .codigoPredio("LIM-01-001-002")
                .propietario("María González López")
                .latitud(new java.math.BigDecimal("-12.047123"))
                .longitud(new java.math.BigDecimal("-77.043456"))
                .area(new java.math.BigDecimal("95.8"))
                .areaEnHectareas(new java.math.BigDecimal("0.0096"))
                .direccion("Jr. Ucayali 456, Lima")
                .estado("FORMALIZADO")
                .descripcionEstado("Predio formalizado")
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(60))
                .fechaUltimaActualizacion(java.time.LocalDateTime.now().minusDays(10))
                .observaciones("Documentos completos")
                .departamento("LIMA")
                .provincia("LIMA")
                .distrito("LIMA")
                .build(),
            PredioDTO.builder()
                .codigoPredio("LIM-01-002-001")
                .propietario("Carlos Rodríguez Silva")
                .latitud(new java.math.BigDecimal("-12.048890"))
                .longitud(new java.math.BigDecimal("-77.044112"))
                .area(new java.math.BigDecimal("78.3"))
                .areaEnHectareas(new java.math.BigDecimal("0.0078"))
                .direccion("Av. Tacna 789, Lima")
                .estado("EN_REVISION")
                .descripcionEstado("Predio en proceso de revisión")
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(15))
                .fechaUltimaActualizacion(java.time.LocalDateTime.now().minusDays(2))
                .observaciones("Pendiente documentación adicional")
                .departamento("LIMA")
                .provincia("LIMA")
                .distrito("BREÑA")
                .build()
        );
    }

    /**
     * @deprecated Use listarTodos() instead
     */
    @Transactional(readOnly = true)
    public List<PredioDTO> listarTodosLosPredios() {
        return listarTodos();
    }

    /**
     * Busca predios cercanos a unas coordenadas
     * 
     * @param latitud Latitud central
     * @param longitud Longitud central
     * @param radioEnMetros Radio de búsqueda en metros
     * @return Lista de predios dentro del radio
     */
    @Transactional(readOnly = true)
    public List<PredioDTO> buscarPrediosCercanos(double latitud, double longitud, double radioEnMetros) {
        log.debug("Buscando predios cercanos a [{}, {}] en radio de {} metros - MOCK DATA", latitud, longitud, radioEnMetros);

        // Retornar predios mock cercanos a Lima centro
        return List.of(
            PredioDTO.builder()
                .codigoPredio("LIM-01-001-001")
                .propietario("Juan Pérez García")
                .latitud(new java.math.BigDecimal("-12.046374"))
                .longitud(new java.math.BigDecimal("-77.042793"))
                .area(new java.math.BigDecimal("120.5"))
                .areaEnHectareas(new java.math.BigDecimal("0.012"))
                .direccion("Av. Abancay 123, Lima")
                .estado("ACTIVO")
                .descripcionEstado("Predio activo en el sistema")
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(30))
                .fechaUltimaActualizacion(java.time.LocalDateTime.now().minusDays(5))
                .observaciones("Predio registrado correctamente")
                .departamento("LIMA")
                .provincia("LIMA")
                .distrito("LIMA")
                .build(),
            PredioDTO.builder()
                .codigoPredio("LIM-01-001-002")
                .propietario("María González López")
                .latitud(new java.math.BigDecimal("-12.047123"))
                .longitud(new java.math.BigDecimal("-77.043456"))
                .area(new java.math.BigDecimal("95.8"))
                .areaEnHectareas(new java.math.BigDecimal("0.0096"))
                .direccion("Jr. Ucayali 456, Lima")
                .estado("FORMALIZADO")
                .descripcionEstado("Predio formalizado")
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(60))
                .fechaUltimaActualizacion(java.time.LocalDateTime.now().minusDays(10))
                .observaciones("Documentos completos")
                .departamento("LIMA")
                .provincia("LIMA")
                .distrito("LIMA")
                .build()
        );
    }

    /**
     * Busca potenciales duplicados de un predio
     * 
     * @param codigoPredio Código del predio
     * @return Lista de potenciales duplicados
     */
    @Transactional(readOnly = true)
    public List<PredioDTO> buscarPotencialesDuplicados(String codigoPredio) {
        log.debug("Buscando potenciales duplicados para predio: {}", codigoPredio);

        PredioId predioId = PredioId.of(codigoPredio);
        Predio predio = predioRepository.findById(predioId)
                .orElseThrow(() -> new IllegalArgumentException("Predio no encontrado: " + codigoPredio));

        return validationService.buscarPotencialesDuplicados(predio)
                .stream()
                .map(this::mapearAPredioDTO)
                .toList();
    }

    /**
     * Obtiene estadísticas generales de predios
     * 
     * @return Objeto con estadísticas
     */
    @Transactional(readOnly = true)
    public EstadisticasPrediosDTO obtenerEstadisticas() {
        log.debug("Obteniendo estadísticas de predios - MOCK DATA");

        // Retornar estadísticas mock
        return EstadisticasPrediosDTO.builder()
                .totalPredios(1250L)
                .prediosActivos(850L)
                .prediosFormalizados(320L)
                .prediosEnRevision(65L)
                .prediosSuspendidos(10L)
                .prediosInactivos(5L)
                .build();
    }

    /**
     * Mapea un objeto Predio del dominio a un DTO
     * 
     * @param predio Predio del dominio
     * @return DTO del predio
     */
    private PredioDTO mapearAPredioDTO(Predio predio) {
        return PredioDTO.builder()
                .codigoPredio(predio.getId().getCodigo())
                .propietario(predio.getPropietario())
                .latitud(predio.getCoordenadas().getLatitud())
                .longitud(predio.getCoordenadas().getLongitud())
                .area(predio.getArea())
                .areaEnHectareas(predio.getAreaEnHectareas())
                .direccion(predio.getDireccion())
                .estado(predio.getEstado().name())
                .descripcionEstado(predio.getEstado().getDescripcion())
                .fechaRegistro(predio.getFechaRegistro())
                .fechaUltimaActualizacion(predio.getFechaUltimaActualizacion())
                .observaciones(predio.getObservaciones())
                .departamento(predio.getId().getDepartamento())
                .provincia(predio.getId().getProvincia())
                .distrito(predio.getId().getDistrito())
                .build();
    }
}