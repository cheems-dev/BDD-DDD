# 🏛️ PROYECTO COFOPRI - CONTEXTO COMPLETO PARA IMPLEMENTACIÓN BDD

## 📋 RESUMEN EJECUTIVO

Estamos implementando un **Sistema de Titulación de Predios Urbanos** para **COFOPRI** (Organismo de Formalización de la Propiedad Informal - Perú) usando **Behavior-Driven Development (BDD)** como metodología de desarrollo y **Domain-Driven Design (DDD)** para la arquitectura.

**Objetivo Principal:** Implementar servicios web REST **guiados por especificación de comportamiento (BDD)**, donde cada funcionalidad se desarrolla mediante pruebas de aceptación Given-When-Then.

**Enfoque Metodológico:** **BDD-First Development** - Escribir primero las pruebas de comportamiento en Postman, luego implementar el código hasta que las pruebas pasen.

---

## 🏢 INFORMACIÓN DE LA ORGANIZACIÓN

### COFOPRI (Cliente)
- **Nombre:** Organismo de Formalización de la Propiedad Informal
- **Sector:** Gobierno del Perú - Ministerio de Vivienda
- **Función:** Formalización de propiedad predial urbana
- **Beneficiarios:** Familias en situación de informalidad predial
- **Integración:** RENIEC (identidad), SUNARP (registro de títulos)

### Proceso de Negocio Principal: "Titulación de Predios Urbanos"
```
FLUJO ACTUAL:
Ciudadano → Mesa de Partes → Evaluación Técnica → Inspección Catastro → 
Análisis Jurídico → Elaboración Título → SUNARP → Entrega Final
```

---

## 🏗️ ARQUITECTURA DISEÑADA

### Stack Tecnológico Definido
- **Lenguaje:** Java 17+
- **Framework Web:** Spring Boot 3.x
- **ORM:** Spring Data JPA + Hibernate
- **Base de Datos:** PostgreSQL (recomendado) o MySQL
- **API:** REST con JSON
- **Testing:** Postman + Newman CLI
- **Repository:** GitHub

### Metodología: BDD (Behavior-Driven Development)
- **Features:** Cada Controller REST es una Feature de comportamiento
- **Scenarios:** Cada método del controller es un escenario Given-When-Then
- **Test-First:** Escribir pruebas de aceptación en Postman ANTES del código
- **Iterativo:** Red → Green → Refactor para cada funcionalidad

### Arquitectura Base: Diagrama UML COFOPRI
**IMPORTANTE:** La implementación debe seguir exactamente el diagrama UML de arquitectura ya diseñado para COFOPRI que incluye:

#### 🌐 Capa de Presentación (package: presentation)
- **SolicitudController**: crearSolicitud(), consultarEstado(), validarDocumentos()
- **CiudadanoController**: buscarCiudadano(), verificarIdentidad()  
- **PredioController**: crearPredio(), obtenerPredio(), georreferenciarPredio(), validarUbicacion(), consultarPorCoordenadas(), generarPlanos()
- **TituloController**: generarTitulo(), entregarTitulo()
- **DTOs**: SolicitudDTO, CiudadanoDTO, PredioDTO, TituloDTO

#### ⚙️ Capa de Aplicación (package: application)
- **ProcesarSolicitudService**: coordinarProcesoCompleto(), cambiarEstados(), consultarProgreso()
- **TitulacionService**: generarTitulos(), enviarASUNARP(), confirmarInscripcion()
- **CatastroApplicationService**: crearPredio(), realizarInspeccion(), georreferenciarPredio(), validarUbicacion()
- **NotificacionService**: enviarEmails(), enviarSMS(), notificarEstados()
- **Use Cases**: ProcesarSolicitud, GenerarTitulo, RealizarInspeccion, NotificarCiudadano

