# RADP Logging Spring Boot Starter

[![Maven Central Version](https://img.shields.io/maven-central/v/space.x9x.radp/radp-logging-spring-boot-starter?style=for-the-badge)](https://central.sonatype.com/artifact/space.x9x.radp/radp-logging-spring-boot-starter)

## Introduction

RADP Logging Spring Boot Starter provides pre-configured Logback configurations for both Spring Boot applications and
classic Java applications. It offers a comprehensive logging solution with sensible defaults and flexible configuration
options. The module has been refactored to improve logging clarity and extensibility for various use cases.

## Features

- **Ready-to-use Logback configurations**:
  - Spring Boot-specific configurations with color support
  - Classic Logback configurations for non-Spring Boot applications
  - Testing-specific configurations optimized for unit and integration tests

- **Comprehensive logging capabilities**:
  - Console output with customizable patterns and color support
  - File logging with automatic rotation and compression
  - Log-level-based file separation (debug, info, warn, error)
  - Asynchronous logging for improved performance

- **Flexible configuration options**:
  - Environment-specific logging (dev, test, prod)
  - Configurable log file locations, names, and rotation policies
  - Support for Spring property placeholders
  - Enhanced template system for different logging scenarios

- **Performance optimized**:
  - Asynchronously appends to prevent blocking the application
  - Configurable queue sizes and discard thresholds
  - Efficient log file rotation and archiving
  - Improved status listener configuration

## Getting Started

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
logging.config: classpath:radp/logback/templates/logback-spring.xml
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
    <include resource="radp/logback/classic/base.xml"/>
</configuration>
```

2. To override the default values of the template file `radp/logback/classic/base.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <statusListener class="ch.qos.logback.core.status.NopStatusListener"/>
    <!-- Override radp-logging-spring-boot-starter template default values -->
    <property name="userLogPath" value="/path/to/log"/>
    <property name="userLogFileName" value="your logfile name"/>

    <include resource="radp/logback/classic/base.xml"/>
</configuration>
```

3. To adjust log levels:

```XML
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <statusListener class="ch.qos.logback.core.status.NopStatusListener"/>
    <include resource="radp/logback/classic/base.xml"/>

    <!-- Case 1: Adjust the root log level -->
    <property name="logging.level.root" value="trace"/>
    <!-- Case 2: Specify log level for a specific package -->
    <logger name="space.x9x.radp" level="trace"/>
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

## Enhanced Template System

The enhanced template system provides a more flexible way to configure logging for different scenarios:

### Available Templates

#### Spring Boot Templates

- `radp/logback/template/logback-spring.xml` - Main template for Spring Boot applications
- `radp/logback/template/logback-test.xml` - Optimized template for Spring Boot test environments

#### Classic Java Templates

- `radp/logback/template/logback-classic.xml` - Main template for classic Java applications

### Status Listener Configuration

The improved status listener configuration helps reduce noise in logs and provides better error reporting:

```xml

<statusListener class="ch.qos.logback.core.status.NopStatusListener"/>
```

This configuration prevents Logback from outputting its internal status messages, which can be useful in production
environments.

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

[Apache 2.0 License—](../../../LICENSE)Copyright © 2024 xooooooooox
and [contributors](https://github.com/xooooooooox/radp/graphs/contributors)
