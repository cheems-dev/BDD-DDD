package pe.gob.cofopri.catastro.domain.service;

import pe.gob.cofopri.catastro.domain.model.Predio;
import pe.gob.cofopri.catastro.domain.model.PredioId;
import pe.gob.cofopri.catastro.domain.model.Coordenadas;
import pe.gob.cofopri.catastro.domain.repository.PredioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio para validaciones complejas de predios.
 * 
 * Contiene lógica de negocio que involucra múltiples entidades
 * o validaciones que no pertenecen a una sola entidad.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Service
@RequiredArgsConstructor
public class PredioValidationService {

    private final PredioRepository predioRepository;

    /**
     * Valida si un predio puede ser registrado verificando:
     * - Que no exista otro predio con el mismo ID
     * - Que no haya solapamiento geográfico con otros predios
     * 
     * @param predio El predio a validar
     * @throws IllegalStateException Si el predio no puede ser registrado
     */
    public void validarRegistroPredio(Predio predio) {
        // Verificar que no exista otro predio con el mismo ID
        if (predioRepository.existsById(predio.getId())) {
            throw new IllegalStateException(
                String.format("Ya existe un predio con el código %s", predio.getId().getCodigo())
            );
        }

        // Verificar solapamiento geográfico (predios muy cercanos)
        validarSolapamientoGeografico(predio);
    }

    /**
     * Valida que no haya solapamiento geográfico con otros predios
     * 
     * @param predio El predio a validar
     * @throws IllegalStateException Si hay solapamiento
     */
    private void validarSolapamientoGeografico(Predio predio) {
        List<Predio> prediosCercanos = predioRepository.findByCoordenadasEnRadio(
            predio.getCoordenadas(), 50.0 // Radio de 50 metros
        );

        for (Predio predioExistente : prediosCercanos) {
            if (!predioExistente.getId().equals(predio.getId())) {
                double distancia = predio.getCoordenadas()
                    .calcularDistanciaEnMetros(predioExistente.getCoordenadas());
                
                if (distancia < 10.0) { // Menos de 10 metros se considera solapamiento
                    throw new IllegalStateException(
                        String.format("El predio %s está muy cerca del predio existente %s (%.2f metros). " +
                                     "Mínimo requerido: 10 metros",
                                predio.getId().getCodigo(),
                                predioExistente.getId().getCodigo(),
                                distancia)
                    );
                }
            }
        }
    }

    /**
     * Valida si un predio puede cambiar de estado
     * 
     * @param predioId ID del predio
     * @param nuevoEstado Nuevo estado deseado
     * @throws IllegalStateException Si el cambio de estado no es válido
     */
    public void validarCambioEstado(PredioId predioId, Predio.EstadoPredio nuevoEstado) {
        Predio predio = predioRepository.findById(predioId)
            .orElseThrow(() -> new IllegalArgumentException("Predio no encontrado: " + predioId.getCodigo()));

        Predio.EstadoPredio estadoActual = predio.getEstado();

        // Reglas de negocio para cambios de estado
        switch (nuevoEstado) {
            case FORMALIZADO:
                if (estadoActual != Predio.EstadoPredio.ACTIVO && 
                    estadoActual != Predio.EstadoPredio.EN_REVISION) {
                    throw new IllegalStateException(
                        "Solo se puede formalizar un predio que esté ACTIVO o EN_REVISION"
                    );
                }
                break;

            case INACTIVO:
                if (estadoActual == Predio.EstadoPredio.FORMALIZADO) {
                    throw new IllegalStateException(
                        "No se puede inactivar un predio ya FORMALIZADO"
                    );
                }
                break;

            case EN_REVISION:
                if (estadoActual == Predio.EstadoPredio.FORMALIZADO || 
                    estadoActual == Predio.EstadoPredio.INACTIVO) {
                    throw new IllegalStateException(
                        "No se puede poner en revisión un predio FORMALIZADO o INACTIVO"
                    );
                }
                break;

            case SUSPENDIDO:
                if (estadoActual == Predio.EstadoPredio.FORMALIZADO) {
                    throw new IllegalStateException(
                        "No se puede suspender un predio ya FORMALIZADO"
                    );
                }
                break;

            case ACTIVO:
                // ACTIVO es válido desde cualquier estado excepto FORMALIZADO
                if (estadoActual == Predio.EstadoPredio.FORMALIZADO) {
                    throw new IllegalStateException(
                        "No se puede reactivar un predio ya FORMALIZADO"
                    );
                }
                break;
        }
    }

    /**
     * Busca predios potencialmente duplicados basándose en:
     * - Mismo propietario
     * - Coordenadas muy cercanas
     * - Similar dirección
     * 
     * @param predio El predio a verificar
     * @return Lista de predios potencialmente duplicados
     */
    public List<Predio> buscarPotencialesDuplicados(Predio predio) {
        // Buscar predios del mismo propietario
        List<Predio> prediosPropietario = predioRepository.findByPropietario(predio.getPropietario());
        
        return prediosPropietario.stream()
            .filter(p -> !p.getId().equals(predio.getId()))
            .filter(p -> {
                // Verificar si están muy cerca geográficamente
                double distancia = predio.getCoordenadas()
                    .calcularDistanciaEnMetros(p.getCoordenadas());
                return distancia < 100.0; // Menos de 100 metros
            })
            .filter(p -> {
                // Verificar similitud en la dirección (básica)
                String dir1 = predio.getDireccion().toLowerCase().trim();
                String dir2 = p.getDireccion().toLowerCase().trim();
                return dir1.contains(dir2) || dir2.contains(dir1) || 
                       calcularSimilitudDireccion(dir1, dir2) > 0.7;
            })
            .toList();
    }

    /**
     * Calcula similitud básica entre dos direcciones
     * 
     * @param direccion1 Primera dirección
     * @param direccion2 Segunda dirección
     * @return Valor entre 0 y 1 indicando similitud
     */
    private double calcularSimilitudDireccion(String direccion1, String direccion2) {
        String[] palabras1 = direccion1.split("\\s+");
        String[] palabras2 = direccion2.split("\\s+");
        
        int coincidencias = 0;
        for (String palabra1 : palabras1) {
            for (String palabra2 : palabras2) {
                if (palabra1.equals(palabra2) && palabra1.length() > 2) {
                    coincidencias++;
                    break;
                }
            }
        }
        
        return (double) coincidencias / Math.max(palabras1.length, palabras2.length);
    }
}