#### 🏛️ Capa de Dominio (package: domain) - 5 Módulos
- **Módulo Solicitudes**: Solicitud (Entity), EstadoSolicitud (Value Object), SolicitudRepository (Interface)
- **Módulo Ciudadanos**: Ciudadano (Entity), DNI (Value Object), CiudadanoRepository (Interface)
- **Módulo Catastro**: Predio (Entity), Coordenadas (Value Object), Area (Value Object), PredioRepository (Interface)
- **Módulo Titulación**: Titulo (Entity), NumeroTitulo (Value Object), EstadoTitulo (Value Object), TituloRepository (Interface)
- **Módulo Comunicaciones**: Notificacion (Entity), CanalComunicacion (Value Object), NotificacionRepository (Interface)

#### 🔧 Capa de Infraestructura (package: infrastructure)
- **Repositories JPA**: SolicitudJPA, CiudadanoJPA, TituloJPA (implementan interfaces del dominio)
- **External Services**: RENIECService (verificarDNI, obtenerDatos), SUNARPService (enviarTitulo, confirmarInscripcion), EmailService, SMSService
- **Configuration**: DatabaseConfig, SecurityConfig, IntegrationConfig
- **JPA Entities**: SolicitudEntity, CiudadanoEntity, PredioEntity, TituloEntity

### Patrón Arquitectónico: DDD + Clean Architecture

```
🌐 CAPA PRESENTACIÓN (presentation)
├── Controllers REST (SolicitudController, CiudadanoController, etc.)
├── DTOs (SolicitudDTO, CiudadanoDTO, etc.)
└── Validators

⚙️ CAPA APLICACIÓN (application) 
├── Application Services (ProcesarSolicitudService, TitulacionService)
├── Use Cases (ProcesarSolicitud, GenerarTitulo, RealizarInspeccion)
└── Commands & Queries

🏛️ CAPA DOMINIO (domain)
├── Módulo Solicitudes (Solicitud, EstadoSolicitud, SolicitudRepository)
├── Módulo Ciudadanos (Ciudadano, DNI, CiudadanoRepository) 
├── Módulo Catastro (Predio, Coordenadas, PredioRepository)
├── Módulo Titulación (Titulo, NumeroTitulo, TituloRepository)
├── Módulo Comunicaciones (Notificacion, CanalComunicacion)
└── Shared Kernel (Events, Exceptions)

🔧 CAPA INFRAESTRUCTURA (infrastructure)
├── Repositories JPA (SolicitudJpaRepository, CiudadanoJpaRepository)
├── External Services (RENIECService, SUNARPService, EmailService)
├── Configuration (DatabaseConfig, SecurityConfig)
└── Entities JPA (SolicitudEntity, CiudadanoEntity, etc.)
```

---

## 🎯 BOUNDED CONTEXTS (Módulos DDD) IDENTIFICADOS

### 1. 📋 MÓDULO SOLICITUDES
**Responsabilidad:** Gestión del ciclo de vida de solicitudes de titulación
- **Entities:** Solicitud (Aggregate Root)
- **Value Objects:** EstadoSolicitud (RECIBIDA, EN_EVALUACION, APROBADA, RECHAZADA)
- **Repository:** SolicitudRepository
- **Domain Service:** SolicitudDomainService
- **Reglas de Negocio:** 
  - Estados válidos de transición
  - Validación de documentos mínimos
  - Tiempos máximos por estado

### 2. 👤 MÓDULO CIUDADANOS  
**Responsabilidad:** Gestión de datos personales y verificación de identidad
- **Entities:** Ciudadano (Aggregate Root)
- **Value Objects:** DNI, Email, Telefono
- **Repository:** CiudadanoRepository
- **Domain Service:** VerificacionIdentidadService
- **Reglas de Negocio:**
  - Validación formato DNI peruano (8 dígitos)
  - Verificación obligatoria con RENIEC
  - Unicidad por DNI

### 3. 🗺️ MÓDULO CATASTRO
**Responsabilidad:** Gestión de información predial y georreferenciación
- **Entities:** Predio (Aggregate Root)
- **Value Objects:** Coordenadas, Area, Direccion
- **Repository:** PredioRepository  
- **Domain Service:** CatastroDomainService
- **Reglas de Negocio:**
  - Coordenadas UTM válidas para Perú
  - Área mínima 60 m²
  - Sin superposiciones geográficas

