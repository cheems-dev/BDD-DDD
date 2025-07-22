package pe.gob.cofopri.infrastructure.repository.jpa;

import pe.gob.cofopri.infrastructure.entity.PredioEntity;
import pe.gob.cofopri.infrastructure.entity.PredioEntity.EstadoPredio;
import pe.gob.cofopri.catastro.domain.model.Predio;
import pe.gob.cofopri.catastro.domain.model.PredioId;
import pe.gob.cofopri.catastro.domain.model.Coordenadas;
import pe.gob.cofopri.catastro.domain.repository.PredioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación JPA del repository de Predio.
 */
@Repository
@RequiredArgsConstructor
public class PredioJpaRepository implements PredioRepository {

    private final PredioJpaRepositoryInterface jpaRepository;

    @Override
    public Predio save(Predio predio) {
        PredioEntity entity = toEntity(predio);
        PredioEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Predio> findById(PredioId id) {
        return jpaRepository.findById(id.getCodigo()).map(this::toDomain);
    }

    @Override
    public List<Predio> findByPropietario(String propietario) {
        return jpaRepository.findByPropietarioContaining(propietario)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Predio> findByEstado(Predio.EstadoPredio estado) {
        EstadoPredio estadoEntity = EstadoPredio.valueOf(estado.name());
        return jpaRepository.findByEstado(estadoEntity)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Predio> findByCoordenadasEnRadio(Coordenadas coordenadas, double radioEnMetros) {
        // Implementación simplificada - en producción usar funciones geoespaciales
        BigDecimal lat = coordenadas.getLatitud();
        BigDecimal lon = coordenadas.getLongitud();
        BigDecimal radioDecimal = BigDecimal.valueOf(radioEnMetros / 111000); // Aproximación simple
        
        return jpaRepository.findByCoordenadaXBetweenAndCoordenadaYBetween(
                lat.subtract(radioDecimal), lat.add(radioDecimal),
                lon.subtract(radioDecimal), lon.add(radioDecimal))
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Predio> findByDepartamento(String codigoDepartamento) {
        // Implementación simplificada - buscar por ID que contenga el código
        return jpaRepository.findAll()
                .stream()
                .filter(entity -> entity.getId().startsWith(codigoDepartamento))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Predio> findByProvincia(String codigoDepartamento, String codigoProvincia) {
        String prefijo = codigoDepartamento + codigoProvincia;
        return jpaRepository.findAll()
                .stream()
                .filter(entity -> entity.getId().startsWith(prefijo))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Predio> findByDistrito(String codigoDepartamento, String codigoProvincia, String codigoDistrito) {
        String prefijo = codigoDepartamento + codigoProvincia + codigoDistrito;
        return jpaRepository.findAll()
                .stream()
                .filter(entity -> entity.getId().startsWith(prefijo))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Predio> findAll() {
        return jpaRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }


    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByEstado(Predio.EstadoPredio estado) {
        EstadoPredio estadoEntity = EstadoPredio.valueOf(estado.name());
        return jpaRepository.countByEstado(estadoEntity);
    }

    @Override
    public void deleteById(PredioId id) {
        jpaRepository.deleteById(id.getCodigo());
    }

    @Override
    public boolean existsById(PredioId id) {
        return jpaRepository.existsById(id.getCodigo());
    }

    private PredioEntity toEntity(Predio predio) {
        return PredioEntity.builder()
                .id(predio.getId().getCodigo())
                .propietario(predio.getPropietario())
                .coordenadaX(predio.getCoordenadas().getLatitud())
                .coordenadaY(predio.getCoordenadas().getLongitud())
                .area(predio.getArea())
                .direccion(predio.getDireccion())
                .estado(EstadoPredio.valueOf(predio.getEstado().name()))
                .fechaRegistro(predio.getFechaRegistro())
                .fechaUltimaActualizacion(predio.getFechaUltimaActualizacion())
                .observaciones(predio.getObservaciones())
                .build();
    }

    private Predio toDomain(PredioEntity entity) {
        return Predio.builder()
                .id(PredioId.of(entity.getId()))
                .propietario(entity.getPropietario())
                .coordenadas(new Coordenadas(entity.getCoordenadaX(), entity.getCoordenadaY()))
                .area(entity.getArea())
                .direccion(entity.getDireccion())
                .estado(Predio.EstadoPredio.valueOf(entity.getEstado().name()))
                .fechaRegistro(entity.getFechaRegistro())
                .fechaUltimaActualizacion(entity.getFechaUltimaActualizacion())
                .observaciones(entity.getObservaciones())
                .build();
    }
}