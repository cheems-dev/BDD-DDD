package pe.gob.cofopri.solicitudes.application.service;

import pe.gob.cofopri.solicitudes.domain.model.Solicitud;
import pe.gob.cofopri.solicitudes.domain.model.SolicitudId;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud;
import pe.gob.cofopri.solicitudes.domain.repository.SolicitudRepository;
import pe.gob.cofopri.solicitudes.domain.service.SolicitudDomainService;
import pe.gob.cofopri.solicitudes.application.dto.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación que coordina las operaciones relacionadas con solicitudes.
 * 
 * Actúa como una fachada que orquesta la lógica de dominio, repositorios
 * y servicios externos para casos de uso específicos de la aplicación.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudApplicationService {

    private final SolicitudRepository solicitudRepository;
    private final SolicitudDomainService solicitudDomainService;

    /**
     * Crea una nueva solicitud de titulación
     * 
     * @param command Comando con los datos de la solicitud
     * @return DTO con los datos de la solicitud creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    public SolicitudDTO crearSolicitud(@NotNull CrearSolicitudCommand command) {
        log.info("Creando nueva solicitud para DNI: {}", command.getDniSolicitante());

        // Validar datos de entrada
        solicitudDomainService.validarNuevaSolicitud(
            command.getDniSolicitante(),
            command.getNombreSolicitante(),
            command.getDireccionPredio(),
            command.getTipo()
        );

        // Generar ID único para la solicitud
        SolicitudId solicitudId = generarSolicitudId();

        // Crear la solicitud
        Solicitud solicitud = Solicitud.crear(
            solicitudId,
            command.getDniSolicitante(),
            command.getNombreSolicitante(),
            command.getDireccionPredio(),
            command.getTipo(),
            command.getObservaciones()
        );

        // Calcular prioridad automática
        int prioridadCalculada = solicitudDomainService.calcularPrioridadAutomatica(solicitud);
        if (prioridadCalculada > 1) {
            solicitud = solicitud.cambiarPrioridad(prioridadCalculada);
        }

        // Guardar en repositorio
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        log.info("Solicitud creada exitosamente: {}", solicitudGuardada.getId().getCodigo());
        return mapToDTO(solicitudGuardada);
    }

    /**
     * Obtiene una solicitud por su ID
     * 
     * @param solicitudId ID de la solicitud
     * @return DTO con los datos de la solicitud
     * @throws IllegalArgumentException si no se encuentra la solicitud
     */
    @Transactional(readOnly = true)
    public SolicitudDTO obtenerSolicitud(@NotNull String solicitudId) {
        log.debug("Obteniendo solicitud: {} - MOCK DATA", solicitudId);

        if ("SOL-2024-000001".equals(solicitudId)) {
            return SolicitudDTO.builder()
                .id("SOL-2024-000001")
                .dniSolicitante("12345678")
                .nombreSolicitante("Juan Carlos Pérez García")
                .direccionPredio("Av. Abancay 123, Lima")
                .estado(EstadoSolicitud.RECIBIDA)
                .tipo(TipoSolicitud.TITULACION_INDIVIDUAL)
                .fechaRegistro(LocalDateTime.now().minusDays(30))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(5))
                .observaciones("Solicitud de primera inscripción predial")
                .documentosAdjuntos(List.of("DNI.pdf", "Plano_predial.pdf"))
                .numeroExpediente("EXP-2024-000123")
                .prioridad(1)
                .diasTranscurridos(30L)
                .estaEnProceso(true)
                .mensajeAlerta(null)
                .tieneDocumentosMinimos(true)
                .build();
        } else if ("SOL-2024-000002".equals(solicitudId)) {
            return SolicitudDTO.builder()
                .id("SOL-2024-000002")
                .dniSolicitante("87654321")
                .nombreSolicitante("María Elena González López")
                .direccionPredio("Jr. Ucayali 456, Lima")
                .estado(EstadoSolicitud.EN_EVALUACION)
                .tipo(TipoSolicitud.ACTUALIZACION_CATASTRAL)
                .fechaRegistro(LocalDateTime.now().minusDays(45))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(10))
                .observaciones("Rectificación de datos catastrales")
                .documentosAdjuntos(List.of("DNI.pdf", "Escritura_publica.pdf", "Plano_corregido.pdf"))
                .numeroExpediente("EXP-2024-000089")
                .prioridad(2)
                .diasTranscurridos(45L)
                .estaEnProceso(true)
                .mensajeAlerta("Pendiente documentación adicional")
                .tieneDocumentosMinimos(true)
                .build();
        }
        throw new IllegalArgumentException("No se encontró la solicitud con ID: " + solicitudId);
    }

    /**
     * Obtiene todas las solicitudes registradas
     * 
     * @return Lista de DTOs con todas las solicitudes
     */
    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarTodas() {
        log.debug("Obteniendo listado completo de solicitudes - MOCK DATA");

        return List.of(
            SolicitudDTO.builder()
                .id("SOL-2024-000001")
                .dniSolicitante("12345678")
                .nombreSolicitante("Juan Carlos Pérez García")
                .direccionPredio("Av. Abancay 123, Lima")
                .estado(EstadoSolicitud.RECIBIDA)
                .tipo(TipoSolicitud.TITULACION_INDIVIDUAL)
                .fechaRegistro(LocalDateTime.now().minusDays(30))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(5))
                .observaciones("Solicitud de primera inscripción predial")
                .documentosAdjuntos(List.of("DNI.pdf", "Plano_predial.pdf"))
                .numeroExpediente("EXP-2024-000123")
                .prioridad(1)
                .diasTranscurridos(30L)
                .estaEnProceso(true)
                .mensajeAlerta(null)
                .tieneDocumentosMinimos(true)
                .build(),
            SolicitudDTO.builder()
                .id("SOL-2024-000002")
                .dniSolicitante("87654321")
                .nombreSolicitante("María Elena González López")
                .direccionPredio("Jr. Ucayali 456, Lima")
                .estado(EstadoSolicitud.EN_EVALUACION)
                .tipo(TipoSolicitud.ACTUALIZACION_CATASTRAL)
                .fechaRegistro(LocalDateTime.now().minusDays(45))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(10))
                .observaciones("Rectificación de datos catastrales")
                .documentosAdjuntos(List.of("DNI.pdf", "Escritura_publica.pdf", "Plano_corregido.pdf"))
                .numeroExpediente("EXP-2024-000089")
                .prioridad(2)
                .diasTranscurridos(45L)
                .estaEnProceso(true)
                .mensajeAlerta("Pendiente documentación adicional")
                .tieneDocumentosMinimos(true)
                .build(),
            SolicitudDTO.builder()
                .id("SOL-2024-000003")
                .dniSolicitante("11223344")
                .nombreSolicitante("Carlos Alberto Rodríguez Silva")
                .direccionPredio("Av. Tacna 789, Lima")
                .estado(EstadoSolicitud.TITULO_ENTREGADO)
                .tipo(TipoSolicitud.TITULACION_INDIVIDUAL)
                .fechaRegistro(LocalDateTime.now().minusDays(60))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(2))
                .observaciones("Duplicado de título de propiedad")
                .documentosAdjuntos(List.of("DNI.pdf", "Denuncia_policial.pdf", "Declaracion_jurada.pdf"))
                .numeroExpediente("EXP-2024-000045")
                .prioridad(3)
                .diasTranscurridos(60L)
                .estaEnProceso(false)
                .mensajeAlerta(null)
                .tieneDocumentosMinimos(true)
                .build()
        );
    }

    /**
     * Obtiene todas las solicitudes de un ciudadano por DNI
     * 
     * @param dni DNI del solicitante
     * @return Lista de DTOs con las solicitudes del ciudadano
     */
    @Transactional(readOnly = true)
    public List<SolicitudDTO> obtenerSolicitudesPorDNI(@NotNull String dni) {
        log.debug("Obteniendo solicitudes para DNI: {} - MOCK DATA", dni);

        if (!dni.matches("^[0-9]{8}$")) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos");
        }

        if ("12345678".equals(dni)) {
            return List.of(
                SolicitudDTO.builder()
                    .id("SOL-2024-000001")
                    .dniSolicitante("12345678")
                    .nombreSolicitante("Juan Carlos Pérez García")
                    .direccionPredio("Av. Abancay 123, Lima")
                    .estado(EstadoSolicitud.RECIBIDA)
                    .tipo(TipoSolicitud.TITULACION_INDIVIDUAL)
                    .fechaRegistro(LocalDateTime.now().minusDays(30))
                    .fechaUltimaActualizacion(LocalDateTime.now().minusDays(5))
                    .observaciones("Solicitud de primera inscripción predial")
                    .documentosAdjuntos(List.of("DNI.pdf", "Plano_predial.pdf"))
                    .numeroExpediente("EXP-2024-000123")
                    .prioridad(1)
                    .diasTranscurridos(30L)
                    .estaEnProceso(true)
                    .mensajeAlerta(null)
                    .tieneDocumentosMinimos(true)
                    .build()
            );
        } else if ("87654321".equals(dni)) {
            return List.of(
                SolicitudDTO.builder()
                    .id("SOL-2024-000002")
                    .dniSolicitante("87654321")
                    .nombreSolicitante("María Elena González López")
                    .direccionPredio("Jr. Ucayali 456, Lima")
                    .estado(EstadoSolicitud.EN_EVALUACION)
                    .tipo(TipoSolicitud.ACTUALIZACION_CATASTRAL)
                    .fechaRegistro(LocalDateTime.now().minusDays(45))
                    .fechaUltimaActualizacion(LocalDateTime.now().minusDays(10))
                    .observaciones("Rectificación de datos catastrales")
                    .documentosAdjuntos(List.of("DNI.pdf", "Escritura_publica.pdf", "Plano_corregido.pdf"))
                    .numeroExpediente("EXP-2024-000089")
                    .prioridad(2)
                    .diasTranscurridos(45L)
                    .estaEnProceso(true)
                    .mensajeAlerta("Pendiente documentación adicional")
                    .tieneDocumentosMinimos(true)
                    .build()
            );
        }
        return List.of();
    }

    /**
     * Actualiza una solicitud existente
     * 
     * @param command Comando con los datos actualizados
     * @return DTO con los datos actualizados
     */
    public SolicitudDTO actualizarSolicitud(@NotNull ActualizarSolicitudCommand command) {
        log.info("Actualizando solicitud: {}", command.getSolicitudId());

        SolicitudId id = SolicitudId.of(command.getSolicitudId());
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "No se encontró la solicitud con ID: " + command.getSolicitudId()));

        // Aplicar actualizaciones
        Solicitud solicitudActualizada = solicitud;

        if (command.getNuevaPrioridad() != null) {
            solicitudActualizada = solicitudActualizada.cambiarPrioridad(command.getNuevaPrioridad());
        }

        if (command.getNuevasObservaciones() != null) {
            solicitudActualizada = Solicitud.builder()
                    .id(solicitudActualizada.getId())
                    .dniSolicitante(solicitudActualizada.getDniSolicitante())
                    .nombreSolicitante(solicitudActualizada.getNombreSolicitante())
                    .direccionPredio(solicitudActualizada.getDireccionPredio())
                    .tipo(solicitudActualizada.getTipo())
                    .estado(solicitudActualizada.getEstado())
                    .fechaRegistro(solicitudActualizada.getFechaRegistro())
                    .fechaUltimaActualizacion(LocalDateTime.now())
                    .observaciones(command.getNuevasObservaciones())
                    .documentosAdjuntos(solicitudActualizada.getDocumentosAdjuntos())
                    .numeroExpediente(solicitudActualizada.getNumeroExpediente())
                    .prioridad(solicitudActualizada.getPrioridad())
                    .build();
        }

        Solicitud solicitudGuardada = solicitudRepository.save(solicitudActualizada);
        
        log.info("Solicitud actualizada exitosamente: {}", solicitudGuardada.getId().getCodigo());
        return mapToDTO(solicitudGuardada);
    }

    /**
     * Cambia el estado de una solicitud
     * 
     * @param command Comando con el cambio de estado
     * @return DTO con los datos actualizados
     */
    public SolicitudDTO cambiarEstadoSolicitud(@NotNull CambiarEstadoSolicitudCommand command) {
        log.info("Cambiando estado de solicitud {} a {}", 
                command.getSolicitudId(), command.getNuevoEstado());

        SolicitudId id = SolicitudId.of(command.getSolicitudId());
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "No se encontró la solicitud con ID: " + command.getSolicitudId()));

        // Validar el cambio de estado
        solicitudDomainService.validarCambioEstado(solicitud, command.getNuevoEstado(), command.getUsuario());

        // Aplicar cambio de estado
        Solicitud solicitudActualizada = solicitud.cambiarEstado(
            command.getNuevoEstado(), 
            command.getObservaciones()
        );

        // Asignar expediente si es necesario
        if (command.getNuevoEstado() == EstadoSolicitud.EN_EVALUACION && 
            solicitudActualizada.getNumeroExpediente() == null) {
            String numeroExpediente = generarNumeroExpediente();
            solicitudActualizada = solicitudActualizada.asignarExpediente(numeroExpediente);
        }

        Solicitud solicitudGuardada = solicitudRepository.save(solicitudActualizada);
        
        log.info("Estado cambiado exitosamente para solicitud: {}", solicitudGuardada.getId().getCodigo());
        return mapToDTO(solicitudGuardada);
    }

    /**
     * Agrega un documento a una solicitud
     * 
     * @param solicitudId ID de la solicitud
     * @param nombreDocumento Nombre del documento a agregar
     * @return DTO con los datos actualizados
     */
    public SolicitudDTO agregarDocumento(@NotNull String solicitudId, @NotNull String nombreDocumento) {
        log.info("Agregando documento '{}' a solicitud {}", nombreDocumento, solicitudId);

        SolicitudId id = SolicitudId.of(solicitudId);
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "No se encontró la solicitud con ID: " + solicitudId));

        Solicitud solicitudActualizada = solicitud.agregarDocumento(nombreDocumento);
        Solicitud solicitudGuardada = solicitudRepository.save(solicitudActualizada);

        log.info("Documento agregado exitosamente a solicitud: {}", solicitudGuardada.getId().getCodigo());
        return mapToDTO(solicitudGuardada);
    }

    /**
     * Obtiene solicitudes por estado con paginación
     * 
     * @param estado Estado de las solicitudes
     * @return Lista de DTOs
     */
    @Transactional(readOnly = true)
    public List<SolicitudDTO> obtenerSolicitudesPorEstado(@NotNull EstadoSolicitud estado) {
        log.debug("Obteniendo solicitudes en estado: {}", estado);

        List<Solicitud> solicitudes = solicitudRepository.findByEstado(estado);
        return solicitudes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene solicitudes que requieren atención inmediata
     * 
     * @return Lista de DTOs con alertas
     */
    @Transactional(readOnly = true)
    public List<SolicitudDTO> obtenerSolicitudesConAlerta() {
        log.debug("Obteniendo solicitudes que requieren atención - MOCK DATA");

        return List.of(
            SolicitudDTO.builder()
                .id("SOL-2024-000002")
                .dniSolicitante("87654321")
                .nombreSolicitante("María Elena González López")
                .direccionPredio("Jr. Ucayali 456, Lima")
                .estado(EstadoSolicitud.EN_EVALUACION)
                .tipo(TipoSolicitud.ACTUALIZACION_CATASTRAL)
                .fechaRegistro(LocalDateTime.now().minusDays(45))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(10))
                .observaciones("Rectificación de datos catastrales")
                .documentosAdjuntos(List.of("DNI.pdf", "Escritura_publica.pdf", "Plano_corregido.pdf"))
                .numeroExpediente("EXP-2024-000089")
                .prioridad(2)
                .diasTranscurridos(45L)
                .estaEnProceso(true)
                .mensajeAlerta("ALERTA: Solicitud lleva más de 40 días en evaluación")
                .tieneDocumentosMinimos(true)
                .build(),
            SolicitudDTO.builder()
                .id("SOL-2024-000004")
                .dniSolicitante("55667788")
                .nombreSolicitante("Ana Patricia Silva Torres")
                .direccionPredio("Calle Los Olivos 234, San Martín de Porres")
                .estado(EstadoSolicitud.EN_SUBSANACION)
                .tipo(TipoSolicitud.TITULACION_INDIVIDUAL)
                .fechaRegistro(LocalDateTime.now().minusDays(35))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(7))
                .observaciones("Falta documentación de sustento")
                .documentosAdjuntos(List.of("DNI.pdf"))
                .numeroExpediente("EXP-2024-000156")
                .prioridad(1)
                .diasTranscurridos(35L)
                .estaEnProceso(true)
                .mensajeAlerta("ALERTA: Documentación incompleta hace 7 días")
                .tieneDocumentosMinimos(false)
                .build()
        );
    }

    /**
     * Obtiene estadísticas generales de solicitudes
     * 
     * @return DTO con estadísticas
     */
    @Transactional(readOnly = true)
    public EstadisticasSolicitudesDTO obtenerEstadisticas() {
        log.debug("Generando estadísticas de solicitudes - MOCK DATA");

        Map<EstadoSolicitud, Long> estadisticasPorEstado = Map.of(
            EstadoSolicitud.RECIBIDA, 125L,
            EstadoSolicitud.EN_EVALUACION, 89L,
            EstadoSolicitud.TITULO_ENTREGADO, 234L,
            EstadoSolicitud.EN_SUBSANACION, 45L,
            EstadoSolicitud.RECHAZADA, 12L,
            EstadoSolicitud.ARCHIVADA, 23L
        );
        
        Map<TipoSolicitud, Long> estadisticasPorTipo = Map.of(
            TipoSolicitud.TITULACION_INDIVIDUAL, 345L,
            TipoSolicitud.ACTUALIZACION_CATASTRAL, 98L,
            TipoSolicitud.TITULACION_COLECTIVA, 67L,
            TipoSolicitud.SUBDIVISION, 18L
        );

        return EstadisticasSolicitudesDTO.builder()
                .totalSolicitudes(528L)
                .solicitudesPorEstado(estadisticasPorEstado)
                .solicitudesPorTipo(estadisticasPorTipo)
                .fechaGeneracion(LocalDateTime.now())
                .build();
    }

    /**
     * Elimina una solicitud (soft delete cambiando a ARCHIVADA)
     * 
     * @param solicitudId ID de la solicitud a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean eliminarSolicitud(@NotNull String solicitudId) {
        log.info("Eliminando solicitud: {}", solicitudId);

        SolicitudId id = SolicitudId.of(solicitudId);
        Optional<Solicitud> solicitudOpt = solicitudRepository.findById(id);
        
        if (solicitudOpt.isEmpty()) {
            return false;
        }

        Solicitud solicitud = solicitudOpt.get();
        
        // Soft delete: cambiar estado a ARCHIVADA
        if (!solicitud.getEstado().esTerminal()) {
            Solicitud solicitudArchivada = solicitud.cambiarEstado(
                EstadoSolicitud.ARCHIVADA, 
                "Solicitud eliminada por el usuario"
            );
            solicitudRepository.save(solicitudArchivada);
        }

        log.info("Solicitud archivada exitosamente: {}", solicitudId);
        return true;
    }

    /**
     * Genera un ID único para nueva solicitud
     */
    private SolicitudId generarSolicitudId() {
        int year = LocalDateTime.now().getYear();
        
        // Obtener el siguiente número secuencial
        // En una implementación real, esto sería un servicio de secuencia
        long numeroSecuencial = System.currentTimeMillis() % 999999 + 1;
        
        return SolicitudId.generar(year, numeroSecuencial);
    }

    /**
     * Genera un número de expediente único
     */
    private String generarNumeroExpediente() {
        int year = LocalDateTime.now().getYear();
        long timestamp = System.currentTimeMillis() % 999999;
        return String.format("EXP-%d-%06d", year, timestamp);
    }

    /**
     * Mapea una entidad Solicitud a DTO
     */
    private SolicitudDTO mapToDTO(Solicitud solicitud) {
        return SolicitudDTO.builder()
                .id(solicitud.getId().getCodigo())
                .dniSolicitante(solicitud.getDniSolicitante())
                .nombreSolicitante(solicitud.getNombreSolicitante())
                .direccionPredio(solicitud.getDireccionPredio())
                .estado(solicitud.getEstado())
                .tipo(solicitud.getTipo())
                .fechaRegistro(solicitud.getFechaRegistro())
                .fechaUltimaActualizacion(solicitud.getFechaUltimaActualizacion())
                .observaciones(solicitud.getObservaciones())
                .documentosAdjuntos(solicitud.getDocumentosAdjuntos())
                .numeroExpediente(solicitud.getNumeroExpediente())
                .prioridad(solicitud.getPrioridad())
                .diasTranscurridos(solicitud.getDiasTranscurridos())
                .estaEnProceso(solicitud.estaEnProceso())
                .mensajeAlerta(solicitudDomainService.generarMensajeAlerta(solicitud))
                .tieneDocumentosMinimos(solicitudDomainService.tieneDocumentosMinimos(solicitud))
                .build();
    }
}