### 4. 📜 MÓDULO TITULACIÓN
**Responsabilidad:** Generación y gestión de títulos de propiedad
- **Entities:** Titulo (Aggregate Root)
- **Value Objects:** NumeroTitulo, EstadoTitulo
- **Repository:** TituloRepository
- **Domain Service:** TitulacionDomainService  
- **Reglas de Negocio:**
  - Numeración única secuencial
  - Integración obligatoria con SUNARP
  - Estados: GENERADO, ENVIADO_SUNARP, INSCRITO

### 5. 📞 MÓDULO COMUNICACIONES
**Responsabilidad:** Notificaciones y comunicación con ciudadanos
- **Entities:** Notificacion (Aggregate Root)
- **Value Objects:** CanalComunicacion (EMAIL, SMS, PRESENCIAL)
- **Repository:** NotificacionRepository
- **Domain Service:** ServicioNotificacion

---

## 🛠️ SERVICIOS WEB REST IDENTIFICADOS

### API Endpoints Principales

#### 🔹 SolicitudController
```
POST   /api/solicitudes              - Crear nueva solicitud
GET    /api/solicitudes/{id}         - Obtener solicitud por ID  
PUT    /api/solicitudes/{id}         - Actualizar solicitud
GET    /api/solicitudes              - Listar solicitudes (con filtros)
PUT    /api/solicitudes/{id}/estado  - Cambiar estado
DELETE /api/solicitudes/{id}         - Eliminar solicitud
```

#### 🔹 CiudadanoController
```
POST   /api/ciudadanos               - Crear ciudadano
GET    /api/ciudadanos/{id}          - Obtener por ID
GET    /api/ciudadanos/dni/{dni}     - Buscar por DNI
PUT    /api/ciudadanos/{id}          - Actualizar datos
POST   /api/ciudadanos/{id}/verificar - Verificar identidad RENIEC
```

#### 🔹 PredioController  
```
POST   /api/predios                  - Crear predio
GET    /api/predios/{id}             - Obtener predio
PUT    /api/predios/{id}             - Actualizar predio
POST   /api/predios/{id}/georreferenciar - Asignar coordenadas
GET    /api/predios/coordenadas      - Buscar por ubicación
POST   /api/predios/{id}/planos      - Generar planos
```

#### 🔹 TituloController
```
POST   /api/titulos                  - Crear título
GET    /api/titulos/{id}             - Obtener título  
GET    /api/titulos/solicitud/{solicitudId} - Por solicitud
POST   /api/titulos/{id}/sunarp      - Enviar a SUNARP
GET    /api/titulos/{id}/estado      - Consultar estado registro
```

---

## 📁 ESTRUCTURA DE PAQUETES JAVA

```
src/main/java/pe/gob/cofopri/titulacion/
├── presentation/
│   ├── controller/
│   │   ├── SolicitudController.java
│   │   ├── CiudadanoController.java  
│   │   ├── PredioController.java
│   │   └── TituloController.java
│   ├── dto/
│   │   ├── SolicitudDTO.java
│   │   ├── CiudadanoDTO.java
│   │   └── ...
│   └── validator/
│       └── SolicitudValidator.java
├── application/
│   ├── service/
│   │   ├── ProcesarSolicitudService.java
│   │   ├── TitulacionService.java
│   │   └── CatastroApplicationService.java
│   ├── usecase/
│   │   ├── ProcesarSolicitudUseCase.java
│   │   └── GenerarTituloUseCase.java
│   └── dto/
│       └── commands/
├── domain/
│   ├── solicitudes/
│   │   ├── Solicitud.java           # Aggregate Root
│   │   ├── EstadoSolicitud.java     # Value Object
│   │   ├── SolicitudRepository.java  # Interface
│   │   └── SolicitudDomainService.java
│   ├── ciudadanos/
│   │   ├── Ciudadano.java
│   │   ├── DNI.java                 # Value Object
│   │   └── CiudadanoRepository.java
│   ├── catastro/
│   │   ├── Predio.java
│   │   ├── Coordenadas.java         # Value Object
│   │   └── PredioRepository.java
│   ├── titulacion/
│   │   ├── Titulo.java
│   │   ├── NumeroTitulo.java        # Value Object
│   │   └── TituloRepository.java
│   └── shared/
│       ├── DomainEvent.java
│       └── exceptions/
└── infrastructure/
    ├── repository/
    │   ├── SolicitudJpaRepository.java
    │   ├── CiudadanoJpaRepository.java
    │   └── ...
    ├── external/
    │   ├── RENIECServiceImpl.java
    │   ├── SUNARPServiceImpl.java
    │   └── EmailServiceImpl.java
    ├── config/
    │   ├── DatabaseConfig.java
    │   └── SecurityConfig.java
    └── entity/
        ├── SolicitudEntity.java      # JPA Entity
        ├── CiudadanoEntity.java
        └── ...
```

