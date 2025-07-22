package pe.gob.cofopri.solicitudes.domain.service;

import pe.gob.cofopri.solicitudes.domain.model.Solicitud;
import pe.gob.cofopri.solicitudes.domain.model.SolicitudId;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Servicio de dominio que encapsula la lógica de negocio compleja
 * relacionada con las solicitudes de titulación en COFOPRI.
 * 
 * Implementa reglas de negocio, validaciones complejas y políticas
 * que no pertenecen a una entidad específica.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Slf4j
@Service
public class SolicitudDomainService {

    private static final Pattern PATRON_DNI = Pattern.compile("^[0-9]{8}$");
    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]{2,100}$");
    private static final int DIAS_MAXIMO_PROCESO = 90;
    private static final int DIAS_ALERTA_RETRASO = 60;

    /**
     * Valida si una solicitud cumple con todos los criterios de negocio para ser creada
     * 
     * @param dniSolicitante DNI del solicitante
     * @param nombreSolicitante Nombre completo del solicitante
     * @param direccionPredio Dirección del predio
     * @param tipo Tipo de solicitud
     * @throws IllegalArgumentException si algún criterio no se cumple
     */
    public void validarNuevaSolicitud(@NotBlank String dniSolicitante, 
                                     @NotBlank String nombreSolicitante,
                                     @NotBlank String direccionPredio, 
                                     @NotNull TipoSolicitud tipo) {
        
        log.debug("Validando nueva solicitud para DNI: {}", dniSolicitante);

        // Validación de DNI peruano
        if (!PATRON_DNI.matcher(dniSolicitante.trim()).matches()) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos");
        }

        // Validación de nombre (solo letras, tildes, ñ y espacios)
        if (!PATRON_NOMBRE.matcher(nombreSolicitante.trim()).matches()) {
            throw new IllegalArgumentException(
                "El nombre debe contener solo letras, tildes, ñ y espacios, entre 2 y 100 caracteres");
        }

        // Validación de dirección
        if (direccionPredio.trim().length() < 10) {
            throw new IllegalArgumentException("La dirección del predio debe tener al menos 10 caracteres");
        }

        if (direccionPredio.trim().length() > 200) {
            throw new IllegalArgumentException("La dirección del predio no puede exceder los 200 caracteres");
        }

        // Validaciones específicas por tipo de solicitud
        validarTipoSolicitud(tipo, direccionPredio);

        log.info("Validación exitosa para nueva solicitud - DNI: {}, Tipo: {}", dniSolicitante, tipo);
    }

    /**
     * Valida si un cambio de estado es permitido según las reglas de negocio
     * 
     * @param solicitud Solicitud actual
     * @param nuevoEstado Estado al que se quiere cambiar
     * @param usuario Usuario que solicita el cambio (para auditoría)
     * @throws IllegalStateException si el cambio no es permitido
     */
    public void validarCambioEstado(@NotNull Solicitud solicitud, 
                                   @NotNull EstadoSolicitud nuevoEstado,
                                   @NotBlank String usuario) {
        
        log.debug("Validando cambio de estado de {} a {} para solicitud {}", 
                 solicitud.getEstado(), nuevoEstado, solicitud.getId().getCodigo());

        // No se puede cambiar desde estados terminales
        if (solicitud.getEstado().esTerminal()) {
            throw new IllegalStateException(
                String.format("No se puede cambiar el estado desde %s porque es un estado terminal", 
                             solicitud.getEstado()));
        }

        // Validaciones específicas por estado
        switch (nuevoEstado) {
            case INSPECCION_REALIZADA -> {
                if (solicitud.getEstado() != EstadoSolicitud.INSPECCION_PENDIENTE) {
                    throw new IllegalStateException("Solo se puede marcar inspección realizada desde estado pendiente");
                }
            }
            case TITULO_GENERADO -> {
                if (solicitud.getEstado() != EstadoSolicitud.ANALISIS_JURIDICO) {
                    throw new IllegalStateException("Solo se puede generar título después del análisis jurídico");
                }
            }
            case ENVIADO_SUNARP -> {
                if (solicitud.getEstado() != EstadoSolicitud.TITULO_GENERADO) {
                    throw new IllegalStateException("Solo se puede enviar a SUNARP después de generar el título");
                }
            }
            case TITULO_ENTREGADO -> {
                if (solicitud.getEstado() != EstadoSolicitud.INSCRITO) {
                    throw new IllegalStateException("Solo se puede entregar título después de la inscripción en SUNARP");
                }
            }
        }

        log.info("Cambio de estado validado exitosamente para solicitud {} por usuario {}", 
                solicitud.getId().getCodigo(), usuario);
    }

    /**
     * Calcula la prioridad automática de una solicitud basada en criterios de negocio
     * 
     * @param solicitud Solicitud a evaluar
     * @return Nivel de prioridad calculado (1=Normal, 2=Alta, 3=Urgente)
     */
    public int calcularPrioridadAutomatica(@NotNull Solicitud solicitud) {
        log.debug("Calculando prioridad para solicitud {}", solicitud.getId().getCodigo());

        int prioridad = 1; // Prioridad normal por defecto

        // Prioridad alta para adultos mayores (asumiendo que nombres con "de la" indican mayor edad)
        if (esPosibleAdultoMayor(solicitud.getNombreSolicitante())) {
            prioridad = Math.max(prioridad, 2);
        }

        // Prioridad alta para solicitudes con mucho tiempo en proceso
        long diasTranscurridos = solicitud.getDiasTranscurridos();
        if (diasTranscurridos >= DIAS_ALERTA_RETRASO) {
            prioridad = Math.max(prioridad, 2);
        }

        // Prioridad urgente para solicitudes cerca del límite máximo
        if (diasTranscurridos >= DIAS_MAXIMO_PROCESO - 15) {
            prioridad = 3;
        }

        // Prioridad alta para titulación colectiva (impacto social mayor)
        if (solicitud.getTipo() == TipoSolicitud.TITULACION_COLECTIVA) {
            prioridad = Math.max(prioridad, 2);
        }

        log.info("Prioridad calculada: {} para solicitud {}", prioridad, solicitud.getId().getCodigo());
        return prioridad;
    }

    /**
     * Determina si una solicitud requiere atención inmediata
     * 
     * @param solicitud Solicitud a evaluar
     * @return true si requiere atención inmediata
     */
    public boolean requiereAtencionInmediata(@NotNull Solicitud solicitud) {
        // Solicitudes urgentes siempre requieren atención
        if (solicitud.getPrioridad() != null && solicitud.getPrioridad() >= 3) {
            return true;
        }

        // Solicitudes con más de 60 días requieren atención
        if (solicitud.getDiasTranscurridos() >= DIAS_ALERTA_RETRASO) {
            return true;
        }

        // Solicitudes en estados críticos
        EstadoSolicitud estado = solicitud.getEstado();
        if (estado == EstadoSolicitud.EN_SUBSANACION || estado == EstadoSolicitud.RECHAZADA) {
            return true;
        }

        return false;
    }

    /**
     * Genera un mensaje de alerta para solicitudes que requieren seguimiento
     * 
     * @param solicitud Solicitud a evaluar
     * @return Mensaje de alerta o null si no requiere alerta
     */
    public String generarMensajeAlerta(@NotNull Solicitud solicitud) {
        if (!requiereAtencionInmediata(solicitud)) {
            return null;
        }

        StringBuilder mensaje = new StringBuilder("ALERTA: ");
        
        if (solicitud.getDiasTranscurridos() >= DIAS_MAXIMO_PROCESO) {
            mensaje.append("Solicitud excede tiempo máximo de proceso (")
                   .append(DIAS_MAXIMO_PROCESO)
                   .append(" días). ");
        } else if (solicitud.getDiasTranscurridos() >= DIAS_ALERTA_RETRASO) {
            mensaje.append("Solicitud con posible retraso (")
                   .append(solicitud.getDiasTranscurridos())
                   .append(" días). ");
        }

        if (solicitud.getPrioridad() != null && solicitud.getPrioridad() >= 3) {
            mensaje.append("PRIORIDAD URGENTE. ");
        }

        if (solicitud.getEstado() == EstadoSolicitud.EN_SUBSANACION) {
            mensaje.append("Requiere documentos adicionales del ciudadano. ");
        }

        return mensaje.toString().trim();
    }

    /**
     * Valida si los documentos mínimos están presentes según el tipo de solicitud
     * 
     * @param solicitud Solicitud a validar
     * @return true si tiene los documentos mínimos
     */
    public boolean tieneDocumentosMinimos(@NotNull Solicitud solicitud) {
        int documentosRequeridos = switch (solicitud.getTipo()) {
            case TITULACION_INDIVIDUAL -> 3; // DNI, documento de posesión, plano
            case TITULACION_COLECTIVA -> 5;  // Adiciona acta de asamblea y lista de beneficiarios
            case ACTUALIZACION_CATASTRAL -> 2; // DNI y título anterior
            case SUBDIVISION, FUSION -> 4; // Documentos adicionales técnicos
        };

        return solicitud.getDocumentosAdjuntos().size() >= documentosRequeridos;
    }

    /**
     * Valida criterios específicos según el tipo de solicitud
     */
    private void validarTipoSolicitud(TipoSolicitud tipo, String direccionPredio) {
        switch (tipo) {
            case TITULACION_COLECTIVA -> {
                // Para titulación colectiva, la dirección debe indicar un conjunto o área
                if (!direccionPredio.toLowerCase().contains("mz") && 
                    !direccionPredio.toLowerCase().contains("manzana") &&
                    !direccionPredio.toLowerCase().contains("conjunto") &&
                    !direccionPredio.toLowerCase().contains("asociación")) {
                    log.warn("Dirección posiblemente incorrecta para titulación colectiva: {}", direccionPredio);
                }
            }
            case SUBDIVISION -> {
                // Para subdivisión, debe especificar lote o parcela
                if (!direccionPredio.toLowerCase().contains("lote") && 
                    !direccionPredio.toLowerCase().contains("lt")) {
                    throw new IllegalArgumentException(
                        "Para subdivisión, la dirección debe especificar el lote a subdividir");
                }
            }
        }
    }

    /**
     * Heurística simple para detectar posibles adultos mayores
     */
    private boolean esPosibleAdultoMayor(String nombre) {
        String nombreLower = nombre.toLowerCase();
        // Patrones comunes en nombres de adultos mayores en Perú
        return nombreLower.contains("de la") || 
               nombreLower.contains("del ") ||
               nombreLower.matches(".*\\b(doña|don)\\b.*");
    }
}