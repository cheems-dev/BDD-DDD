package pe.gob.cofopri.catastro.application.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para representar estadísticas de predios.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasPrediosDTO {

    private long totalPredios;
    private long prediosActivos;
    private long prediosFormalizados;
    private long prediosEnRevision;
    private long prediosSuspendidos;
    private long prediosInactivos;
    
    public double getPorcentajeFormalizados() {
        return totalPredios > 0 ? (double) prediosFormalizados / totalPredios * 100.0 : 0.0;
    }
    
    public double getPorcentajeActivos() {
        return totalPredios > 0 ? (double) prediosActivos / totalPredios * 100.0 : 0.0;
    }
}