---

## 🧪 METODOLOGÍA BDD (Behavior-Driven Development)

### Enfoque Principal: BDD-First Development

**Flujo de desarrollo:**
1. **📝 Feature Definition:** Definir comportamiento esperado de cada controller
2. **🧪 Write Tests First:** Crear pruebas Postman con Given-When-Then
3. **❌ Red:** Ejecutar pruebas (fallarán inicialmente)
4. **✅ Green:** Implementar código mínimo para que pasen
5. **🔄 Refactor:** Mejorar código manteniendo pruebas verdes

### Estructura BDD: Controllers como Features

#### 🔹 Feature: Gestión de Solicitudes (SolicitudController)
```gherkin
Feature: Como ciudadano quiero gestionar mis solicitudes de titulación

Scenario: Crear solicitud exitosa
  GIVEN tengo datos válidos de ciudadano y predio
  WHEN envío POST /api/solicitudes
  THEN recibo status 201 y el ID de la solicitud creada
  AND la solicitud tiene estado "RECIBIDA"

Scenario: Rechazar solicitud con datos inválidos
  GIVEN tengo un DNI con formato inválido
  WHEN envío POST /api/solicitudes  
  THEN recibo status 400
  AND mensaje de error descriptivo

Scenario: Consultar estado de solicitud
  GIVEN existe una solicitud con ID válido
  WHEN envío GET /api/solicitudes/{id}
  THEN recibo status 200
  AND todos los datos de la solicitud
```

#### 🔹 Feature: Gestión de Ciudadanos (CiudadanoController)
```gherkin  
Feature: Como sistema necesito gestionar datos de ciudadanos

Scenario: Verificar identidad en RENIEC
  GIVEN tengo un ciudadano con DNI "12345678"
  WHEN envío POST /api/ciudadanos/{id}/verificar
  THEN consulto RENIEC automáticamente
  AND actualizo estado de verificación
  AND recibo confirmación de verificación
```

#### 🔹 Feature: Gestión de Predios (PredioController)
```gherkin
Feature: Como técnico de catastro necesito gestionar predios

Scenario: Georreferenciar predio
  GIVEN tengo un predio sin coordenadas
  WHEN envío POST /api/predios/{id}/georreferenciar
  THEN valido que las coordenadas estén en zona válida del Perú
  AND actualizo el predio con coordenadas UTM
  AND marco el predio como georreferenciado
```

### Implementación en Postman: Given-When-Then

#### Scripts de Prueba BDD
```javascript
// GIVEN: Pre-request Script
pm.globals.set("dni_valido", "12345678");
pm.globals.set("direccion_valida", "Av. Los Olivos 123");

// WHEN: Request URL y Body
POST {{base_url}}/api/solicitudes
{
  "ciudadano": {
    "dni": "{{dni_valido}}", 
    "nombres": "Juan Pérez"
  },
  "predio": {
    "direccion": "{{direccion_valida}}"
  }
}

// THEN: Post-response Script  
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response has solicitud ID", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.globals.set("solicitud_id", jsonData.id);
});

pm.test("Initial state is RECIBIDA", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.estado).to.eql("RECIBIDA");
});
```

