# UQAI Monorepo - Credit Card & Operations Services

Monorepo con dos microservicios Spring Boot usando Arquitectura Hexagonal.

## Documentation

- **[Architecture Documentation](docs/architecture/ARCHITECTURE.md)** - C4 Model diagrams and system architecture overview
- **[API Documentation](API-DOCUMENTATION.md)** - Complete API reference with examples

---

Monorepo con dos microservicios Spring Boot usando Arquitectura Hexagonal:

- **creditcard-service** (puerto 9000): Gestión de tarjetas de crédito
- **operations-service** (puerto 9093): Consulta de tarjetas activas y procesamiento de operaciones

Ambos servicios comparten la misma base de datos PostgreSQL en Railway.

---

## Requisitos Previos

- Java 17+
- Maven 3.8+
- PostgreSQL (configurado en Railway)

---

## Ejecutar los Servicios

### Opción 1: Desde IntelliJ IDEA

1. Abrir el proyecto en IntelliJ IDEA
2. Ejecutar `com.example.Application` en **creditcard-service** (puerto 9000)
3. Ejecutar `com.example.Application` en **operations-service** (puerto 9093)

### Opción 2: Desde terminal (Maven)

**Terminal 1 - creditcard-service:**
```bash
cd creditcard-service
mvn spring-boot:run
```

**Terminal 2 - operations-service:**
```bash
cd operations-service
mvn spring-boot:run
```

---

## Endpoints Disponibles

### creditcard-service (puerto 9000)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `http://localhost:9000/api/v1/creditcards` | Listar todas las tarjetas |
| `GET` | `http://localhost:9000/api/v1/creditcards/{id}` | Obtener tarjeta por ID |
| `POST` | `http://localhost:9000/api/v1/creditcards` | Crear nueva tarjeta |
| `PATCH` | `http://localhost:9000/api/v1/creditcards/{id}/status` | Actualizar estado |
| `PATCH` | `http://localhost:9000/api/v1/creditcards/{id}/balance` | Actualizar saldo |

### operations-service (puerto 9093)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `http://localhost:9093/api/v1/credit-cards/active` | Listar solo tarjetas activas |
| `POST` | `http://localhost:9093/api/v1/operations` | Procesar CONSUMO/PAGO |

---

## Ejemplos de Uso

### 1. Crear una tarjeta (creditcard-service)

```bash
curl -X POST http://localhost:9000/api/v1/creditcards \
  -H "Content-Type: application/json" \
  -d '{
        "cardNumber": "4532123456789012",
        "holderName": "Juan Perez",
        "creditLimit": 5000.00,
        "availableBalance": 5000.00,
        "status": "ACTIVA"
      }'
```

### 2. Ver todas las tarjetas activas (operations-service)

```bash
curl http://localhost:9093/api/v1/credit-cards/active
```

### 3. Realizar un consumo de $500 (operations-service)

```bash
curl -X POST http://localhost:9093/api/v1/operations \
  -H "Content-Type: application/json" \
  -d '{
        "cardId": 1,
        "amount": 500.00,
        "operation": "CONSUMO",
        "description": "Compra en tienda"
      }'
```

### 4. Realizar un pago de $200 (operations-service)

```bash
curl -X POST http://localhost:9093/api/v1/operations \
  -H "Content-Type: application/json" \
  -d '{
        "cardId": 1,
        "amount": 200.00,
        "operation": "PAGO",
        "description": "Pago de tarjeta"
      }'
```

### 5. Actualizar estado de tarjeta (creditcard-service)

```bash
curl -X PATCH http://localhost:9000/api/v1/creditcards/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "BLOQUEADA"}'
```

---

## Configuración

### Base de Datos (Railway PostgreSQL)

Los servicios están configurados para conectarse a Railway:
- **Host:** switchback.proxy.rlwy.net
- **Puerto:** 16602
- **Base de datos:** railway
- **Usuario:** postgres
- **Contraseña:** tBEcFMqrsXUQjPqrzkkCjVRMPyOBVsWo

### Puertos

| Servicio | Puerto |
|----------|--------|
| creditcard-service | 9000 |
| operations-service | 9093 |

---

## Estructura del Proyecto

```
uqai-monorepo/
├── creditcard-service/        # Microservicio de tarjetas
│   └── src/main/java/com/example/
│       ├── domain/           # Modelo de dominio
│       ├── application/      # Casos de uso (ports & services)
│       └── infrastructure/  # Adaptadores (REST, JPA)
├── operations-service/       # Microservicio de operaciones
│   └── src/main/java/com/example/
│       ├── domain/
│       ├── application/
│       └── infrastructure/
├── docker-compose.yml        # Orquestación de contenedores
├── API-DOCUMENTATION.md     # Documentación completa
└── .env.example             # Variables de entorno
```