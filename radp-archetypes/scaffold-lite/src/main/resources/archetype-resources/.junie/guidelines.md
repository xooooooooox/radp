# Guidelines

This document provides a concise, practical guide for contributing to RADP. It links to deeper explanations and style
rules used by Junie in this repository.

## Project Structure

This repository is a multi-module Maven project organized by Domain-Driven Design (DDD) with a Ports & Adapters (
Hexagonal) architecture. At a glance:

- scaffold-std-demo-types — Shared types used across modules
  - Enums, common utilities, unified exceptions/error codes
  - Keep framework dependencies minimal
- scaffold-std-demo-domain — Pure domain layer
  - Aggregates, Entities, Value Objects, Domain Services, Domain Events
  - Repository interfaces (ports) defined here; no framework/persistence details
- scaffold-std-demo-infrastructure — Technical adapters (outbound)
  - Repository implementations, DAOs/POs, database/cache/config/message integrations
  - Depends on “domain” to implement its ports; contains anti‑corruption for external systems
- scaffold-std-demo-trigger — Inbound adapters (“triggers”)
  - HTTP/RPC controllers, jobs/schedulers, message listeners, input validation and DTOs
  - Orchestrates calls into the domain (no business rules here)
- scaffold-std-demo-app — Application bootstrap and packaging
  - Spring Boot entrypoint, wiring/configuration, deployment artifacts
  - Kubernetes/Helm manifests under src/main/jkube (JKube): deployment.yml, service.yml, helm/values.yaml

Supporting directories:

- docs — Architecture diagrams and technical notes
  - Open the reference diagram: `../docs/scaffold.drawio` (see pages “4.1-DDD架构指导图一”, “4.2-DDD架构指导图二”, and
    the appendix on MVC)
- Writerside — Documentation site sources (JetBrains Writerside)
- logs — Runtime logs (current and archived)

Interaction rules (summary):

- Triggers call into domain services (and/or application orchestration where present)
- Domain depends only on types; it declares repository interfaces
- Infrastructure depends on domain to implement repository interfaces and external integrations

For a visual walkthrough of layers, ports, and adapters, see `../docs/scaffold.drawio`.

## Code Style and Formatting

- [Code style (cheatsheet)](./codestyle.md)
- [Extended guidelines with explanations](./codestyle-with-explanations.md)
- [Checkstyle config](../.coding/checkstyle/checkstyle.xml)
- [Checkstyle suppressions](../.coding/checkstyle/checkstyle-suppressions.xml)
- [Editor configuration](../.editorconfig)

Important notes:

- Prefer constructor injection; avoid field/setter injection in production code.
- Keep Spring components package-private where possible.
- Use DTOs for web APIs; don’t expose JPA entities from controllers.
- Centralize exception handling with @RestControllerAdvice; prefer ProblemDetails format.
- Logging: never use System.out; guard expensive debug logs; never log secrets.
- Avoid fully qualified names (FQN) in Java/Groovy — add imports and use simple type names.

See the two documents above for the full list of rules and rationale.

## How to Build

Prerequisites:

- JDK 17 (as configured in the root pom.xml)
- Maven Wrapper is included; use `./mvnw` (or `mvnw.cmd` on Windows)
- Docker is recommended for integration tests that use Testcontainers

Common commands (run from repository root):

- Verify everything:
  `./mvnw clean verify`
- Skip tests (when appropriate):
  `./mvnw clean verify -DskipTests`
- Run Checkstyle only:
  `./mvnw -DskipTests checkstyle:check`
- (If configured) Apply Spring Java Format:
  `./mvnw spring-javaformat:apply`
