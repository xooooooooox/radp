# RADP Logging Spring Boot Starter

[![Maven Central Version](https://img.shields.io/maven-central/v/space.x9x.radp/radp-logging-spring-boot-starter?style=for-the-badge)](https://central.sonatype.com/artifact/space.x9x.radp/radp-logging-spring-boot-starter)

## Introduction

RADP Logging Spring Boot Starter provides pre-configured Logback configurations for both Spring Boot applications and
classic Java applications. It offers a comprehensive logging solution with sensible defaults and flexible configuration
options.

## Features

- **Ready-to-use Logback configurations**:
  - Spring Boot-specific configurations with color support
  - Classic Logback configurations for non-Spring Boot applications
  - Testing-specific configurations optimized for unit and integration tests

- **Comprehensive logging capabilities**:
  - Console output with customizable patterns and color support
  - File logging with automatic rotation and compression
  - Log level-based file separation (debug, info, warn, error)
  - Asynchronous logging for improved performance

- **Flexible configuration options**:
  - Environment-specific logging (dev, test, prod)
  - Configurable log file locations, names, and rotation policies
  - Support for Spring property placeholders

- **Performance optimized**:
  - Asynchronous appends to prevent blocking the application
  - Configurable queue sizes and discard thresholds
  - Efficient log file rotation and archiving

## Getting Started

### Prerequisites

- Java 17 or later
- Spring Boot 3.x

### Installation

Add the dependency to your Maven project:

```xml

<dependency>
    <groupId>space.x9x.radp</groupId>
    <artifactId>radp-logging-spring-boot-starter</artifactId>
    <version>${radp.version}</version>
</dependency>
```

### Usage

#### For Spring Boot Applications

1. The starter automatically provides a `logback-spring.xml` configuration.

2. Configure logging properties in your `application.properties` or `application.yml`:

```yaml
# Basic configuration
spring.application.name: myapp
logging.config: classpath:logback/springboot/templates/logback-spring.xml
logging.file.path: /var/log

# Optional: Configure log levels
logging.level.root: info
logging.level.org.springframework: warn
logging.level.com.example: debug

# Optional: Customize console pattern (set to empty string to disable console logging)
# logging.pattern.console: 
```

#### For Non-Spring Boot Applications

1. Create a `logback.xml` file in your classpath that includes the provided templates:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <statusListener class="ch.qos.logback.core.status.NopStatusListener"/>
    <include resource="logback/classic/scenarios/standard/logback-config.xml"/>
</configuration>
```

## Configuration Options

### Common Properties

| Property                  | Description                              | Default Value       |
|---------------------------|------------------------------------------|---------------------|
| `logging.file.path`       | Directory where log files will be stored | `${java.io.tmpdir}` |
| `spring.application.name` | Prefix for log file names                | `application`       |
| `logging.level.root`      | Root logging level                       | `info`              |
| `logging.pattern.console` | Custom pattern for console output        | *Built-in pattern*  |

### Rolling Policy Properties

| Property                                       | Description                               | Default Value |
|------------------------------------------------|-------------------------------------------|---------------|
| `LOGBACK_ROLLINGPOLICY_CLEAN_HISTORY_ON_START` | Whether to clean history on start         | `false`       |
| `LOGBACK_ROLLINGPOLICY_MAX_FILE_SIZE`          | Maximum size of log files before rotation | `10MB`        |
| `LOGBACK_ROLLINGPOLICY_TOTAL_SIZE_CAP`         | Total size cap for all log files          | `1GB`         |
| `LOGBACK_ROLLINGPOLICY_MAX_HISTORY`            | Number of days to keep log files          | `30`          |

## Scenarios

### Standard Scenario

The standard scenario is suitable for regular applications and provides:

- Console output with color support
- File logging with rotation
- Log-level-based file separation
- Profile-specific configurations (local, dev, test, prod)

### Testing Scenario

The testing scenario is optimized for unit and integration tests:

- Simplified configuration focused on console output
- Higher default log levels for debugging
- Minimal file logging

## Advanced Usage

### Custom Log Patterns

You can customize log patterns by setting the appropriate properties:

```yaml
# Custom console pattern
logging:
  pattern:
    console: "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr([%t]){magenta} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx"
```

### Disabling Console Output

To disable console output, set the console pattern to an empty string:

```yaml
logging:
  pattern:
    console: 
```

## License

[GNU General Public License—](../../LICENSE)Copyright (C) 2024 xooooooooox
and [contributors](https://github.com/xooooooooox/radp/graphs/contributors)