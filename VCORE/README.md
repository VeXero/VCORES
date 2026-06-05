# VCORE Backend Documentation

This document explains the current backend application end-to-end: architecture, file responsibilities, quality characteristics, testing strategy, CI/CD, containerization, and improvement roadmap.

## 1) Project Scope and Business Context

`VCORE` is een Spring Boot backend voor mn art commission requests te managen.

Core user flow:
1. Een klant indient een verzoek om commission aan `POST /api/commissions`.
2. The backend valideert de  payload.
3. The backend stores the request in PostgreSQL.
4. The backend sends:
   - an owner notification email (Resend API)
   - a user confirmation email (Gmail SMTP)
5. Data can be retrieved via `GET /api/commissions`.

## 2) Architecture Overview

The application uses a layered architecture:

- **Presentation layer (`rest`)**: HTTP endpoints and request/response mapping.
- **Service layer (`service`)**: business logic, orchestration, email integration.
- **Persistence layer (`repos` + `domain`)**: JPA entities and repository abstraction.
- **Model layer (`model`)**: DTOs and enums used at API boundaries.
- **Configuration layer (`config` + `application.yml`)**: CORS, Spring settings, DB and mail configuration.

### 2.1 Layer interaction (request lifecycle)

1. `CommissionController.createCommission(...)` receives JSON request.
2. `@Valid` triggers bean validation from `CommissionRequestDTO`.
3. Controller delegates to `CommissionService.createCommission(...)`.
4. Service maps DTO to `CommissionRequest` entity and saves through `CommissionRepository`.
5. Service calls:
   - `sendEmailViaApi(...)` (Resend HTTP API)
   - `sendEmailViaGmail(...)` (Spring Mail / SMTP)
6. Service returns confirmation message to controller.
7. Controller returns HTTP `201 Created`.

## 3) File-by-File Reference

### 3.1 Root and Infrastructure

- `README.md` (root): repository-level documentation entry point.
- `.github/workflows/maven.yml`: CI workflow for Maven build and dependency graph submission.
- `.github/workflows/pom.xml`: duplicate Maven project file in workflow folder; currently not used by runtime build and should generally be removed or justified.

### 3.2 Build and Runtime

- `pom.xml`: dependencies and Maven plugin setup.
  - includes web, validation, JPA, PostgreSQL, mail, test starters.
- `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties`: Maven wrapper scripts and distribution config.
- `Dockerfile`: multi-stage build for the Spring Boot jar and runtime image.
- `docker-compose.yml`: local orchestration of PostgreSQL + application containers.

### 3.3 Application Bootstrapping

- `src/main/java/io/bootify/vcore/VcoreApplication.java`: Spring Boot entry point.

### 3.4 Configuration

- `src/main/resources/application.yml`: datasource, JPA behavior, Resend key, Gmail SMTP settings, owner email.
- `src/main/java/io/bootify/vcore/config/CorsConfig.java`: allows CORS from `http://localhost:3000` for frontend integration.
- `src/main/java/io/bootify/vcore/config/DomainConfig.java`: placeholder configuration class (currently empty).

### 3.5 REST Layer

- `src/main/java/io/bootify/vcore/rest/HomeResource.java`
  - `GET /api/hello` quick test endpoint.
- `src/main/java/io/bootify/vcore/rest/CommissionController.java`
  - `POST /api/commissions` create commission.
  - `GET /api/commissions` read all commissions.

### 3.6 Model and Domain

- `src/main/java/io/bootify/vcore/model/CommissionRequestDTO.java`
  - API payload model with validation annotations.
- `src/main/java/io/bootify/vcore/model/CommissionType.java`
  - enum + `@JsonCreator` supporting flexible string values.
- `src/main/java/io/bootify/vcore/model/ArtstyleType.java`
  - enum + `@JsonCreator` for style variants.
- `src/main/java/io/bootify/vcore/domain/CommissionRequest.java`
  - JPA entity mapped to table `commission_requests`.

### 3.7 Persistence

- `src/main/java/io/bootify/vcore/repos/CommissionRepository.java`
  - `JpaRepository<CommissionRequest, Long>` for CRUD operations.

### 3.8 Business and Integrations

- `src/main/java/io/bootify/vcore/service/CommissionService.java`
  - persists requests and coordinates two email channels.
  - Resend integration for owner email (`RestTemplate` + Bearer token).
  - Gmail SMTP integration for requester confirmation (`JavaMailSender`).

### 3.9 Tests

- `src/test/java/io/bootify/vcore/service/CommissionServiceTest.java`
  - unit test coverage for service behavior.
  - note: currently needs alignment with latest `CommissionService` constructor signature.

## 4) Software Quality Characteristics and Architectural Influence

### 4.1 Maintainability

- Layered architecture keeps concerns separated.
- DTO/entity split isolates API model from persistence model.
- Spring dependency injection improves testability and modularity.

### 4.2 Reliability

- Validation annotations prevent malformed input entering business logic.
- Email failures are caught so commission persistence still succeeds.

### 4.3 Usability and UX Integration

- Backend exposes clear JSON APIs consumable by a separate frontend framework.
- CORS configuration enables local frontend-backend collaboration during UX iteration.

### 4.4 Scalability

- PostgreSQL with connection pooling (`hikari`) supports moderate request load.
- Service logic is stateless and horizontally scalable.

### 4.5 Interoperability

- Uses direct client-server communication over REST/JSON.
- Integrates with external email providers via HTTP API (Resend) and SMTP (Gmail).

## 5) Testing Strategy (Current + Target)

### 5.1 Current tests

- **Unit tests**: `CommissionServiceTest` validates save + mapping behavior.

### 5.2 Recommended test suite expansion