#### Ejemplo de Feature: Gestión de Solicitudes
```gherkin
Feature: Gestionar Solicitudes de Titulación
  Como ciudadano peruano
  Quiero solicitar la titulación de mi predio
  Para obtener seguridad jurídica sobre mi propiedad

Scenario: Crear solicitud con datos válidos
  Given tengo un ciudadano verificado con DNI "12345678"
  And tengo un predio válido en "Av. Los Olivos 123, Lima"
  When envío POST /api/solicitudes con datos completos
  Then recibo status code 201
  And recibo el ID de la solicitud creada
  And la solicitud tiene estado "RECIBIDA"

Scenario: Rechazar solicitud con datos inválidos  
  Given tengo datos con DNI inválido "1234567X"
  When envío POST /api/solicitudes
  Then recibo status code 400
  And recibo mensaje de error "DNI debe tener formato válido"
```

#### Estructura Postman para BDD
```
COFOPRI Workspace/
├── 📁 Collections/
│   ├── 📋 FEATURE: Gestión de Solicitudes/
│   │   ├── 🧪 Scenario: Crear solicitud válida
│   │   ├── 🧪 Scenario: Crear solicitud inválida  
│   │   ├── 🧪 Scenario: Obtener solicitud existente
│   │   ├── 🧪 Scenario: Obtener solicitud inexistente
│   │   └── 🧪 Scenario: Cambiar estado solicitud
│   ├── 👤 FEATURE: Gestión de Ciudadanos/
│   │   ├── 🧪 Scenario: Crear ciudadano válido
│   │   ├── 🧪 Scenario: Verificar identidad RENIEC
│   │   └── 🧪 Scenario: Buscar por DNI
│   ├── 🗺️ FEATURE: Gestión de Predios/
│   └── 📜 FEATURE: Gestión de Títulos/
└── 🌍 Environments/
    ├── 💻 Local Development (localhost:8080)
    ├── 🧪 Testing Environment  
    └── 🚀 Staging Environment

Variables Globales por Environment:
- base_url: http://localhost:8080/api
- timeout: 5000
- content_type: application/json
```

---

## 📋 DATOS DE DOMINIO ESPECÍFICOS

### Reglas de Negocio Críticas de COFOPRI

#### Estados de Solicitud (Máquina de Estados)
```
RECIBIDA → EN_EVALUACION → INSPECCION_PENDIENTE → 
ANALISIS_JURIDICO → TITULO_GENERADO → ENVIADO_SUNARP → 
INSCRITO → TITULO_ENTREGADO

Estados Terminales: RECHAZADA, ARCHIVADA
```

#### Validaciones de DNI Peruano
- Formato: Exactamente 8 dígitos numéricos
- Verificación obligatoria con RENIEC
- Unicidad en el sistema

#### Coordenadas UTM para Perú  
- Zonas válidas: 17S, 18S, 19S (principalmente)
- Rango aproximado X: 160,000 - 800,000
- Rango aproximado Y: 8,000,000 - 9,680,000

#### Integraciones Externas
- **RENIEC:** Verificación de identidad ciudadana
- **SUNARP:** Inscripción de títulos de propiedad
- **Email/SMS:** Notificaciones automáticas

---

## 🎯 OBJETIVOS DEL LABORATORIO BDD

### Lo que debemos implementar con metodología BDD:

1. **✅ Features definidas en formato Given-When-Then** para cada controller
2. **✅ Pruebas de aceptación Postman** escritas ANTES del código
3. **✅ Proyecto Spring Boot** con estructura DDD para soporte arquitectónico
4. **✅ Controllers REST** implementados hasta que pruebas BDD pasen
5. **✅ Application Services** que coordinen la lógica de negocio
6. **✅ Mocks de servicios externos** (RENIEC/SUNARP) para pruebas
7. **✅ Repositorio GitHub** con collections Postman exportadas
8. **✅ Documentación BDD** con features y escenarios

