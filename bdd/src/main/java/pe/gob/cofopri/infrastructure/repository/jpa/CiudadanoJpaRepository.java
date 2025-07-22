package pe.gob.cofopri.infrastructure.repository.jpa;

import pe.gob.cofopri.infrastructure.entity.CiudadanoEntity;
import pe.gob.cofopri.infrastructure.entity.CiudadanoEntity.EstadoVerificacion;
import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano;
import pe.gob.cofopri.ciudadanos.domain.model.DNI;
import pe.gob.cofopri.ciudadanos.domain.repository.CiudadanoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación JPA del repository de Ciudadano.
 */

@Repository
@RequiredArgsConstructor
public class CiudadanoJpaRepository implements CiudadanoRepository {

    private final CiudadanoJpaRepositoryInterface jpaRepository;

    @Override
    public Ciudadano save(Ciudadano ciudadano) {
        CiudadanoEntity entity = toEntity(ciudadano);
        CiudadanoEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Ciudadano> findByDni(DNI dni) {
        return jpaRepository.findById(dni.getNumero()).map(this::toDomain);
    }

    @Override
    public Optional<Ciudadano> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public List<Ciudadano> findByEstadoVerificacion(Ciudadano.EstadoVerificacion estado) {
        EstadoVerificacion estadoEntity = EstadoVerificacion.valueOf(estado.name());
        return jpaRepository.findByEstadoVerificacion(estadoEntity)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Ciudadano> findByNombresContaining(String nombres) {
        return jpaRepository.findByNombresContaining(nombres)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Ciudadano> findByDepartamento(String departamento) {
        return jpaRepository.findByDepartamento(departamento)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Ciudadano> findByFechaNacimientoBetween(LocalDate inicio, LocalDate fin) {
        return jpaRepository.findByFechaNacimientoBetween(inicio, fin)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Ciudadano> findCiudadanosQueNecesitanReverificacion() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(90);
        EstadoVerificacion estadoPendiente = EstadoVerificacion.PENDIENTE;
        return jpaRepository.findCiudadanosQueNecesitanReverificacion(estadoPendiente, fechaLimite)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByDni(DNI dni) {
        return jpaRepository.existsById(dni.getNumero());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByEstadoVerificacion(Ciudadano.EstadoVerificacion estado) {
        EstadoVerificacion estadoEntity = EstadoVerificacion.valueOf(estado.name());
        return jpaRepository.countByEstadoVerificacion(estadoEntity);
    }

    @Override
    public List<Ciudadano> findAll() {
        return jpaRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private CiudadanoEntity toEntity(Ciudadano ciudadano) {
        return CiudadanoEntity.builder()
                .dni(ciudadano.getDni().getNumero())
                .nombres(ciudadano.getNombres())
                .apellidos(ciudadano.getApellidos())
                .fechaNacimiento(ciudadano.getFechaNacimiento())
                .estadoCivil(CiudadanoEntity.EstadoCivil.valueOf(ciudadano.getEstadoCivil().name()))
                .sexo(CiudadanoEntity.Sexo.valueOf(ciudadano.getSexo().name()))
                .direccion(ciudadano.getDireccion())
                .distrito(ciudadano.getDistrito())
                .provincia(ciudadano.getProvincia())
                .departamento(ciudadano.getDepartamento())
                .telefono(ciudadano.getTelefono())
                .email(ciudadano.getEmail())
                .estadoVerificacion(CiudadanoEntity.EstadoVerificacion.valueOf(ciudadano.getEstadoVerificacion().name()))
                .fechaRegistro(ciudadano.getFechaRegistro())
                .fechaUltimaActualizacion(ciudadano.getFechaUltimaActualizacion())
                .fechaUltimaVerificacion(ciudadano.getFechaUltimaVerificacion())
                .observaciones(ciudadano.getObservaciones())
                .build();
    }

    private Ciudadano toDomain(CiudadanoEntity entity) {
        return Ciudadano.builder()
                .dni(DNI.of(entity.getDni()))
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .fechaNacimiento(entity.getFechaNacimiento())
                .estadoCivil(Ciudadano.EstadoCivil.valueOf(entity.getEstadoCivil().name()))
                .sexo(Ciudadano.Sexo.valueOf(entity.getSexo().name()))
                .direccion(entity.getDireccion())
                .distrito(entity.getDistrito())
                .provincia(entity.getProvincia())
                .departamento(entity.getDepartamento())
                .telefono(entity.getTelefono())
                .email(entity.getEmail())
                .estadoVerificacion(Ciudadano.EstadoVerificacion.valueOf(entity.getEstadoVerificacion().name()))
                .fechaRegistro(entity.getFechaRegistro())
                .fechaUltimaActualizacion(entity.getFechaUltimaActualizacion())
                .fechaUltimaVerificacion(entity.getFechaUltimaVerificacion())
                .observaciones(entity.getObservaciones())
                .build();
    }
}