- **Integration tests** (`@SpringBootTest`, Testcontainers PostgreSQL): verify JPA mappings and repository interactions.
- **API tests** (`MockMvc`): verify HTTP status codes, validation errors, and response payloads.
- **End-to-end tests**: run backend + DB in Docker and test full create/retrieve flow.
- **Acceptance tests**: business-scenario tests for commission submission rules.
- **Contract tests**: enforce backend API compatibility for frontend consumers.

### 5.3 Quality characteristic validation through tests

- **Performance validation**: run load tests (k6/Gatling/JMeter) on `POST /api/commissions` and `GET /api/commissions`.
- **Security validation**: test invalid input, auth model (when added), and secret/config handling.

## 6) Security Analysis and Defenses

### 6.1 Current security posture

Implemented safeguards:
- Input validation with Jakarta annotations (`@NotBlank`, `@Email`, `@NotNull`).
- ORM/JPA usage reduces SQL injection risk versus manual SQL string concatenation.

Gaps to address (high priority):
- API keys and app passwords are currently stored in plaintext in `application.yml`.
- No authentication/authorization on endpoints.
- No rate limiting, abuse protection, or CSRF strategy (if browser-authenticated flows are added).
- CORS currently allows all methods from localhost only; production policy should be explicit and minimal.

### 6.2 Recommended defenses

1. Move all secrets to environment variables or secret manager.
2. Rotate exposed Resend/Gmail credentials immediately.
3. Add Spring Security with endpoint authorization.
4. Add request rate limiting (Bucket4j / gateway policies).
5. Add secure logging rules (avoid leaking personal data or secrets).
6. Add dependency vulnerability scanning in CI.

## 7) Performance Analysis and Recommendations

### 7.1 Current performance characteristics

- Simple CRUD path, low algorithmic complexity.
- Email calls are synchronous inside request processing.
- `GET /api/commissions` returns all rows without pagination.

### 7.2 Risks

- Request latency depends on external email providers.
- Large commission table can slow reads and increase memory usage.

### 7.3 Improvements

1. Make email sending asynchronous (event queue or async executor).
2. Add pagination and sorting to `GET /api/commissions`.
3. Add database indexes if query patterns grow.
4. Add metrics and tracing (Micrometer + Prometheus/Grafana).

## 8) Full-Stack Context (Frontend/Backend Separation)

This repository contains the backend project.

How the backend supports a separate frontend project:
- API-first design with JSON DTOs.
- CORS support for local frontend origin.
- Business rules centralized in service layer so UI clients stay thin.

Direct and event-based communication:
- **Direct communication** is currently implemented (HTTP REST).
- **Event mechanism** can be introduced by publishing a domain event after commission creation (e.g., for email and analytics workers).

## 9) SDLC, Process, and Quality Assurance

### 9.1 Suggested lifecycle

1. Requirements and business analysis.
2. Architecture and API design.
3. Iterative implementation with feature branches.
4. Automated tests + static checks.
5. Code review and refactoring.
6. CI validation on PRs.
7. Containerized deployment.
8. Monitoring and incident feedback loop.

### 9.2 Industry-standard quality techniques

- Code reviews with checklist (security, correctness, maintainability).
- Refactoring passes to keep service logic cohesive.
- Static analysis (recommended: SpotBugs, PMD, Checkstyle, SonarQube).
- Dependency vulnerability checks (OWASP Dependency-Check, GitHub Dependabot).

## 10) CI/CD Pipeline

Current pipeline file: `.github/workflows/maven.yml`.

Current implemented steps:
- checkout code
- setup JDK 21
- Maven package build in `VCORE`
- dependency graph submission

Recommended CI extension:
1. Run unit + integration tests explicitly.
2. Run static analysis and security scans.
3. Build Docker image.
4. Publish artifacts and image tags.
5. Add deployment step based on target environment.
6. Add monitoring/reporting hooks post-deploy.

## 11) Containerization

### 11.1 Why containerization matters

- Reproducible environment.
- Dependency isolation.
- Easier deployment to cloud/VM/container platforms.

### 11.2 Current setup

- `Dockerfile`: builds jar in builder stage and runs on JRE base image.
- `docker-compose.yml`: starts PostgreSQL and app with dependency order and healthcheck.

### 11.3 Recommended hardening

- Use non-root user in runtime image.
- Externalize secrets via environment variables.
- Pin base image digests for supply-chain stability.

## 12) Architecture Communication and Diagrams

Use and version these diagrams for project transferability:
- **Context diagram**: frontend, backend, PostgreSQL, Resend, Gmail SMTP.
- **Container diagram**: API service container + DB container.
- **Sequence diagram**: commission submission flow through layers and external providers.
- **Deployment diagram**: CI pipeline to runtime environment.

## 13) GenAI in Engineering Workflow (with critical review)

Potential productive uses:
- generate test cases for edge conditions
- draft refactoring proposals
- suggest secure configuration patterns
- create initial API docs and sequence diagrams

Critical evaluation practices:
1. Verify generated code against project standards.
2. Validate with tests and static analysis.
3. Check security and performance impact before merge.
4. Require human review for all GenAI-produced changes.

## 14) Immediate Action Checklist

1. Rotate exposed credentials in `application.yml`.
2. Move secrets to environment variables.
3. Fix `CommissionServiceTest` constructor mismatch.
4. Add integration and API tests.
5. Add static analysis/security scanning to CI.
6. Add pagination and async email handling.

## 15) Run and Build

From repository root:

```powershell
cd VCORE
.\mvnw.cmd clean package
```

Run application:

```powershell
cd VCORE
.\mvnw.cmd spring-boot:run
```

Run with Docker Compose:

```powershell
cd VCORE
docker compose up --build
```
