# AGENTS.md - TechXplore Backend Guide

## Project Overview
**TechXplore Backend** is a Spring Boot 3.5.15 (Java 17) microservice for managing energy data with AI capabilities. The architecture combines traditional REST API patterns with modern AI/ML infrastructure (Spring AI 1.1.8, AWS OpenSearch vector store, Kafka integration).

---

## Architecture & Data Flow

### Layered Architecture
```
HTTP Request
    ↓
ZDataProcessController (/api/zdataprocess)
    ↓
ZEnergyDataService (business logic)
    ↓
ZEnergyDataRepository (Spring Data JPA)
    ↓
EnergyData (JPA Entity)
    ↓
PostgreSQL Database
```

### Key Pattern: Naming Convention with "Z" Prefix
Many core components use a "Z" prefix (ZDataProcessController, ZEnergyDataService, ZEnergyDataRepository, ZEnergyDataUpdateRequest/Response). This appears to be a project-specific convention—when adding new features, maintain this pattern for consistency.

### Request/Response Flow
1. **API Contract**: Requests/responses use DTOs (ZEnergyDataUpdateRequest → ZEnergyDataUpdateResponse)
2. **Service Layer**: Transforms DTO to entity, performs persistence, returns response DTO
3. **Exception Handling**: Global `@RestControllerAdvice` catches exceptions and returns ErrorResponse JSON

---

## Core Components & Responsibilities

### Controller Layer (`com.techxplore.techxplorebackend.controller`)
- **ZDataProcessController**: REST endpoints for energy data
  - `PUT /api/zdataprocess/updateFetch`: Update/fetch energy data
  - `GET /api/zdataprocess/fetchAllEnergyData`: Retrieve all energy records
  - Uses constructor injection: `public ZDataProcessController(ZEnergyDataService service)`

### Service Layer (`com.techxplore.techxplorebackend.service`)
- **ZEnergyDataService**: Core business logic
  - `updateFetch(ZEnergyDataUpdateRequest)`: Creates/updates EnergyData entity
  - `fetchAllEnergyData()`: Retrieves all records
  - Direct repository access for persistence

### Data Layer
- **ZEnergyDataRepository**: Extends `JpaRepository<EnergyData, Long>` (Spring Data JPA)
- **EnergyData Entity**: `@Entity` with auto-generated ID, fields: source (String), value (Double)

### DTOs (`com.techxplore.techxplorebackend.model`)
- **ZEnergyDataUpdateRequest**: Input model with id, source, value
- **ZEnergyDataUpdateResponse**: Output model with message, id

### Error Handling (`com.techxplore.techxplorebackend.advice`)
- **GlobalExceptionHandler**: `@RestControllerAdvice` catches all exceptions
- **ErrorResponse**: Simple error DTO with error message field

### Converters (`com.techxplore.techxplorebackend.converter`)
- **JsonNodeConverter**: `@Converter` for JPA—converts Jackson JsonNode to/from database String

---

## Critical Dependencies & Integrations

### Active Dependencies
| Dependency | Purpose |
|---|---|
| **spring-boot-starter-web** | REST API support |
| **spring-boot-starter-data-jpa** | ORM and database access |
| **spring-boot-starter-data-elasticsearch** | Elasticsearch integration |
| **spring-ai-starter-vector-store-aws-opensearch** | Vector store for AI embeddings |
| **spring-kafka** | Event streaming & message queue |
| **spring-boot-starter-oauth2-client** | OAuth2 authentication |
| **postgresql** | Primary database (runtime) |

### Test Dependencies
- `spring-boot-starter-test` (JUnit 5, Spring Test)
- `spring-kafka-test` (Kafka testing)
- `spring-security-test` (OAuth2 testing)

---

## Developer Workflows

### Build & Run
```bash
# Build project (Maven)
./mvnw clean package

# Run application (uses application.yaml config)
./mvnw spring-boot:run

# Run tests
./mvnw test

# Build Docker image
./mvnw spring-boot:build-image
```

### Configuration
- **Main config**: `src/main/resources/application.yaml`
- Currently minimal; extend here for Spring profiles (dev/prod), database credentials, AI model endpoints

### Adding New Features
1. **New Entity**: Create in `entity/` package, add `@Entity` and `@Id` annotations
2. **New Repo**: Extend `JpaRepository<YourEntity, ID>` in `repository/` (Spring Data provides CRUD)
3. **New Service**: Create in `service/` package, inject repo via constructor
4. **New Controller**: Create in `controller/` package, follow ZDataProcessController pattern (constructor injection, return `ResponseEntity<>`)
5. **DTOs**: Create request/response models in `model/` package for API contracts

---

## Project Conventions & Patterns

### Constructor Injection (No @Autowired)
All dependencies are injected via constructor, not fields. Example:
```java
private final ZEnergyDataService service;
public ZDataProcessController(ZEnergyDataService service) {
    this.service = service;
}
```
**Why**: More testable, thread-safe, required dependencies are explicit.

### ResponseEntity Wrapper
Controllers always return `ResponseEntity<T>`:
```java
return ResponseEntity.ok(service.fetchAllEnergyData());
```
Allows control over HTTP status codes and headers.

### Global Exception Handling
All exceptions caught by `GlobalExceptionHandler` → ErrorResponse JSON. Don't throw exceptions from controller; let service layer throw and handler catches.

### DTO Transformation
Services receive DTOs, create/populate entities, and return response DTOs. Entities don't cross service boundaries.

---

## Key Files Reference

| File | Role |
|---|---|
| `pom.xml` | Maven dependencies, Spring Boot version (3.5.15), Spring AI version (1.1.8) |
| `src/main/resources/application.yaml` | Spring Boot config (currently minimal) |
| `src/main/java/com/techxplore/techxplorebackend/` | Application code root |
| `src/test/java/com/techxplore/techxplorebackend/` | Unit/integration tests |

---

## AI/ML Integration Points

- **Spring AI** enabled with AWS OpenSearch vector store (for semantic search on energy data)
- **Elasticsearch** configured (see pom.xml dependency)
- **Kafka** available for streaming energy data updates
- These are infrastructure dependencies; integration code not yet visible in current codebase—AI features are infrastructure-ready but not actively implemented in controllers/services

---

## Testing Strategy

- **Test Location**: `src/test/java/com/techxplore/techxplorebackend/`
- **Base Annotation**: `@SpringBootTest` (full Spring context for integration tests)
- **Test Dependencies**: JUnit 5, Mockito (via spring-boot-starter-test)
- **Example**: `TechxplorebackendApplicationTests.contextLoads()` (smoke test)

**Add tests for new endpoints** using `@SpringBootTest` and `TestRestTemplate` or `@WebMvcTest` for isolated controller testing.

---

## Quick Productivity Tips

1. **Entity Changes**: After modifying `EnergyData`, services don't need changes (JPA handles mapping)
2. **New Endpoint**: Copy ZDataProcessController structure—add method, inject service, return ResponseEntity
3. **Database Query**: Extend ZEnergyDataRepository with custom query methods (e.g., `findBySource(String)`)
4. **Error Handling**: Add specific `@ExceptionHandler` in GlobalExceptionHandler, no try-catch needed
5. **Testing New Endpoint**: Use `@SpringBootTest` + mock the service layer or integration test against H2 in-memory DB


