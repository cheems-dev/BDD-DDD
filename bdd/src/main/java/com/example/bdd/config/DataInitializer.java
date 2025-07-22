package com.example.bdd.config;

import pe.gob.cofopri.ciudadanos.domain.model.Ciudadano;
import pe.gob.cofopri.ciudadanos.domain.model.DNI;
import pe.gob.cofopri.ciudadanos.domain.repository.CiudadanoRepository;
import pe.gob.cofopri.catastro.domain.model.Predio;
import pe.gob.cofopri.catastro.domain.model.PredioId;
import pe.gob.cofopri.catastro.domain.model.Coordenadas;
import pe.gob.cofopri.catastro.domain.repository.PredioRepository;
import pe.gob.cofopri.solicitudes.domain.model.Solicitud;
import pe.gob.cofopri.solicitudes.domain.model.SolicitudId;
import pe.gob.cofopri.solicitudes.domain.repository.SolicitudRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

/**
 * Componente que inicializa datos de prueba para el sistema COFOPRI.
 * Se ejecuta automáticamente al iniciar la aplicación.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CiudadanoRepository ciudadanoRepository;
    private final PredioRepository predioRepository;
    private final SolicitudRepository solicitudRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 Iniciando carga de datos de prueba...");
        
        // Crear ciudadanos de prueba
        crearCiudadanosDePrueba();
        
        // Crear predios de prueba
        crearPrediosDePrueba();
        
        // Crear solicitudes de prueba
        crearSolicitudesDePrueba();
        
        log.info("✅ Datos de prueba cargados exitosamente");
        log.info("📊 Estadísticas:");
        log.info("   - Ciudadanos: {}", ciudadanoRepository.count());
        log.info("   - Predios: {}", predioRepository.count());
        log.info("   - Solicitudes: {}", solicitudRepository.count());
    }

    private void crearCiudadanosDePrueba() {
        log.info("👥 Creando ciudadanos de prueba...");
        
        List<Ciudadano> ciudadanos = Arrays.asList(
            // Ciudadano 1 - María García
            Ciudadano.builder()
                .dni(DNI.of("12345678"))
                .nombres("María Elena")
                .apellidos("García Rodríguez")
                .fechaNacimiento(LocalDate.of(1985, 3, 15))
                .estadoCivil(Ciudadano.EstadoCivil.CASADO)
                .sexo(Ciudadano.Sexo.FEMENINO)
                .direccion("Av. Los Álamos 123, Miraflores")
                .distrito("Miraflores")
                .provincia("Lima")
                .departamento("Lima")
                .telefono("987654321")
                .email("maria.garcia@email.com")
                .estadoVerificacion(Ciudadano.EstadoVerificacion.VERIFICADO)
                .fechaRegistro(LocalDateTime.now().minusDays(30))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(5))
                .fechaUltimaVerificacion(LocalDateTime.now().minusDays(5))
                .observaciones("Ciudadana verificada con RENIEC")
                .build(),

            // Ciudadano 2 - Juan Pérez
            Ciudadano.builder()
                .dni(DNI.of("87654321"))
                .nombres("Juan Carlos")
                .apellidos("Pérez Mendoza")
                .fechaNacimiento(LocalDate.of(1978, 8, 22))
                .estadoCivil(Ciudadano.EstadoCivil.SOLTERO)
                .sexo(Ciudadano.Sexo.MASCULINO)
                .direccion("Jirón Huancayo 456, Breña")
                .distrito("Breña")
                .provincia("Lima")
                .departamento("Lima")
                .telefono("956789123")
                .email("juan.perez@email.com")
                .estadoVerificacion(Ciudadano.EstadoVerificacion.PENDIENTE)
                .fechaRegistro(LocalDateTime.now().minusDays(15))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(3))
                .observaciones("Pendiente de verificación RENIEC")
                .build(),

            // Ciudadano 3 - Ana Flores
            Ciudadano.builder()
                .dni(DNI.of("11223344"))
                .nombres("Ana Lucía")
                .apellidos("Flores Vargas")
                .fechaNacimiento(LocalDate.of(1992, 12, 8))
                .estadoCivil(Ciudadano.EstadoCivil.UNION_DE_HECHO)
                .sexo(Ciudadano.Sexo.FEMENINO)
                .direccion("Calle Las Flores 789, San Borja")
                .distrito("San Borja")
                .provincia("Lima")
                .departamento("Lima")
                .telefono("945123678")
                .email("ana.flores@email.com")
                .estadoVerificacion(Ciudadano.EstadoVerificacion.VERIFICADO)
                .fechaRegistro(LocalDateTime.now().minusDays(45))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(10))
                .fechaUltimaVerificacion(LocalDateTime.now().minusDays(10))
                .observaciones("Verificada exitosamente")
                .build(),

            // Ciudadano 4 - Roberto Silva
            Ciudadano.builder()
                .dni(DNI.of("55667788"))
                .nombres("Roberto")
                .apellidos("Silva Quispe")
                .fechaNacimiento(LocalDate.of(1965, 5, 30))
                .estadoCivil(Ciudadano.EstadoCivil.VIUDO)
                .sexo(Ciudadano.Sexo.MASCULINO)
                .direccion("Av. Arequipa 1010, Lince")
                .distrito("Lince")
                .provincia("Lima")
                .departamento("Lima")
                .telefono("934567890")
                .email("roberto.silva@email.com")
                .estadoVerificacion(Ciudadano.EstadoVerificacion.ERROR_VERIFICACION)
                .fechaRegistro(LocalDateTime.now().minusDays(20))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(2))
                .observaciones("Error en verificación - documentos inconsistentes")
                .build(),

            // Ciudadano 5 - Carmen López
            Ciudadano.builder()
                .dni(DNI.of("99887766"))
                .nombres("Carmen Rosa")
                .apellidos("López Huamán")
                .fechaNacimiento(LocalDate.of(1988, 11, 14))
                .estadoCivil(Ciudadano.EstadoCivil.DIVORCIADO)
                .sexo(Ciudadano.Sexo.FEMENINO)
                .direccion("Jr. Cusco 234, Cercado de Lima")
                .distrito("Cercado de Lima")
                .provincia("Lima")
                .departamento("Lima")
                .telefono("923456789")
                .email("carmen.lopez@email.com")
                .estadoVerificacion(Ciudadano.EstadoVerificacion.VERIFICADO)
                .fechaRegistro(LocalDateTime.now().minusDays(60))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(15))
                .fechaUltimaVerificacion(LocalDateTime.now().minusDays(15))
                .observaciones("Ciudadana con historial de titulación previa")
                .build()
        );

        for (Ciudadano ciudadano : ciudadanos) {
            if (!ciudadanoRepository.existsByDni(ciudadano.getDni())) {
                ciudadanoRepository.save(ciudadano);
                log.info("   ✓ Ciudadano creado: {} {}", ciudadano.getNombres(), ciudadano.getApellidos());
            }
        }
    }

    private void crearPrediosDePrueba() {
        log.info("🏠 Creando predios de prueba...");
        
        List<Predio> predios = Arrays.asList(
            // Predio 1 - Lima Miraflores
            Predio.builder()
                .id(PredioId.of("15-01-01-001-001"))
                .propietario("María Elena García Rodríguez")
                .coordenadas(Coordenadas.of(-12.1171, -77.0282)) // Miraflores
                .area(BigDecimal.valueOf(250.50))
                .direccion("Av. Los Álamos 123, Miraflores, Lima")
                .estado(Predio.EstadoPredio.ACTIVO)
                .fechaRegistro(LocalDateTime.now().minusDays(30))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(5))
                .observaciones("Predio residencial en zona consolidada")
                .build(),

            // Predio 2 - Lima Breña
            Predio.builder()
                .id(PredioId.of("15-01-02-002-001"))
                .propietario("Juan Carlos Pérez Mendoza")
                .coordenadas(Coordenadas.of(-12.0544, -77.0543)) // Breña
                .area(BigDecimal.valueOf(180.75))
                .direccion("Jirón Huancayo 456, Breña, Lima")
                .estado(Predio.EstadoPredio.EN_REVISION)
                .fechaRegistro(LocalDateTime.now().minusDays(25))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(3))
                .observaciones("En proceso de verificación catastral")
                .build(),

            // Predio 3 - Lima San Borja
            Predio.builder()
                .id(PredioId.of("15-01-03-003-001"))
                .propietario("Ana Lucía Flores Vargas")
                .coordenadas(Coordenadas.of(-12.1034, -76.9958)) // San Borja
                .area(BigDecimal.valueOf(320.25))
                .direccion("Calle Las Flores 789, San Borja, Lima")
                .estado(Predio.EstadoPredio.FORMALIZADO)
                .fechaRegistro(LocalDateTime.now().minusDays(90))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(60))
                .observaciones("Predio formalizado - título registrado en SUNARP")
                .build(),

            // Predio 4 - Lima Lince
            Predio.builder()
                .id(PredioId.of("15-01-04-004-001"))
                .propietario("Roberto Silva Quispe")
                .coordenadas(Coordenadas.of(-12.0894, -77.0350)) // Lince
                .area(BigDecimal.valueOf(195.80))
                .direccion("Av. Arequipa 1010, Lince, Lima")
                .estado(Predio.EstadoPredio.SUSPENDIDO)
                .fechaRegistro(LocalDateTime.now().minusDays(40))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(10))
                .observaciones("Suspendido por conflicto de linderos")
                .build(),

            // Predio 5 - Lima Cercado
            Predio.builder()
                .id(PredioId.of("15-01-05-005-001"))
                .propietario("Carmen Rosa López Huamán")
                .coordenadas(Coordenadas.of(-12.0464, -77.0428)) // Cercado de Lima
                .area(BigDecimal.valueOf(275.90))
                .direccion("Jr. Cusco 234, Cercado de Lima, Lima")
                .estado(Predio.EstadoPredio.ACTIVO)
                .fechaRegistro(LocalDateTime.now().minusDays(60))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(20))
                .observaciones("Predio en zona histórica - requiere permisos especiales")
                .build(),

            // Predio 6 - Predio rural en Cusco
            Predio.builder()
                .id(PredioId.of("08-01-01-001-002"))
                .propietario("Comunidad Campesina Anta")
                .coordenadas(Coordenadas.of(-13.5170, -72.0855)) // Cusco
                .area(BigDecimal.valueOf(1250.00))
                .direccion("Sector Huaylla, Distrito de Anta, Cusco")
                .estado(Predio.EstadoPredio.ACTIVO)
                .fechaRegistro(LocalDateTime.now().minusDays(120))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(30))
                .observaciones("Predio rural comunitario - uso agropecuario")
                .build()
        );

        for (Predio predio : predios) {
            if (!predioRepository.existsById(predio.getId())) {
                predioRepository.save(predio);
                log.info("   ✓ Predio creado: {} - {}", predio.getId().getCodigo(), predio.getPropietario());
            }
        }
    }

    private void crearSolicitudesDePrueba() {
        log.info("📋 Creando solicitudes de prueba...");
        
        List<Solicitud> solicitudes = Arrays.asList(
            // Solicitud 1 - Titulación Individual
            Solicitud.builder()
                .id(SolicitudId.generar(2024, 1234))
                .dniSolicitante("12345678")
                .nombreSolicitante("María Elena García Rodríguez")
                .tipo(Solicitud.TipoSolicitud.TITULACION_INDIVIDUAL)
                .estado(Solicitud.EstadoSolicitud.EN_EVALUACION)
                .direccionPredio("Av. Los Álamos 123, Miraflores, Lima")
                .numeroExpediente("EXP-2024-001234")
                .prioridad(1)
                .fechaRegistro(LocalDateTime.now().minusDays(20))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(2))
                .observaciones("Solicitud de titulación individual - documentos completos")
                .documentosAdjuntos(List.of("DNI", "Declaración Jurada", "Plano de Ubicación"))
                .build(),

            // Solicitud 2 - Actualización Catastral
            Solicitud.builder()
                .id(SolicitudId.generar(2024, 1235))
                .dniSolicitante("87654321")
                .nombreSolicitante("Juan Carlos Pérez Mendoza")
                .tipo(Solicitud.TipoSolicitud.ACTUALIZACION_CATASTRAL)
                .estado(Solicitud.EstadoSolicitud.INSPECCION_PENDIENTE)
                .direccionPredio("Jirón Huancayo 456, Breña, Lima")
                .numeroExpediente("EXP-2024-001235")
                .prioridad(2)
                .fechaRegistro(LocalDateTime.now().minusDays(15))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(1))
                .observaciones("Actualización por ampliación de vivienda")
                .documentosAdjuntos(List.of("DNI", "Licencia de Construcción", "Plano Actualizado"))
                .build(),

            // Solicitud 3 - Titulación Colectiva
            Solicitud.builder()
                .id(SolicitudId.generar(2024, 1236))
                .dniSolicitante("11223344")
                .nombreSolicitante("Ana Lucía Flores Vargas")
                .tipo(Solicitud.TipoSolicitud.TITULACION_COLECTIVA)
                .estado(Solicitud.EstadoSolicitud.TITULO_GENERADO)
                .direccionPredio("Calle Las Flores 789, San Borja, Lima")
                .numeroExpediente("EXP-2024-001236")
                .prioridad(1)
                .fechaRegistro(LocalDateTime.now().minusDays(45))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(5))
                .observaciones("Titulación colectiva para condominio - en proceso de entrega")
                .documentosAdjuntos(List.of("DNI", "Acta de Asamblea", "Plano de Conjunto", "Reglamento Interno"))
                .build(),

            // Solicitud 4 - Subdivisión
            Solicitud.builder()
                .id(SolicitudId.generar(2024, 1237))
                .dniSolicitante("55667788")
                .nombreSolicitante("Roberto Silva Quispe")
                .tipo(Solicitud.TipoSolicitud.SUBDIVISION)
                .estado(Solicitud.EstadoSolicitud.EN_SUBSANACION)
                .direccionPredio("Av. Arequipa 1010, Lince, Lima")
                .numeroExpediente("EXP-2024-001237")
                .prioridad(3)
                .fechaRegistro(LocalDateTime.now().minusDays(30))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(3))
                .observaciones("Subdivisión requiere documentos adicionales")
                .documentosAdjuntos(List.of("DNI", "Plano de Subdivisión"))
                .build(),

            // Solicitud 5 - Fusión
            Solicitud.builder()
                .id(SolicitudId.generar(2024, 1238))
                .dniSolicitante("99887766")
                .nombreSolicitante("Carmen Rosa López Huamán")
                .tipo(Solicitud.TipoSolicitud.FUSION)
                .estado(Solicitud.EstadoSolicitud.ANALISIS_JURIDICO)
                .direccionPredio("Jr. Cusco 234, Cercado de Lima, Lima")
                .numeroExpediente("EXP-2024-001238")
                .prioridad(2)
                .fechaRegistro(LocalDateTime.now().minusDays(25))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(4))
                .observaciones("Fusión de dos lotes contiguos - en análisis jurídico")
                .documentosAdjuntos(List.of("DNI", "Títulos de ambos predios", "Plano de Fusión"))
                .build(),

            // Solicitud 6 - Rechazada
            Solicitud.builder()
                .id(SolicitudId.generar(2024, 1239))
                .dniSolicitante("12345678")
                .nombreSolicitante("María Elena García Rodríguez")
                .tipo(Solicitud.TipoSolicitud.TITULACION_INDIVIDUAL)
                .estado(Solicitud.EstadoSolicitud.RECHAZADA)
                .direccionPredio("Predio sin dirección válida")
                .numeroExpediente("EXP-2024-001239")
                .prioridad(3)
                .fechaRegistro(LocalDateTime.now().minusDays(50))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(35))
                .observaciones("Rechazada por documentación insuficiente e incorrecta")
                .documentosAdjuntos(List.of("DNI"))
                .build(),

            // Solicitud 7 - Titulo Entregado
            Solicitud.builder()
                .id(SolicitudId.generar(2023, 5678))
                .dniSolicitante("11223344")
                .nombreSolicitante("Ana Lucía Flores Vargas")
                .tipo(Solicitud.TipoSolicitud.TITULACION_INDIVIDUAL)
                .estado(Solicitud.EstadoSolicitud.TITULO_ENTREGADO)
                .direccionPredio("Otro predio de Ana Flores")
                .numeroExpediente("EXP-2023-005678")
                .prioridad(1)
                .fechaRegistro(LocalDateTime.now().minusDays(180))
                .fechaUltimaActualizacion(LocalDateTime.now().minusDays(120))
                .observaciones("Proceso completado exitosamente - título entregado")
                .documentosAdjuntos(List.of("DNI", "Declaración Jurada", "Plano", "Título de Propiedad"))
                .build()
        );

        for (Solicitud solicitud : solicitudes) {
            solicitudRepository.save(solicitud);
            log.info("   ✓ Solicitud creada: {} - {} ({})", 
                solicitud.getId().getCodigo(), 
                solicitud.getTipo(), 
                solicitud.getEstado());
        }
    }
}