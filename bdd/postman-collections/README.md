# 🧪 COFOPRI - Colecciones BDD Postman

## 📁 Estructura de Archivos

```
postman-collections/
├── COFOPRI_Feature_Gestion_Solicitudes.postman_collection.json    # Feature: Gestión de Solicitudes
├── COFOPRI_Feature_Gestion_Ciudadanos.postman_collection.json     # Feature: Gestión de Ciudadanos  
├── COFOPRI_Feature_Gestion_Predios.postman_collection.json        # Feature: Gestión de Predios
├── COFOPRI_Feature_Gestion_Titulos.postman_collection.json        # Feature: Gestión de Títulos
├── COFOPRI_Local_Development.postman_environment.json             # Environment Variables
└── README.md                                                      # Esta documentación
```

## 🎯 Metodología BDD Implementada

Cada colección implementa **Behavior-Driven Development** con:
- ✅ **Given-When-Then** en cada scenario
- ✅ **Scripts de pre-request** para setup de datos
- ✅ **Scripts de test** para validación de comportamiento
- ✅ **Variables globales** para datos dinámicos
- ✅ **24 scenarios totales** cubriendo casos de éxito y error

## 🚀 Instrucciones de Uso

### 1. Importar a Postman
```bash
# Importar todas las colecciones y environment
# File > Import > Seleccionar todos los archivos .json
```

### 2. Ejecutar con Newman (CLI)
```bash
# Instalar Newman globalmente
npm install -g newman

# Ejecutar colección individual
newman run COFOPRI_Feature_Gestion_Solicitudes.postman_collection.json \
  -e COFOPRI_Local_Development.postman_environment.json

# Ejecutar todas las colecciones
newman run COFOPRI_Feature_Gestion_Solicitudes.postman_collection.json -e COFOPRI_Local_Development.postman_environment.json
newman run COFOPRI_Feature_Gestion_Ciudadanos.postman_collection.json -e COFOPRI_Local_Development.postman_environment.json
newman run COFOPRI_Feature_Gestion_Predios.postman_collection.json -e COFOPRI_Local_Development.postman_environment.json
newman run COFOPRI_Feature_Gestion_Titulos.postman_collection.json -e COFOPRI_Local_Development.postman_environment.json

# Con reporte HTML
newman run COFOPRI_Feature_Gestion_Solicitudes.postman_collection.json \
  -e COFOPRI_Local_Development.postman_environment.json \
  -r html --reporter-html-export results.html
```

### 3. Configurar Environment
- **base_url**: `http://localhost:8080`
- **mock_external_services**: `true` (para RENIEC/SUNARP)
- **debug_mode**: `true`

## 📊 Features y Scenarios

### 🔹 Feature: Gestión de Solicitudes (6 scenarios)
- ✅ Crear solicitud con datos válidos
- ❌ Rechazar solicitud con DNI inválido
- ✅ Consultar solicitud existente
- ❌ Consultar solicitud inexistente
- ✅ Cambiar estado de solicitud
- ✅ Listar solicitudes con filtros

### 👤 Feature: Gestión de Ciudadanos (6 scenarios)  
- ✅ Crear ciudadano con datos válidos
- ❌ Rechazar ciudadano con DNI duplicado
- ✅ Buscar ciudadano por DNI existente
- ❌ Buscar ciudadano por DNI inexistente
- ✅ Verificar identidad ciudadano en RENIEC
- ✅ Actualizar datos de ciudadano

### 🗺️ Feature: Gestión de Predios (6 scenarios)
- ✅ Crear predio con datos válidos
- ❌ Rechazar predio con área insuficiente
- ✅ Georreferenciar predio existente
- ❌ Rechazar georreferenciación con coordenadas inválidas
- ✅ Buscar predios por coordenadas
- ✅ Generar planos de predio georreferenciado

### 📜 Feature: Gestión de Títulos (6 scenarios)
- ✅ Generar título para solicitud aprobada
- ✅ Consultar título por solicitud
- ✅ Enviar título a SUNARP para inscripción
- ✅ Consultar estado de inscripción en SUNARP
- ❌ Rechazar envío a SUNARP por documentos faltantes
- ✅ Actualizar estado título tras inscripción exitosa

## 🔧 Validaciones Implementadas

### Reglas de Negocio COFOPRI
- **DNI**: Formato 8 dígitos, verificación RENIEC
- **Área de predio**: Mínimo 60 m²
- **Coordenadas UTM**: Válidas para Perú (zonas 17S, 18S, 19S)
- **Estados de solicitud**: Máquina de estados válida
- **Integración SUNARP**: Documentos requeridos

### Validaciones Técnicas
- **Response time**: < 5000ms
- **Content-Type**: application/json
- **Status codes**: HTTP estándares
- **Data types**: Validación de tipos de datos
- **Required fields**: Campos obligatorios presentes

## 🎭 Datos de Prueba

Las colecciones usan datos de prueba consistentes:
- **DNI válido**: 12345678, 87654321
- **Coordenadas Lima**: UTM X: 276543.25, Y: 8664123.50
- **Área predio**: 120.50 m²
- **Ubicación**: San Juan de Lurigancho, Lima

## 📈 Ejecución en CI/CD

Para integrar con GitHub Actions u otros pipelines:

```yaml
# Ejemplo para GitHub Actions
- name: Run BDD Tests
  run: |
    npm install -g newman
    newman run postman-collections/COFOPRI_Feature_Gestion_Solicitudes.postman_collection.json \
      -e postman-collections/COFOPRI_Local_Development.postman_environment.json \
      --reporters cli,json --reporter-json-export results.json
```

---

**💡 Nota**: Asegúrate de que tu aplicación Spring Boot esté ejecutándose en `localhost:8080` antes de ejecutar las pruebas.