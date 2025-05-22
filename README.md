# RADP - Rapid Application Development Platform

```text
 ███████████     █████████   ██████████   ███████████ 
░░███░░░░░███   ███░░░░░███ ░░███░░░░███ ░░███░░░░░███
 ░███    ░███  ░███    ░███  ░███   ░░███ ░███    ░███
 ░██████████   ░███████████  ░███    ░███ ░██████████ 
 ░███░░░░░███  ░███░░░░░███  ░███    ░███ ░███░░░░░░  
 ░███    ░███  ░███    ░███  ░███    ███  ░███        
 █████   █████ █████   █████ ██████████   █████       
░░░░░   ░░░░░ ░░░░░   ░░░░░ ░░░░░░░░░░   ░░░░░        
```

[![GitHub License](https://img.shields.io/github/license/xooooooooox/radp?style=for-the-badge)](LICENSE)
[![GitHub Release](https://img.shields.io/github/v/release/xooooooooox/radp?style=for-the-badge)](https://github.com/xooooooooox/radp/releases)
[![Maven Central Version](https://img.shields.io/maven-central/v/space.x9x.radp/radp?style=for-the-badge)](https://central.sonatype.com/namespace/space.x9x.radp)

[![Static Badge](https://img.shields.io/badge/README-EN-blue)](./README.md) [![Static Badge](https://img.shields.io/badge/README-中-red)](./README_CN.md) [![Static Badge](https://img.shields.io/badge/Document-Pages-green)](https://xooooooooox.github.io/radp)

## Introduction

RADP is a comprehensive one-stop solution designed to streamline enterprise-level Java development. By standardizing
dependencies, integrating common components, and providing out-of-the-box tooling, RADP significantly reduces
development complexity and maintenance overhead. Built on the Spring ecosystem, it offers a unified platform for
building robust, scalable, and maintainable applications.

## Key Features

### Unified Dependency Management & Plugin Wrappers

RADP manages library versions centrally to prevent dependency conflicts and encapsulates frequently used Maven plugins.
This ensures consistent builds and frees developers from the tedium of managing complex build configurations.

- Centralized version management for Spring Boot, Spring Cloud, and other dependencies
- Pre-configured Maven plugins for common tasks
- Optimized build profiles for different environments and scenarios

### Common Components Integration

Built upon the Spring ecosystem, RADP integrates and extends several essential parts:

- **Spring Framework Extensions**: Enhanced Spring Framework components with additional utilities
- **Spring Boot Starters**: Custom starters for common technologies and patterns
- **Spring Cloud Integration**: Simplified microservices development with Spring Cloud
- **Database Access**: Integration with MyBatis, Liquibase, HikariCP, and Druid
- **API Documentation**: SpringDoc OpenAPI integration for API documentation
- **Monitoring & Diagnostics**: Integration with tools like CAT and Arthas
- **Distributed Systems**: Support for XxlJob (distributed scheduling), Dubbo, and more
- **Security**: Enhanced Spring Security integration

### Extensible Adaptation Layers

RADP offers abstract layers and integration points for mainstream enterprise technologies:

- **Message Queues**: Kafka, RabbitMQ, and other messaging systems
- **Caching**: Multi-level caching strategies and Redis integration
- **Communication**: SMS platforms, email integration, and more
- **Data Processing**: Excel handling for importing, exporting, and processing data
- **Common Scenarios**: Distributed locks, unique ID generation, idempotency handling, etc.

### Design Pattern Framework

RADP provides abstractions and implementations of common design patterns to simplify their application:

- **Behavioral Patterns**: Strategy, Observer, Command, etc.
- **Creational Patterns**: Factory, Builder, Singleton, etc.
- **Structural Patterns**: Adapter, Decorator, Proxy, etc.
- **Compound Patterns**: Decision Tree and other complex patterns

### Scaffolding & Architecture Templates

RADP provides scaffolding tools and recommended architectural patterns to upstart development:

- **Project Archetypes**: Multiple archetypes for different project sizes and complexities
  - Standard (DDD-based multi-module)
  - Lite (Simplified DDD)
  - Simple (Single module)
  - Tiny (Minimal setup)
  - Starter (For creating custom starters)
- **Architecture Patterns**: Support for DDD (Domain-Driven Design) and MVC patterns
- **DevOps Integration**: Docker configuration, CI/CD templates (GitHub Actions, GitLab CI)
- **Testing Support**: Comprehensive testing utilities for different testing scenarios

## Getting Started

### Prerequisites

- JDK 17 or later
- Maven 3.6.0 or later

### Installation

Add RADP as a parent to your Maven project:

```xml

<parent>
  <groupId>space.x9x.radp</groupId>
  <artifactId>radp-parent</artifactId>
  <version>3.19</version>
</parent>
```

Or add specific components as dependencies:

```xml

<dependency>
  <groupId>space.x9x.radp</groupId>
  <artifactId>radp-spring-boot-starters</artifactId>
  <version>3.19</version>
  <type>pom</type>
</dependency>
```

### Creating a New Project with Archetypes

RADP provides several Maven archetypes to quickly bootstrap new projects:

```bash
# Create a standard DDD project
mvn archetype:generate \
  -DarchetypeGroupId=space.x9x.radp \
  -DarchetypeArtifactId=scaffold-std \
  -DarchetypeVersion=3.19 \
  -DgroupId=com.example \
  -DartifactId=my-project \
  -Dversion=1.0-SNAPSHOT
```

Available archetypes:

- `scaffold-std`: Standard DDD-based multi-module project
- `scaffold-lite`: Lightweight DDD project
- `scaffold-simple`: Simple single-module project
- `scaffold-tiny`: Minimal project setup
- `scaffold-starter`: Template for creating custom starters

## Project Structure

RADP is organized into several main modules:

- **radp-components**: Core components and extensions
  - radp-dependencies: Dependency management
  - radp-parent: Parent POM with build configurations
  - radp-commons: Common utilities
  - radp-spring-framework: Spring Framework extensions
  - radp-spring-boot: Spring Boot extensions
  - radp-spring-cloud: Spring Cloud extensions
  - radp-spring-boot-starters: Custom Spring Boot starters
  - radp-design-pattern-framework: Design pattern abstractions
- **radp-archetypes**: Project templates and scaffolding
- **radp-agents**: Agent-based extensions
- **radp-plugins**: Maven plugins and extensions
- **radp-tests**: Testing utilities and frameworks

## Documentation

Comprehensive documentation is available at [https://xooooooooox.github.io/radp](https://xooooooooox.github.io/radp).

## Versioning

RADP follows [Semantic Versioning](https://semver.org/). For the versions available, see
the [tags on this repository](https://github.com/xooooooooox/radp/tags).

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

> Thanks to the following people who contributed to this project. 🎉🎉🙏🙏

<a href="https://github.com/xooooooooox/radp/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=xooooooooox/radp" />
</a>

## Star History

![Star History Chart](https://api.star-history.com/svg?repos=xooooooooox/radp&type=Date)

## License

[Apache 2.0 License—](./LICENSE)Copyright © 2024 xooooooooox
and [contributors](https://github.com/xooooooooox/radp/graphs/contributors)
