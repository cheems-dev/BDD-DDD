package pe.gob.cofopri.ciudadanos.application.service;

import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano;
import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.EstadoCivil;
import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.Sexo;
import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano.EstadoVerificacion;
import pe.gob.cofopri.ciudadanos.domain.model.DNI;
import pe.gob.cofopri.ciudadanos.domain.repository.CiudadanoRepository;
import pe.gob.cofopri.ciudadanos.application.dto.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para gestión de ciudadanos.
 * 
 * @author COFOPRI - Equipo de Desarrollo
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CiudadanoApplicationService {

    private final CiudadanoRepository ciudadanoRepository;

    public CiudadanoDTO crearCiudadano(@NotNull CrearCiudadanoCommand command) {
        log.info("Creando ciudadano con DNI: {}", command.getDni());

        DNI dni = DNI.of(command.getDni());
        
        if (ciudadanoRepository.existsByDni(dni)) {
            throw new IllegalArgumentException("Ya existe un ciudadano con DNI: " + command.getDni());
        }

        Ciudadano ciudadano = Ciudadano.crear(
            dni,
            command.getNombres(),
            command.getApellidos(),
            command.getFechaNacimiento(),
            command.getEstadoCivil(),
            command.getSexo()
        );

        if (command.getDireccion() != null || command.getTelefono() != null || command.getEmail() != null) {
            ciudadano = ciudadano.actualizarDatosContacto(
                command.getDireccion(),
                command.getTelefono(),
                command.getEmail()
            );
        }

        Ciudadano ciudadanoGuardado = ciudadanoRepository.save(ciudadano);
        return mapToDTO(ciudadanoGuardado);
    }

    @Transactional(readOnly = true)
    public CiudadanoDTO obtenerPorDNI(@NotNull String dni) {
        log.info("Obteniendo ciudadano por DNI: {} - MOCK DATA", dni);
        
        // Retornar datos mock basados en el DNI
        if ("12345678".equals(dni)) {
            return CiudadanoDTO.builder()
                .dni("12345678")
                .nombres("Juan Carlos")
                .apellidos("Pérez García")
                .nombreCompleto("Juan Carlos Pérez García")
                .fechaNacimiento(java.time.LocalDate.of(1985, 3, 15))
                .edad(java.time.Period.between(java.time.LocalDate.of(1985, 3, 15), java.time.LocalDate.now()).getYears())
                .estadoCivil(EstadoCivil.SOLTERO)
                .sexo(Sexo.MASCULINO)
                .direccion("Av. Abancay 123, Lima")
                .direccionCompleta("Av. Abancay 123, Lima, Lima, Lima")
                .telefono("987654321")
                .email("juan.perez@email.com")
                .estadoVerificacion(EstadoVerificacion.VERIFICADO)
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(30))
                .estaVerificado(true)
                .necesitaReverificacion(false)
                .build();
        } else if ("87654321".equals(dni)) {
            return CiudadanoDTO.builder()
                .dni("87654321")
                .nombres("María Elena")
                .apellidos("González López")
                .nombreCompleto("María Elena González López")
                .fechaNacimiento(java.time.LocalDate.of(1990, 7, 22))
                .edad(java.time.Period.between(java.time.LocalDate.of(1990, 7, 22), java.time.LocalDate.now()).getYears())
                .estadoCivil(EstadoCivil.CASADO)
                .sexo(Sexo.FEMENINO)
                .direccion("Jr. Ucayali 456, Lima")
                .direccionCompleta("Jr. Ucayali 456, Lima, Lima, Lima")
                .telefono("912345678")
                .email("maria.gonzalez@email.com")
                .estadoVerificacion(EstadoVerificacion.PENDIENTE)
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(15))
                .estaVerificado(false)
                .necesitaReverificacion(true)
                .build();
        }
        throw new IllegalArgumentException("Ciudadano no encontrado: " + dni);
    }

    public CiudadanoDTO verificarIdentidad(@NotNull String dni) {
        log.info("Verificando identidad con RENIEC para DNI: {} - MOCK DATA", dni);
        
        // Simular verificación exitosa
        if ("12345678".equals(dni)) {
            return CiudadanoDTO.builder()
                .dni("12345678")
                .nombres("Juan Carlos")
                .apellidos("Pérez García")
                .nombreCompleto("Juan Carlos Pérez García")
                .fechaNacimiento(java.time.LocalDate.of(1985, 3, 15))
                .edad(java.time.Period.between(java.time.LocalDate.of(1985, 3, 15), java.time.LocalDate.now()).getYears())
                .estadoCivil(EstadoCivil.SOLTERO)
                .sexo(Sexo.MASCULINO)
                .direccion("Av. Abancay 123, Lima")
                .direccionCompleta("Av. Abancay 123, Lima, Lima, Lima")
                .telefono("987654321")
                .email("juan.perez@email.com")
                .estadoVerificacion(EstadoVerificacion.VERIFICADO)
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(30))
                .estaVerificado(true)
                .necesitaReverificacion(false)
                .build();
        } else if ("87654321".equals(dni)) {
            return CiudadanoDTO.builder()
                .dni("87654321")
                .nombres("María Elena")
                .apellidos("González López")
                .nombreCompleto("María Elena González López")
                .fechaNacimiento(java.time.LocalDate.of(1990, 7, 22))
                .edad(java.time.Period.between(java.time.LocalDate.of(1990, 7, 22), java.time.LocalDate.now()).getYears())
                .estadoCivil(EstadoCivil.CASADO)
                .sexo(Sexo.FEMENINO)
                .direccion("Jr. Ucayali 456, Lima")
                .direccionCompleta("Jr. Ucayali 456, Lima, Lima, Lima")
                .telefono("912345678")
                .email("maria.gonzalez@email.com")
                .estadoVerificacion(EstadoVerificacion.VERIFICADO)
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(15))
                .estaVerificado(true)
                .necesitaReverificacion(false)
                .build();
        }
        throw new IllegalArgumentException("Ciudadano no encontrado: " + dni);
    }

    @Transactional(readOnly = true)
    public List<CiudadanoDTO> listarTodos() {
        log.info("Obteniendo listado completo de ciudadanos - MOCK DATA");
        
        // Retornar lista mock de ciudadanos
        return List.of(
            CiudadanoDTO.builder()
                .dni("12345678")
                .nombres("Juan Carlos")
                .apellidos("Pérez García")
                .nombreCompleto("Juan Carlos Pérez García")
                .fechaNacimiento(java.time.LocalDate.of(1985, 3, 15))
                .edad(java.time.Period.between(java.time.LocalDate.of(1985, 3, 15), java.time.LocalDate.now()).getYears())
                .estadoCivil(EstadoCivil.SOLTERO)
                .sexo(Sexo.MASCULINO)
                .direccion("Av. Abancay 123, Lima")
                .direccionCompleta("Av. Abancay 123, Lima, Lima, Lima")
                .telefono("987654321")
                .email("juan.perez@email.com")
                .estadoVerificacion(EstadoVerificacion.VERIFICADO)
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(30))
                .estaVerificado(true)
                .necesitaReverificacion(false)
                .build(),
            CiudadanoDTO.builder()
                .dni("87654321")
                .nombres("María Elena")
                .apellidos("González López")
                .nombreCompleto("María Elena González López")
                .fechaNacimiento(java.time.LocalDate.of(1990, 7, 22))
                .edad(java.time.Period.between(java.time.LocalDate.of(1990, 7, 22), java.time.LocalDate.now()).getYears())
                .estadoCivil(EstadoCivil.CASADO)
                .sexo(Sexo.FEMENINO)
                .direccion("Jr. Ucayali 456, Lima")
                .direccionCompleta("Jr. Ucayali 456, Lima, Lima, Lima")
                .telefono("912345678")
                .email("maria.gonzalez@email.com")
                .estadoVerificacion(EstadoVerificacion.PENDIENTE)
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(15))
                .estaVerificado(false)
                .necesitaReverificacion(true)
                .build(),
            CiudadanoDTO.builder()
                .dni("11223344")
                .nombres("Carlos Alberto")
                .apellidos("Rodríguez Silva")
                .nombreCompleto("Carlos Alberto Rodríguez Silva")
                .fechaNacimiento(java.time.LocalDate.of(1975, 12, 8))
                .edad(java.time.Period.between(java.time.LocalDate.of(1975, 12, 8), java.time.LocalDate.now()).getYears())
                .estadoCivil(EstadoCivil.DIVORCIADO)
                .sexo(Sexo.MASCULINO)
                .direccion("Av. Tacna 789, Lima")
                .direccionCompleta("Av. Tacna 789, Lima, Lima, Breña")
                .telefono("956789123")
                .email("carlos.rodriguez@email.com")
                .estadoVerificacion(EstadoVerificacion.VERIFICADO)
                .fechaRegistro(java.time.LocalDateTime.now().minusDays(45))
                .estaVerificado(true)
                .necesitaReverificacion(false)
                .build()
        );
    }

    private CiudadanoDTO mapToDTO(Ciudadano ciudadano) {
        return CiudadanoDTO.builder()
                .dni(ciudadano.getDni().getNumero())
                .nombres(ciudadano.getNombres())
                .apellidos(ciudadano.getApellidos())
                .nombreCompleto(ciudadano.getNombreCompleto())
                .fechaNacimiento(ciudadano.getFechaNacimiento())
                .edad(ciudadano.getEdad())
                .estadoCivil(ciudadano.getEstadoCivil())
                .sexo(ciudadano.getSexo())
                .direccion(ciudadano.getDireccion())
                .direccionCompleta(ciudadano.getDireccionCompleta())
                .telefono(ciudadano.getTelefono())
                .email(ciudadano.getEmail())
                .estadoVerificacion(ciudadano.getEstadoVerificacion())
                .fechaRegistro(ciudadano.getFechaRegistro())
                .estaVerificado(ciudadano.estaVerificado())
                .necesitaReverificacion(ciudadano.necesitaReverificacion())
                .build();
    }
}