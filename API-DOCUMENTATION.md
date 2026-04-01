# API Documentation - UQAI Monorepo

## Contenido

- [Credit Card Service (Puerto 8080)](#credit-card-service-puerto-8080)
- [Operations Service (Puerto 8081)](#operations-service-puerto-8081)
- [Modelos Comunes](#modelos-comunes)

---

## Credit Card Service (Puerto 9000)

### Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/creditcards` | Listar todas las tarjetas |
| `GET` | `/api/v1/creditcards/{id}` | Obtener tarjeta por ID |
| `POST` | `/api/v1/creditcards` | Crear nueva tarjeta |
| `PATCH` | `/api/v1/creditcards/{id}/status` | Actualizar estado (ACTIVA/BLOQUEADA) |
| `PATCH` | `/api/v1/creditcards/{id}/balance` | Actualizar saldo (CONSUMO/PAGO) |

---

### 1. GET /api/v1/creditcards

Lista todas las tarjetas de crédito.

**Respuesta (200 OK):**

```json
[
  {
    "id": 1,
    "cardNumber": "****3456",
    "holderName": "Juan Perez",
    "creditLimit": 5000.00,
    "availableBalance": 4500.00,
    "status": "ACTIVA",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

---

### 2. GET /api/v1/creditcards/{id}

Obtiene una tarjeta específica por su ID.

**Parámetros de Path:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | ID de la tarjeta (requerido) |

**Respuesta (200 OK):**

```json
{
  "id": 1,
  "cardNumber": "****3456",
  "holderName": "Juan Perez",
  "creditLimit": 5000.00,
  "availableBalance": 4500.00,
  "status": "ACTIVA",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Error (404 Not Found):**

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Credit card with id 1 not found"
}
```

---

### 3. POST /api/v1/creditcards

Crea una nueva tarjeta de crédito.

**Request Body:**

```json
{
  "cardNumber": "1234567890123456",
  "holderName": "Juan Perez",
  "creditLimit": 5000.00,
  "availableBalance": 5000.00,
  "status": "ACTIVA"
}
```

**Campos del Request:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `cardNumber` | `String` | ✅ Sí | Número de la tarjeta (16 dígitos) |
| `holderName` | `String` | ✅ Sí | Nombre del titular |
| `creditLimit` | `BigDecimal` | ✅ Sí | Límite de crédito máximo |
| `availableBalance` | `BigDecimal` | ✅ Sí | Saldo disponible inicial |
| `status` | `CreditCardStatus` | ✅ Sí | Estado inicial (`ACTIVA` o `BLOQUEADA`) |

**Respuesta (201 Created):**

```json
{
  "id": 1,
  "cardNumber": "****3456",
  "holderName": "Juan Perez",
  "creditLimit": 5000.00,
  "availableBalance": 5000.00,
  "status": "ACTIVA",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Errores:**

| Código | Descripción |
|--------|-------------|
| 400 | Validación fallida (campos inválidos o faltantes) |
| 409 | La tarjeta ya existe |

---

### 4. PATCH /api/v1/creditcards/{id}/status

Actualiza el estado de una tarjeta de crédito.

**Parámetros de Path:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | ID de la tarjeta (requerido) |

**Request Body:**

```json
{
  "status": "BLOQUEADA"
}
```

**Campos del Request:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `status` | `CreditCardStatus` | ✅ Sí | Nuevo estado (`ACTIVA` o `BLOQUEADA`) |

**Valores posibles para `status`:**

| Valor | Descripción |
|-------|-------------|
| `ACTIVA` | Tarjeta activa y disponible para uso |
| `BLOQUEADA` | Tarjeta bloqueada, no se puede operar |

**Respuesta (200 OK):**

```json
{
  "id": 1,
  "cardNumber": "****3456",
  "holderName": "Juan Perez",
  "creditLimit": 5000.00,
  "availableBalance": 4500.00,
  "status": "BLOQUEADA",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:00:00"
}
```

**Errores:**

| Código | Descripción |
|--------|-------------|
| 404 | Tarjeta no encontrada |
| 400 | Validación fallida |

---

### 5. PATCH /api/v1/creditcards/{id}/balance

Actualiza el saldo de una tarjeta de crédito (CONSUMO o PAGO).

**Parámetros de Path:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | ID de la tarjeta (requerido) |

**Request Body:**

```json
{
  "amount": 500.00,
  "operation": "CONSUMO"
}
```

**Campos del Request:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `amount` | `BigDecimal` | ✅ Sí | Monto a operar (debe ser mayor a cero) |
| `operation` | `OperationType` | ✅ Sí | Tipo de operación (`CONSUMO` o `PAGO`) |

**Valores posibles para `operation`:**

| Valor | Descripción | Efecto en el saldo |
|-------|-------------|---------------------|
| `CONSUMO` | El cliente realiza un consumo | Resta del `availableBalance` |
| `PAGO` | El cliente realiza un pago | Suma al `availableBalance` |

**Validaciones:**

- ✅ La tarjeta debe existir (404 si no)
- ✅ La tarjeta debe estar en estado `ACTIVA` (no `BLOQUEADA`)
- ✅ Para `CONSUMO`: debe tener saldo disponible suficiente
- ✅ Para `PAGO`: el resultado no puede exceder el `creditLimit`

**Respuesta (200 OK):**

```json
{
  "id": 1,
  "cardNumber": "****3456",
  "holderName": "Juan Perez",
  "creditLimit": 5000.00,
  "availableBalance": 4000.00,
  "status": "ACTIVA",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:05:00"
}
```

**Ejemplos de uso:**

#### Ejemplo 1: Realizar un consumo de $500

```bash
curl -X PATCH http://localhost:9000/api/v1/creditcards/1/balance \
  -H "Content-Type: application/json" \
  -d '{
        "amount": 500.00,
        "operation": "CONSUMO"
      }'
```

**Resultado:** `availableBalance` baja de 4500.00 a 4000.00

#### Ejemplo 2: Realizar un pago de $1000

```bash
curl -X PATCH http://localhost:9000/api/v1/creditcards/1/balance \
  -H "Content-Type: application/json" \
  -d '{
        "amount": 1000.00,
        "operation": "PAGO"
      }'
```

**Resultado:** `availableBalance` sube de 4000.00 a 5000.00

**Errores:**

| Código | Mensaje | Causa |
|--------|---------|-------|
| 400 | "El monto debe ser mayor a cero" | amount es negativo o cero |
| 400 | "No se puede operar una tarjeta bloqueada" | La tarjeta está en estado BLOQUEADA |
| 400 | "Saldo insuficiente. Disponible: X" | CONSUMO exceeds availableBalance |
| 400 | "El pago excedería el límite de crédito" | PAGO causaría que balance > creditLimit |
| 404 | "Credit card with id X not found" | La tarjeta no existe |

---

## Operations Service (Puerto 9093)

### Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/credit-cards/active` | Listar solo tarjetas activas |
| `POST` | `/api/v1/operations` | Procesar operación (CONSUMO/PAGO) |

---

### 1. GET /api/v1/credit-cards/active

Obtiene todas las tarjetas de crédito que están en estado `ACTIVA`.

Este endpoint consulta al creditcard-service y filtra solo las tarjetas activas.

**Respuesta (200 OK):**

```json
[
  {
    "id": 1,
    "cardNumber": "****3456",
    "holderName": "Juan Perez",
    "creditLimit": 5000.00,
    "availableBalance": 4000.00,
    "status": "ACTIVA",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:05:00"
  },
  {
    "id": 2,
    "cardNumber": "****7890",
    "holderName": "Maria Garcia",
    "creditLimit": 10000.00,
    "availableBalance": 8500.00,
    "status": "ACTIVA",
    "createdAt": "2024-01-10T09:00:00",
    "updatedAt": "2024-01-14T15:30:00"
  }
]
```

**Nota:** Las tarjetas en estado `BLOQUEADA` no aparecen en esta lista.

---

### 2. POST /api/v1/operations

Procesa una operación de consumo o pago en una tarjeta de crédito.

Este endpoint delega al creditcard-service para realizar la operación real.

**Request Body:**

```json
{
  "cardId": 1,
  "amount": 500.00,
  "operation": "CONSUMO"
}
```

**Campos del Request:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `cardId` | `Long` | ✅ Sí | ID de la tarjeta a operar |
| `amount` | `BigDecimal` | ✅ Sí | Monto a operar (debe ser mayor a cero) |
| `operation` | `OperationType` | ✅ Sí | Tipo de operación (`CONSUMO` o `PAGO`) |

**Valores posibles para `operation`:**

| Valor | Descripción | Efecto en el saldo |
|-------|-------------|---------------------|
| `CONSUMO` | El cliente realiza un consumo | Resta del `availableBalance` |
| `PAGO` | El cliente realiza un pago | Suma al `availableBalance` |

**Respuesta (200 OK):**

```json
{
  "cardId": 1,
  "previousBalance": 4500.00,
  "newBalance": 4000.00,
  "amount": 500.00,
  "operation": "CONSUMO",
  "processedAt": "2024-01-15T11:30:00"
}
```

**Campos de la Respuesta:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `cardId` | `Long` | ID de la tarjeta |
| `previousBalance` | `BigDecimal` | Saldo antes de la operación |
| `newBalance` | `BigDecimal` | Saldo después de la operación |
| `amount` | `BigDecimal` | Monto operado |
| `operation` | `OperationType` | Tipo de operación realizada |
| `processedAt` | `LocalDateTime` | Fecha y hora de procesamiento |

**Ejemplos de uso:**

#### Ejemplo 1: Realizar un consumo de $500

```bash
curl -X POST http://localhost:9093/api/v1/operations \
  -H "Content-Type: application/json" \
  -d '{
        "cardId": 1,
        "amount": 500.00,
        "operation": "CONSUMO"
      }'
```

**Resultado:**
- `previousBalance`: 4500.00
- `newBalance`: 4000.00 (4500 - 500)

#### Ejemplo 2: Realizar un pago de $1000

```bash
curl -X POST http://localhost:9093/api/v1/operations \
  -H "Content-Type: application/json" \
  -d '{
        "cardId": 1,
        "amount": 1000.00,
        "operation": "PAGO"
      }'
```

**Resultado:**
- `previousBalance`: 4000.00
- `newBalance`: 5000.00 (4000 + 1000)

**Errores:**

| Código | Mensaje | Causa |
|--------|---------|-------|
| 400 | "El monto debe ser mayor a cero" | amount es negativo o cero |
| 400 | "No se puede operar una tarjeta bloqueada" | La tarjeta está en estado BLOQUEADA |
| 400 | "Saldo insuficiente" | CONSUMO excede availableBalance |
| 400 | "El pago excedería el límite de crédito" | PAGO causaría que balance > creditLimit |
| 500 | Error del servidor | Error interno en creditcard-service |

---

---

## Modelos Comunes

### CreditCardResponseDTO

```json
{
  "id": 1,
  "cardNumber": "****3456",
  "holderName": "Juan Perez",
  "creditLimit": 5000.00,
  "availableBalance": 4000.00,
  "status": "ACTIVA",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:05:00"
}
```

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | Identificador único de la tarjeta |
| `cardNumber` | `String` | Número de tarjeta enmascarado (solo últimos 4 dígitos) |
| `holderName` | `String` | Nombre del titular de la tarjeta |
| `creditLimit` | `BigDecimal` | Límite de crédito máximo |
| `availableBalance` | `BigDecimal` | Saldo disponible actualmente |
| `status` | `CreditCardStatus` | Estado de la tarjeta (`ACTIVA` o `BLOQUEADA`) |
| `createdAt` | `LocalDateTime` | Fecha de creación |
| `updatedAt` | `LocalDateTime` | Fecha de última actualización |

### CreditCardStatus

| Valor | Descripción |
|-------|-------------|
| `ACTIVA` | Tarjeta activa y disponible para operaciones |
| `BLOQUEADA` | Tarjeta bloqueada, no permite operaciones |

### OperationType

| Valor | Descripción | Efecto |
|-------|-------------|--------|
| `CONSUMO` | Operación de consumo | Resta saldo |
| `PAGO` | Operación de pago | Suma saldo |

---

## Códigos de Estado HTTP

| Código | Significado |
|--------|-------------|
| 200 | OK - La solicitud fue exitosa |
| 201 | Created - Recurso creado exitosamente |
| 400 | Bad Request - Error en los datos enviados |
| 404 | Not Found - Recurso no encontrado |
| 409 | Conflict - Conflicto (ej: tarjeta ya existe) |
| 500 | Internal Server Error - Error en el servidor |

---

## Ejemplo Completo de Uso

### Paso 1: Crear una tarjeta

```bash
curl -X POST http://localhost:9000/api/v1/creditcards \
  -H "Content-Type: application/json" \
  -d '{
        "cardNumber": "1234567890123456",
        "holderName": "Juan Perez",
        "creditLimit": 5000.00,
        "availableBalance": 5000.00,
        "status": "ACTIVA"
      }'
```

### Paso 2: Realizar un consumo de $1000

```bash
curl -X PATCH http://localhost:9000/api/v1/creditcards/1/balance \
  -H "Content-Type: application/json" \
  -d '{
        "amount": 1000.00,
        "operation": "CONSUMO"
      }'
```

### Paso 3: Consultar la tarjeta actualizada

```bash
curl http://localhost:9000/api/v1/creditcards/1
```

### Paso 4: Ver todas las tarjetas activas desde operations-service

```bash
curl http://localhost:9093/api/v1/credit-cards/active
```

### Paso 5: Realizar un pago de $500

```bash
curl -X PATCH http://localhost:9000/api/v1/creditcards/1/balance \
  -H "Content-Type: application/json" \
  -d '{
        "amount": 500.00,
        "operation": "PAGO"
      }'
```

### Paso 2: Realizar un consumo de $1000

```bash
curl -X PATCH http://localhost:9000/api/v1/creditcards/1/balance \
  -H "Content-Type: application/json" \
  -d '{
        "amount": 1000.00,
        "operation": "CONSUMO"
      }'
```

### Paso 3: Consultar la tarjeta actualizada

```bash
curl http://localhost:8080/api/v1/creditcards/1
```

### Paso 4: Ver todas las tarjetas activas desde operations-service

```bash
curl http://localhost:8081/api/v1/credit-cards/active
```

### Paso 5: Realizar un pago de $500

```bash
curl -X PATCH http://localhost:9000/api/v1/creditcards/1/balance \
  -H "Content-Type: application/json" \
  -d '{
        "amount": 500.00,
        "operation": "PAGO"
      }'
```