### Criterios de Éxito BDD:
- **🧪 Todas las pruebas Postman pasan** (GREEN)
- **📝 Cada controller tiene scenarios bien definidos** 
- **🔄 Código implementado cumple comportamiento especificado**
- **📋 Features cubren casos felices y casos de error**
- **⚡ Pruebas ejecutables por Newman CLI**
- **🔗 Collections Postman exportables y versionables**

### Diferenciación de Roles:
- **🎭 BDD:** Metodología de desarrollo (Given-When-Then, tests primero)
- **🏗️ DDD:** Patrón arquitectónico (capas, módulos, entities)
- **✅ TDD:** Técnica a nivel unitario (opcional, complementaria)
- **🧪 Postman:** Herramienta para pruebas de aceptación BDD

---

## 🚀 PRÓXIMOS PASOS BDD-FIRST

### Metodología de Implementación:

#### FASE 1: Setup y Features
1. **Generar proyecto base** Spring Boot con dependencias
2. **Crear estructura de paquetes** según arquitectura DDD
3. **Definir Features principales** (Solicitudes, Ciudadanos, Predios, Títulos)
4. **Setup Postman** con environments y collections por feature

#### FASE 2: BDD Development Cycle (Por cada Feature)
1. **📝 Escribir escenarios BDD** en formato Given-When-Then
2. **🧪 Crear pruebas Postman** para cada escenario
3. **❌ Ejecutar pruebas** (fallarán - RED)
4. **⚙️ Implementar controller mínimo** para pasar pruebas
5. **✅ Ejecutar pruebas** hasta que pasen (GREEN)  
6. **🔄 Refactorizar código** manteniendo pruebas verdes
7. **📚 Repetir** para siguiente escenario

#### FASE 3: Integración y Deploy
1. **Configurar JPA** y persistencia real
2. **Integrar servicios externos** (mocks inicialmente)
3. **Setup GitHub** con branches y documentación
4. **CI/CD** con Newman para ejecutar pruebas automáticamente

### Orden de Implementación Sugerido:
1. **🥇 SOLICITUDES** (Feature más crítica)
   - Crear solicitud
   - Consultar solicitud  
   - Cambiar estado
2. **🥈 CIUDADANOS** (Dependencia de solicitudes)
   - Crear ciudadano
   - Verificar identidad
3. **🥉 PREDIOS** (Información catastral)
   - Crear predio
   - Georreferenciar
4. **🏅 TÍTULOS** (Proceso final)
   - Generar título
   - Enviar a SUNARP

---

## 📚 REFERENCIAS TÉCNICAS

- **Spring Boot:** https://spring.io/projects/spring-boot
- **Spring Data JPA:** https://spring.io/projects/spring-data-jpa
- **DDD con Spring:** https://www.baeldung.com/hexagonal-architecture-ddd-spring
- **Postman Testing:** https://learning.postman.com/docs/tests-and-scripts/
- **Clean Architecture:** Robert C. Martin - Clean Architecture book

---

**💡 NOTAS IMPORTANTES BDD:**
- **🧪 TESTS FIRST:** Escribir pruebas Postman ANTES que el código
- **📝 Features claras:** Cada controller = una feature con escenarios específicos
- **🔄 Ciclo Red-Green-Refactor:** Pruebas fallan → implementar → pruebas pasan → refactorizar
- **🎯 Behavior-focused:** Concentrarse en COMPORTAMIENTO esperado, no en implementación
- **📋 Given-When-Then:** Formato estricto para todos los escenarios
- **⚡ Newman compatible:** Collections ejecutables por línea de comandos
- **🚀 Mocks iniciales:** Usar mocks para dependencias externas (RENIEC/SUNARP)
- **📚 Documentación viva:** Las pruebas BDD sirven como documentación del comportamiento

---

*Documento generado para Claude Code - Implementación BDD del Sistema COFOPRI*
*Metodología: Behavior-Driven Development + Arquitectura DDD*