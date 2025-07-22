package pe.gob.cofopri.solicitudes.application.dto;

import pe.gob.cofopri.solicitudes.domain.model.Solicitud.EstadoSolicitud;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud.TipoSolicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO para transferir estadísticas de solicitudes.
 * 
 * Proporciona información agregada sobre el estado de las solicitudes
 * en el sistema para reportes y dashboards.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EstadisticasSolicitudesDTO {

    private Long totalSolicitudes;

    private Map<EstadoSolicitud, Long> solicitudesPorEstado;

    private Map<TipoSolicitud, Long> solicitudesPorTipo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaGeneracion;

    // Estadísticas calculadas adicionales
    private Long solicitudesEnProceso;

    private Long solicitudesTerminadas;

    private Long solicitudesConRetraso;

    private Long solicitudesUrgentes;

    private Double promedioTiempoProceso;

    private Double porcentajeExito;

    /**
     * Calcula estadísticas derivadas
     */
    public void calcularEstadisticasDerivadas() {
        if (solicitudesPorEstado != null) {
            // Solicitudes en proceso (estados no terminales)
            solicitudesEnProceso = solicitudesPorEstado.entrySet().stream()
                .filter(entry -> !entry.getKey().esTerminal())
                .mapToLong(Map.Entry::getValue)
                .sum();

            // Solicitudes terminadas
            solicitudesTerminadas = solicitudesPorEstado.entrySet().stream()
                .filter(entry -> entry.getKey().esTerminal())
                .mapToLong(Map.Entry::getValue)
                .sum();

            // Porcentaje de éxito (títulos entregados vs total)
            Long titulosEntregados = solicitudesPorEstado.getOrDefault(
                EstadoSolicitud.TITULO_ENTREGADO, 0L);
            
            if (totalSolicitudes > 0) {
                porcentajeExito = (titulosEntregados.doubleValue() / totalSolicitudes) * 100;
            } else {
                porcentajeExito = 0.0;
            }
        }
    }

    /**
     * Obtiene el estado con mayor número de solicitudes
     */
    public EstadoSolicitud getEstadoConMasSolicitudes() {
        if (solicitudesPorEstado == null || solicitudesPorEstado.isEmpty()) {
            return null;
        }

        return solicitudesPorEstado.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    /**
     * Obtiene el tipo con mayor número de solicitudes
     */
    public TipoSolicitud getTipoConMasSolicitudes() {
        if (solicitudesPorTipo == null || solicitudesPorTipo.isEmpty()) {
            return null;
        }

        return solicitudesPorTipo.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    /**
     * Verifica si hay alertas en las estadísticas
     */
    public Boolean hayAlertas() {
        return (solicitudesConRetraso != null && solicitudesConRetraso > 0) ||
               (solicitudesUrgentes != null && solicitudesUrgentes > 0);
    }
}