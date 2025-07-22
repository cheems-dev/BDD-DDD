#!/bin/bash

echo "🚀 Cargando datos de prueba en COFOPRI..."
echo "=========================================="

API_BASE="http://localhost:8080/api"

echo "👥 Creando ciudadanos de prueba..."

# Ciudadano 1 - María García
curl -X POST "$API_BASE/ciudadanos" \
  -H "Content-Type: application/json" \
  -d '{
    "dni": "12345678",
    "nombres": "María Elena",
    "apellidos": "García Rodríguez",
    "fechaNacimiento": "1985-03-15",
    "estadoCivil": "CASADO",
    "sexo": "FEMENINO",
    "direccion": "Av. Los Álamos 123, Miraflores",
    "telefono": "987654321",
    "email": "maria.garcia@email.com"
  }'

echo -e "\n✅ Ciudadano 1 creado"

# Ciudadano 2 - Juan Pérez
curl -X POST "$API_BASE/ciudadanos" \
  -H "Content-Type: application/json" \
  -d '{
    "dni": "87654321",
    "nombres": "Juan Carlos",
    "apellidos": "Pérez Mendoza",
    "fechaNacimiento": "1978-08-22",
    "estadoCivil": "SOLTERO",
    "sexo": "MASCULINO",
    "direccion": "Jirón Huancayo 456, Breña",
    "telefono": "956789123",
    "email": "juan.perez@email.com"
  }'

echo -e "\n✅ Ciudadano 2 creado"

# Ciudadano 3 - Ana Flores
curl -X POST "$API_BASE/ciudadanos" \
  -H "Content-Type: application/json" \
  -d '{
    "dni": "11223344",
    "nombres": "Ana Lucía",
    "apellidos": "Flores Vargas",
    "fechaNacimiento": "1992-12-08",
    "estadoCivil": "UNION_DE_HECHO",
    "sexo": "FEMENINO",
    "direccion": "Calle Las Flores 789, San Borja",
    "telefono": "945123678",
    "email": "ana.flores@email.com"
  }'

echo -e "\n✅ Ciudadano 3 creado"

echo -e "\n🏠 Creando predios de prueba..."

# Predio 1 - Miraflores
curl -X POST "$API_BASE/predios" \
  -H "Content-Type: application/json" \
  -d '{
    "codigoPredio": "150101-001-001-001",
    "propietario": "María Elena García Rodríguez",
    "latitud": -12.1171,
    "longitud": -77.0282,
    "area": 250.50,
    "direccion": "Av. Los Álamos 123, Miraflores, Lima",
    "observaciones": "Predio residencial en zona consolidada"
  }'

echo -e "\n✅ Predio 1 creado"

# Predio 2 - Breña
curl -X POST "$API_BASE/predios" \
  -H "Content-Type: application/json" \
  -d '{
    "codigoPredio": "150102-002-001-001",
    "propietario": "Juan Carlos Pérez Mendoza",
    "latitud": -12.0544,
    "longitud": -77.0543,
    "area": 180.75,
    "direccion": "Jirón Huancayo 456, Breña, Lima",
    "observaciones": "En proceso de verificación catastral"
  }'

echo -e "\n✅ Predio 2 creado"

echo -e "\n📋 Creando solicitudes de prueba..."

# Solicitud 1 - Titulación Individual
curl -X POST "$API_BASE/solicitudes" \
  -H "Content-Type: application/json" \
  -d '{
    "dniSolicitante": "12345678",
    "nombreSolicitante": "María Elena García Rodríguez",
    "tipo": "TITULACION_INDIVIDUAL",
    "direccionPredio": "Av. Los Álamos 123, Miraflores, Lima",
    "observaciones": "Solicitud de titulación individual - documentos completos"
  }'

echo -e "\n✅ Solicitud 1 creada"

# Solicitud 2 - Actualización Catastral
curl -X POST "$API_BASE/solicitudes" \
  -H "Content-Type: application/json" \
  -d '{
    "dniSolicitante": "87654321",
    "nombreSolicitante": "Juan Carlos Pérez Mendoza",
    "tipo": "ACTUALIZACION_CATASTRAL",
    "direccionPredio": "Jirón Huancayo 456, Breña, Lima",
    "observaciones": "Actualización por ampliación de vivienda"
  }'

echo -e "\n✅ Solicitud 2 creada"

# Solicitud 3 - Titulación Colectiva
curl -X POST "$API_BASE/solicitudes" \
  -H "Content-Type: application/json" \
  -d '{
    "dniSolicitante": "11223344",
    "nombreSolicitante": "Ana Lucía Flores Vargas",
    "tipo": "TITULACION_COLECTIVA",
    "direccionPredio": "Calle Las Flores 789, San Borja, Lima",
    "observaciones": "Titulación colectiva para condominio"
  }'

echo -e "\n✅ Solicitud 3 creada"

echo -e "\n🎉 ¡Datos de prueba cargados exitosamente!"
echo "========================================"
echo "🧪 PRUEBA TUS ENDPOINTS:"
echo "• Ciudadanos: curl $API_BASE/ciudadanos/dni/12345678"
echo "• Predios: curl $API_BASE/predios/150101-001-001-001"
echo "• Solicitudes por DNI: curl $API_BASE/solicitudes/dni/12345678"
echo ""
echo "📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "💾 H2 Console: http://localhost:8080/h2-console"
echo "🏠 Home: http://localhost:8080/"