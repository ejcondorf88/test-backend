# UQAI Credit Card System Architecture

This document describes the architecture of the UQAI Credit Card System using the [C4 Model](https://c4model.com/), a visual approach to documenting software architecture at different levels of abstraction.

## About the C4 Model

The C4 Model provides a way to describe software architecture through four levels:
- **Level 1: System Context** - Shows the system as a box and its relationships with users and external systems
- **Level 2: Containers** - Shows the high-level technology choices and how responsibilities are distributed
- **Level 3: Components** - Shows the internal structure of containers and their interactions
- **Level 4: Code** (not documented here) - Represents the actual source code

---

## Level 1 - System Context

The System Context diagram shows the UQAI Credit Card System as a black box and its relationships with external actors and systems.

```mermaid
flowchart TB
    subgraph Actors
        cardHolder[👤 Card Holder<br/>Customer who owns credit cards and performs transactions]
        admin[👤 Administrator<br/>Bank staff who manages credit cards and monitors operations]
    end

    subgraph Systems
        uqaiSystem[🏦 UQAI Credit Card System<br/>Manages credit cards, balances, status, and transaction processing]
        style uqaiSystem fill:#1168bd,stroke:#0b4884,color:#fff
    end

    subgraph External
        paymentProcessor["🌐 Payment Processors<br/>External payment networks (Visa, Mastercard, etc.)"]
        style paymentProcessor fill:#999,stroke:#666,stroke-dasharray: 5 5
    end

    cardHolder -->|Views balance, makes payments, checks transactions<br/>HTTPS/REST| uqaiSystem
    admin -->|Creates cards, updates status, views all cards<br/>HTTPS/REST| uqaiSystem
    uqaiSystem -->|Processes transactions<br/>HTTPS/REST| paymentProcessor
```

### Key Actors

| Actor | Description | Interactions |
|-------|-------------|--------------|
| **Card Holder** | Customer who owns credit cards | Views balance, makes payments, checks transactions |
| **Administrator** | Bank staff | Creates cards, updates status, views all cards |
| **Payment Processors** | External payment networks | Receives transaction processing requests |

---

## Level 2 - Containers

The Container diagram shows the high-level technology stack and how the system is structured into deployable units (containers).

```mermaid
flowchart TB
    subgraph Users
        cardHolder[👤 Card Holder<br/>Customer who owns credit cards]
        admin[👤 Administrator<br/>Bank staff]
    end

    subgraph "UQAI Credit Card System"
        creditcardService[📦 creditcard-service<br/>Spring Boot 3.2, Java 17<br/>Manages credit card CRUD operations, balance updates, and status changes]
        style creditcardService fill:#438dd5,stroke:#2e6295,color:#fff

        operationsService["📦 operations-service<br/>Spring Boot 3.2, Java 17<br/>Processes transactions (CONSUMO/PAGO) and queries active cards"]
        style operationsService fill:#438dd5,stroke:#2e6295,color:#fff

        postgresDB[("🗄️ PostgreSQL Database<br/>PostgreSQL 15.x<br/>Stores credit card data and operations")]
        style postgresDB fill:#438dd5,stroke:#2e6295,color:#fff
    end

    paymentProcessor["🌐 Payment Processors<br/>External payment networks"]
    style paymentProcessor fill:#999,stroke:#666,stroke-dasharray: 5 5

    cardHolder -->|Makes payments and consumption<br/>HTTPS/REST| operationsService
    cardHolder -->|"Views card details and balance<br/>HTTPS/REST<br/>GET /api/v1/creditcards/{id}"| creditcardService
    admin -->|"Manages cards (CRUD, status)<br/>HTTPS/REST<br/>POST/PATCH /api/v1/creditcards"| creditcardService
    admin -->|Queries active cards<br/>HTTPS/REST<br/>GET /api/v1/creditcards/active| operationsService

    creditcardService -->|Reads/Writes card data<br/>JDBC<br/>port 5432| postgresDB
    operationsService -->|Reads card data<br/>JDBC<br/>port 5432| postgresDB
    operationsService -->|Updates balance via HTTP<br/>HTTP REST<br/>port 9000| creditcardService
    operationsService -->|Sends transactions<br/>HTTPS/REST| paymentProcessor
```

### Technology Stack

| Container | Technology | Port | Responsibilities |
|-----------|------------|------|------------------|
| **creditcard-service** | Spring Boot 3.2, Java 17 | 9000 | Credit card CRUD, balance management, status updates |
| **operations-service** | Spring Boot 3.2, Java 17 | 9093 | Transaction processing, active card queries |
| **PostgreSQL Database** | PostgreSQL 15.x | 5432 | Persistence layer for both services |

### Communication Patterns

- **Card Holders** interact with both services via REST API
- **Administrators** manage cards through creditcard-service and query data through operations-service
- **operations-service** communicates with **creditcard-service** via HTTP REST to update card balances
- Both services share the same **PostgreSQL database** via JDBC

---

## Level 3 - Components

The Component diagrams show the internal structure of each service, following Hexagonal Architecture principles.

### creditcard-service Components

```mermaid
flowchart TB
    subgraph Users
        admin[👤 Administrator<br/>Bank staff]
        cardHolder[👤 Card Holder<br/>Customer]
    end

    subgraph "creditcard-service [Port 9000]"
        subgraph InfrastructureLayer["🔧 Infrastructure Layer"]
            restController[CreditCardController<br/>Spring REST Controller<br/>Handles HTTP requests: GET, POST, PATCH /api/v1/creditcards]
            restMapper[CreditCardRestMapper<br/>MapStruct Mapper<br/>Maps between DTOs and Domain models]
            persistenceAdapter[CreditCardPersistenceAdapter<br/>JPA Adapter<br/>Implements LoadCreditCardPort and SaveCreditCardPort]
            jpaRepo[CreditCardJpaRepository<br/>Spring Data JPA<br/>JPA repository interface for database access]
            persistenceMapper[CreditCardPersistenceMapper<br/>MapStruct Mapper<br/>Maps between Entity and Domain models]
            globalException[GlobalExceptionHandler<br/>Spring @ControllerAdvice<br/>Handles domain exceptions and returns HTTP errors]
        end
        style InfrastructureLayer fill:#e3f2fd,stroke:#2196f3

        subgraph ApplicationLayer["⚙️ Application Layer"]
            createService[CreateCreditCardService<br/>Spring @Service<br/>Implements CreateCreditCardUseCase]
            getService[GetCreditCardService<br/>Spring @Service<br/>Implements GetCreditCardUseCase]
            updateStatusService[UpdateCreditCardStatusService<br/>Spring @Service<br/>Implements UpdateCreditCardStatusUseCase]
            updateBalanceService[UpdateBalanceService<br/>Spring @Service<br/>Implements UpdateBalanceUseCase]
        end
        style ApplicationLayer fill:#fff3e0,stroke:#ff9800

        subgraph DomainLayer["💎 Domain Layer"]
            creditCard[CreditCard<br/>Domain Entity<br/>Core business entity with cardNumber, balance, status, limits]
            cardStatus[CreditCardStatus<br/>Enum<br/>ACTIVA, BLOQUEADA]
            operationType[OperationType<br/>Enum<br/>CONSUMO, PAGO]
            cardRepo[CreditCardRepository<br/>Repository Interface<br/>Port for persistence operations]
        end
        style DomainLayer fill:#f3e5f5,stroke:#9c27b0
    end

    postgresDB[("🗄️ PostgreSQL Database<br/>PostgreSQL 15.x<br/>Stores credit card entities")]

    admin -->|Makes requests<br/>HTTPS/REST| restController
    cardHolder -->|Views card<br/>HTTPS/REST| restController

    restController -->|Uses<br/>CreateCreditCardUseCase| createService
    restController -->|Uses<br/>GetCreditCardUseCase| getService
    restController -->|Uses<br/>UpdateCreditCardStatusUseCase| updateStatusService
    restController -->|Uses<br/>UpdateBalanceUseCase| updateBalanceService
    restController -->|Maps DTOs<br/>MapStruct| restMapper

    createService -->|Persists<br/>SaveCreditCardPort| cardRepo
    getService -->|Queries<br/>LoadCreditCardPort| cardRepo
    updateStatusService -->|Updates<br/>SaveCreditCardPort| cardRepo
    updateBalanceService -->|Updates<br/>SaveCreditCardPort| cardRepo

    cardRepo -->|Implemented by<br/>Adapter Pattern| persistenceAdapter
    persistenceAdapter -->|Uses<br/>Spring Data JPA| jpaRepo
    persistenceAdapter -->|Maps entities<br/>MapStruct| persistenceMapper
    jpaRepo -->|Persists<br/>JDBC| postgresDB
```

#### Architecture Layers

1. **Infrastructure Layer**: REST Controllers, Exception Handlers, and Persistence Adapters
   - `CreditCardController`: Handles HTTP requests and delegates to services
   - `CreditCardPersistenceAdapter`: Implements repository ports using JPA
   - Mappers: Convert between DTOs, Entities, and Domain models

2. **Application Layer**: Use case implementations (Services)
   - `CreateCreditCardService`: Creates new credit cards
   - `GetCreditCardService`: Retrieves card information
   - `UpdateCreditCardStatusService`: Updates card status (ACTIVA/BLOQUEADA)
   - `UpdateBalanceService`: Updates card balance (for CONSUMO/PAGO operations)

3. **Domain Layer**: Core business logic
   - `CreditCard`: Domain entity with business rules
   - `CreditCardRepository`: Port interface for persistence
   - Enums: `CreditCardStatus`, `OperationType`

### operations-service Components

```mermaid
flowchart TB
    subgraph Users
        cardHolder[👤 Card Holder<br/>Customer who makes transactions]
        admin[👤 Administrator<br/>Bank staff]
    end

    subgraph "operations-service [Port 9093]"
        subgraph InfrastructureLayer["🔧 Infrastructure Layer"]
            operationController[OperationController<br/>Spring REST Controller<br/>Handles POST /api/v1/operations]
            creditCardController[CreditCardController<br/>Spring REST Controller<br/>Handles GET /api/v1/creditcards/active]
            restMapper[CreditCardRestMapper<br/>MapStruct Mapper<br/>Maps DTOs to Domain models]
            creditCardClient[CreditCardClient<br/>RestTemplate Client<br/>HTTP client to communicate with creditcard-service:9000]
            persistenceAdapter[CreditCardQueryRepositoryImpl<br/>JPA Adapter<br/>Implements CreditCardQueryRepository]
            jpaRepo[CreditCardJpaRepository<br/>Spring Data JPA<br/>JPA repository for local queries]
            persistenceMapper[CreditCardPersistenceMapper<br/>MapStruct Mapper<br/>Maps Entity to Domain]
            globalException[GlobalExceptionHandler<br/>Spring @ControllerAdvice<br/>Handles exceptions]
            beanConfig[BeanConfiguration<br/>Spring @Configuration<br/>Configures beans, RestTemplate, service wiring]
        end
        style InfrastructureLayer fill:#e3f2fd,stroke:#2196f3

        subgraph ApplicationLayer["⚙️ Application Layer"]
            processOperationService[ProcessOperationService<br/>Spring @Service<br/>Implements ProcessOperationUseCase - coordinates balance updates]
            getActiveCardsService[GetActiveCreditCardsService<br/>Spring @Service<br/>Implements GetActiveCreditCardsUseCase]
        end
        style ApplicationLayer fill:#fff3e0,stroke:#ff9800

        subgraph DomainLayer["💎 Domain Layer"]
            creditCard[CreditCard<br/>Domain Entity<br/>Local representation of credit card data]
            cardStatus[CreditCardStatus<br/>Enum<br/>ACTIVA, BLOQUEADA]
            operationType[OperationType<br/>Enum<br/>CONSUMO, PAGO]
            queryRepo[CreditCardQueryRepository<br/>Repository Interface<br/>Port for querying active cards]
        end
        style DomainLayer fill:#f3e5f5,stroke:#9c27b0
    end

    creditcardService[📦 creditcard-service<br/>Spring Boot 3.2, Port 9000<br/>External service for balance updates]
    style creditcardService fill:#999,stroke:#666,stroke-dasharray: 5 5

    postgresDB[("🗄️ PostgreSQL Database<br/>PostgreSQL 15.x<br/>Stores credit card data")]

    cardHolder -->|Makes transactions<br/>HTTPS/REST<br/>POST /api/v1/operations| operationController
    admin -->|Queries active cards<br/>HTTPS/REST<br/>GET /api/v1/creditcards/active| creditCardController

    operationController -->|Uses<br/>ProcessOperationUseCase| processOperationService
    creditCardController -->|Uses<br/>GetActiveCreditCardsUseCase| getActiveCardsService

    processOperationService -->|Calls HTTP API<br/>RestTemplate| creditCardClient
    creditCardClient -->|"Updates balance<br/>HTTP PATCH /api/v1/creditcards/{id}/balance"| creditcardService

    getActiveCardsService -->|Queries<br/>CreditCardQueryRepository| queryRepo
    queryRepo -->|Implemented by<br/>Adapter Pattern| persistenceAdapter
    persistenceAdapter -->|Uses<br/>Spring Data JPA| jpaRepo
    persistenceAdapter -->|Maps<br/>MapStruct| persistenceMapper
    jpaRepo -->|Reads<br/>JDBC| postgresDB
```

#### Key Components

1. **HTTP Client**: `CreditCardClient` uses RestTemplate to communicate with creditcard-service for balance updates
2. **Controllers**: Separate controllers for operations and credit card queries
3. **Services**: `ProcessOperationService` coordinates transaction processing including external balance updates

---

## REST API Endpoints

### creditcard-service (Port 9000)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/creditcards` | List all cards |
| `GET` | `/api/v1/creditcards/{id}` | Get card by ID |
| `POST` | `/api/v1/creditcards` | Create new card |
| `PATCH` | `/api/v1/creditcards/{id}/status` | Update card status |
| `PATCH` | `/api/v1/creditcards/{id}/balance` | Update balance (CONSUMO/PAGO) |

### operations-service (Port 9093)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/operations` | Process transaction (consumption/payment) |
| `GET` | `/api/v1/creditcards/active` | Get all active credit cards |

---

## Data Flow: Transaction Processing

```
Card Holder
    │
    │ POST /api/v1/operations
    ▼
┌─────────────────────┐
│ operations-service  │────┐
│ OperationController │    │
└─────────────────────┘    │
    │                      │
    │                      │
    ▼                      │
┌─────────────────────┐    │
│ ProcessOperation    │    │ 2. Calls creditCardClient
│ Service             │    │
└─────────────────────┘    │
    │                      │
    │                      │
    ▼                      │
┌─────────────────────┐    │
│ CreditCardClient    │────┘
└─────────────────────┘
    │
    │ PATCH /api/v1/creditcards/{id}/balance
    ▼
┌─────────────────────┐
│ creditcard-service  │
│ UpdateBalanceService│
└─────────────────────┘
    │
    ▼
┌─────────────────────┐
│ PostgreSQL DB       │
└─────────────────────┘
```

---

## Architecture Patterns

### Hexagonal Architecture (Clean Architecture)

The system follows Hexagonal Architecture principles:

- **Domain Layer**: Contains business logic, independent of frameworks
- **Application Layer**: Orchestrates use cases, defines ports (interfaces)
- **Infrastructure Layer**: Implements ports with specific technologies (Spring, JPA)

### Benefits

- **Testability**: Domain logic can be tested without Spring context
- **Maintainability**: Technology changes don't affect business logic
- **Flexibility**: Easy to swap implementations (e.g., database, HTTP client)

---

## File Structure

```
docs/architecture/
├── ARCHITECTURE.md              # This file - complete architecture documentation
├── C1-context.mmd               # C4 Context diagram
├── C2-containers.mmd            # C4 Container diagram
├── C3-creditcard-service.mmd    # C4 Component diagram for creditcard-service
└── C3-operations-service.mmd    # C4 Component diagram for operations-service
```

---

## Related Documentation

- [API Documentation](../../API-DOCUMENTATION.md) - Complete API reference
- [README](../../README.md) - Project overview and quick start
- [docker-compose.yml](../../docker-compose.yml) - Local development setup
