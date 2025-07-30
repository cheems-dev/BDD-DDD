# 🏛️ COFOPRI - Sistema de Titulación BDD/DDD

**Sistema de Titulación de Predios Urbanos** implementado con metodología **Behavior-Driven Development (BDD)** y arquitectura **Domain-Driven Design (DDD)** para COFOPRI (Organismo de Formalización de la Propiedad Informal).

## 🎯 Características Principales

- ✅ **Arquitectura DDD** con bounded contexts bien definidos
- ✅ **Testing BDD** con Postman Collections (24 scenarios)
- ✅ **API REST** documentada con OpenAPI 3.0
- ✅ **Domain Models** inmutables con factory methods
- ✅ **Repository Pattern** para persistencia
- ✅ **Value Objects** para validaciones de negocio

## 🏗️ Arquitectura del Sistema

### Bounded Contexts

```
src/main/java/pe/gob/cofopri/
├── catastro/           # Gestión de Predios
├── ciudadanos/         # Gestión de Ciudadanos  
├── solicitudes/        # Procesamiento de Solicitudes
├── infrastructure/     # Capa de infraestructura
└── presentation/       # Controllers REST
```

### Capas DDD

- **Domain Layer**: Entidades, Value Objects, Repository interfaces
- **Application Layer**: Services, DTOs, Commands
- **Infrastructure Layer**: JPA Entities, Repository implementations
- **Presentation Layer**: REST Controllers con documentación BDD

## 🧪 Testing BDD

### Features Implementados

| Feature | Scenarios | Endpoint Base | Estado |
|---------|-----------|---------------|---------|
| **Gestión de Solicitudes** | 6 | `/api/solicitudes` | ✅ Implementado |
| **Gestión de Ciudadanos** | 6 | `/api/ciudadanos` | ✅ Implementado |
| **Gestión de Predios** | 6 | `/api/predios` | ✅ Implementado |
| **Gestión de Títulos** | 6 | `/api/titulos` | ✅ Implementado |

### Scenarios de Ejemplo

```gherkin
# Feature: Gestión de Solicitudes
Scenario: Crear solicitud con datos válidos
  Given un ciudadano con DNI válido "12345678"
  When envío POST /api/solicitudes con datos completos
  Then recibo status 201 Created
  And la respuesta contiene el ID de solicitud generado

Scenario: Rechazar solicitud con DNI inválido  
  Given un DNI inválido "123"
  When envío POST /api/solicitudes
  Then recibo status 400 Bad Request
  And el mensaje indica "DNI debe tener 8 dígitos"
```

## 🚀 Configuración y Ejecución

### Prerrequisitos

- **Java 17+**
- **Maven 3.8+**
- **H2 Database** (incluido) o **PostgreSQL** (opcional)

### Instalación

```bash
# Clonar repositorio
git clone <repository-url>
cd BDD-DDD/bdd

# Compilar proyecto
./mvnw clean compile

# Ejecutar aplicación
./mvnw spring-boot:run
```

### Acceso a la Aplicación

- **API Base**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

## 📊 Ejecutar Pruebas BDD

### Con Postman Collections

```bash
# Instalar Newman
npm install -g newman

# Ejecutar todas las features
cd postman-collections
newman run COFOPRI_Feature_Gestion_Solicitudes.postman_collection.json \
  -e COFOPRI_Local_Development.postman_environment.json

# Con reporte HTML
newman run COFOPRI_Feature_Gestion_Solicitudes.postman_collection.json \
  -e COFOPRI_Local_Development.postman_environment.json \
  -r html --reporter-html-export results.html
```

### Con Maven

```bash
# Pruebas unitarias
./mvnw test

# Pruebas de integración
./mvnw integration-test
```

## 📋 Reglas de Negocio

### Validaciones COFOPRI

- **DNI**: 8 dígitos, verificación con RENIEC
- **Área de Predio**: Mínimo 60 m²
- **Coordenadas UTM**: Válidas para Perú (zonas 17S, 18S, 19S)
- **Estados de Solicitud**: Flujo validado por máquina de estados

### Integraciones Externas

- **RENIEC**: Verificación de identidad ciudadanos
- **SUNARP**: Inscripción de títulos de propiedad

## 🗂️ Estructura del Proyecto

```
bdd/
├── src/main/java/
│   ├── com/example/bdd/           # Configuración Spring Boot
│   └── pe/gob/cofopri/            # Código de dominio
│       ├── catastro/              # BC: Gestión de Predios
│       ├── ciudadanos/            # BC: Gestión de Ciudadanos
│       ├── solicitudes/           # BC: Procesamiento de Solicitudes
│       ├── infrastructure/        # Persistencia JPA
│       └── presentation/          # REST Controllers
├── src/main/resources/
│   └── application.properties     # Configuración Spring
├── src/test/java/                 # Pruebas unitarias
├── documentation/                 # OpenAPI specs
│   ├── openapiSolicitud.yml      # Feature: Gestión de Solicitudes
│   ├── openapiCiudadanos.yml     # Feature: Gestión de Ciudadanos
│   ├── openapiPredios.yml        # Feature: Gestión de Predios
│   └── openapiTitulos.yml        # Feature: Gestión de Títulos
├── postman-collections/          # Pruebas BDD
│   ├── README.md                 # Documentación de testing
│   └── results.html              # Reportes de ejecución
└── pom.xml                       # Dependencias Maven
```

## 🎭 Datos de Prueba

```json
{
  "ciudadanos": {
    "dni_valido": "12345678",
    "nombre": "Juan Pérez García"
  },
  "predios": {
    "area": "120.50",
    "coordenadas": {
      "x": "276543.25",
      "y": "8664123.50"
    },
    "ubicacion": "San Juan de Lurigancho, Lima"
  }
}
```

## 📈 Roadmap

### Próximas Mejoras

- [ ] **Cucumber Integration**: Implementar archivos `.feature` con Gherkin
- [ ] **Step Definitions**: Automatizar scenarios con JUnit 5
- [ ] **Database Migrations**: Flyway para versionado de esquema
- [ ] **Security Layer**: JWT Authentication & Authorization
- [ ] **Monitoring**: Métricas con Micrometer + Prometheus

### Optimizaciones DDD

- [ ] **Event Sourcing**: Para auditoria de cambios de estado
- [ ] **CQRS Pattern**: Separar comandos de consultas
- [ ] **Domain Events**: Comunicación entre bounded contexts

## 🛡️ Consideraciones de Seguridad

- **Validación de entrada**: Todas las APIs validan parámetros
- **Sanitización**: Prevención de inyección SQL/XSS
- **Logs de auditoría**: Trazabilidad de operaciones críticas
- **Datos sensibles**: Nunca expuestos en logs o responses

## 👥 Contribución

1. Fork del repositorio
2. Crear feature branch (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a branch (`git push origin feature/nueva-funcionalidad`)  
5. Crear Pull Request

## 📜 Licencia

Este proyecto es software propietario de **COFOPRI** - Organismo de Formalización de la Propiedad Informal del Perú.

---

**💡 Nota**: Este sistema implementa las mejores prácticas de DDD y BDD para garantizar la calidad y mantenibilidad del código en el dominio de titulación predial.