# Guidelines

This document provides a concise, practical guide for contributing to RADP. It links to deeper explanations and style
rules used by Junie in this repository.

## Project Structure

At a glance (top-level modules):

- radp-components — Core libraries and starters used across projects (Spring Boot, Spring Cloud, Data, Security, etc.)
- radp-agents — Automation and agent-related modules
- radp-plugins — Optional extensions and integrations
- radp-archetypes — Maven archetypes to scaffold new projects (scaffold-*)
- radp-tests — Test suites (smoke, integration, performance, deployment)
- Writerside — Documentation sources

Refer to the repository root for a complete tree and each module’s README where applicable.

## Code Style and Formatting

- [Code style (cheatsheet)](./codestyle.md)
- [Extended guidelines with explanations](./codestyle-with-explanations.md)
- [Checkstyle config](../.coding/checkstyle/checkstyle.xml)
- [Checkstyle suppressions](../.coding/checkstyle/checkstyle-suppressions.xml)
- [Editor configuration](../.editorconfig)
- [Markdown Style](./markdown_codestyle.md)

Important notes:

- Prefer constructor injection; avoid field/setter injection in production code.
- Keep Spring components package-private where possible.
- Use DTOs for web APIs; don’t expose JPA entities from controllers.
- Centralize exception handling with @RestControllerAdvice; prefer ProblemDetails format.
- Logging: never use System.out; guard expensive debug logs; never log secrets.
- Avoid fully qualified names (FQN) in Java/Groovy — add imports and use simple type names.
- To prevent missing metadata in newly generated Java types, must always add a class-level Javadoc block that contains
  these tags:
  - @author — your name/handle; use "Junie" for auto-generated code.
  - @since — current local date and time in the format yyyy-MM-dd HH:mm (24-hour), Example: 2025-11-09 20:05.

See the two documents above for the full list of rules and rationale.

## How to Build

Prerequisites:

- JDK 8 (as configured in the root pom.xml)
- Maven Wrapper is included; use `./mvnw` (or `mvnw.cmd` on Windows)
- Docker is recommended for integration tests that use Testcontainers

Common commands (run from repository root):

- Verify everything:
  `./mvnw clean verify`
- Skip tests (when appropriate):
  `./mvnw clean verify -DskipTests`
- Jacoco report and check
  - `./mvnw clean verify jacoco:report-aggregate -Pcoding,unit-test,env-test`
- Build a single module and its dependencies:
  `./mvnw -pl <module-path> -am clean verify`
  Example: `./mvnw -pl radp-components/radp-parent -am clean verify`
- Run Checkstyle only:
  `./mvnw -DskipTests checkstyle:check`
- (If configured) Apply Spring Java Format:
  `./mvnw spring-javaformat:apply`
