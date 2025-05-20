# RADP Logging Spring Boot Starter

## 概述

RADP Logging Spring Boot Starter 提供了一套完整的日志配置解决方案，可以轻松集成到 Spring Boot 应用中，实现统一的日志格式和管理。

## 特性

- 统一的日志格式配置
- 控制台彩色输出
- 文件日志输出（包括滚动策略）
- 按日志级别分类存储
- 异步日志处理提升性能
- 支持不同的应用场景（标准应用、测试环境、早期初始化等）

## 使用方法

### 1. 添加依赖

```xml

<dependency>
    <groupId>space.x9x.radp</groupId>
    <artifactId>radp-logging-spring-boot-starter</artifactId>
    <version>${radp.version}</version>
</dependency>
```

### 2. 选择合适的日志配置模板

根据您的应用场景，选择合适的日志配置模板：

#### 标准应用场景

在 `src/main/resources` 目录下创建 `logback-spring.xml` 文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="logback/scenarios/standard/logback-spring.xml"/>
</configuration>
```

#### 测试环境场景

在 `src/test/resources` 目录下创建 `logback-test.xml` 文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="logback/scenarios/testing/logback-test.xml"/>
</configuration>
```

#### 早期初始化场景（如 Testcontainers）

在 `src/test/resources` 目录下创建 `logback-test.xml` 文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="logback/scenarios/early-init/logback-test.xml"/>
</configuration>
```

## 配置说明

### 日志级别配置

#### 标准应用场景

在 `application.yml` 或 `application.properties` 中配置：

```yaml
logging:
  level:
    root: info
    your.package.name: debug
```

#### 测试环境和早期初始化场景

在 XML 配置文件中直接设置：

```xml

<root level="info">
    <!-- appenders -->
</root>

<logger name="your.package.name" level="debug"/>
```

### 日志文件路径配置

在 `application.yml` 或 `application.properties` 中配置：

```yaml
logging:
  file:
    path: /path/to/logs
    name: myapp
```

## 目录结构说明

```
src/main/resources/logback/
├── scenarios/                  # 按使用场景组织的配置
│   ├── standard/               # 标准应用场景
│   │   ├── logback-spring.xml  # 主配置文件
│   │   └── README.md           # 使用说明
│   ├── testing/                # 测试环境场景
│   │   ├── logback-test.xml    # 主配置文件
│   │   └── README.md           # 使用说明
│   └── early-init/             # 早期初始化场景
│       ├── logback-test.xml    # 主配置文件
│       └── README.md           # 使用说明
└── components/                 # 共享组件
    ├── standard/               # 标准组件
    │   ├── common.xml          # 通用属性
    │   ├── console.xml         # 控制台输出
    │   └── file.xml            # 文件输出
    └── early-init/             # 早期初始化组件
        ├── common.xml          # 通用属性
        ├── console.xml         # 控制台输出
        └── file.xml            # 文件输出
```

## 场景说明

### 标准应用场景

适用于常规的 Spring Boot 应用，提供完整的日志功能，包括彩色控制台输出、文件日志、按级别分类等。

### 测试环境场景

适用于单元测试和集成测试，配置更加简洁，主要关注控制台输出的可读性。

### 早期初始化场景

专为在 Spring Boot 完全初始化之前就需要记录日志的场景设计，例如使用 Testcontainers 进行测试时。这种场景下，不依赖 Spring
Boot 的日志扩展，使用标准的 Logback 模式，确保日志能够正确格式化。

## 常见问题

### 问题：日志中出现 %PARSER_ERROR[clr] 或 %PARSER_ERROR[wEx]

**解决方案**：这是因为在 Spring Boot 完全初始化之前就有日志输出，此时 Spring Boot 的日志扩展尚未加载。请使用早期初始化场景的配置。

### 问题：如何同时输出到控制台和文件？

**解决方案**：所有场景的配置默认都会同时输出到控制台和文件。如果需要修改，可以编辑相应的配置文件。

### 问题：如何自定义日志格式？

**解决方案**：可以通过继承现有配置并修改 pattern 属性来自定义日志格式。

## 测试指南

本项目提供了完整的测试套件，用于验证各种日志配置场景是否正常工作。以下是如何测试各种模板文件的说明：

### 运行所有测试

```bash
cd radp-components/radp-spring-boot-starters/radp-logging-spring-boot-starter
mvn test -DskipTests=false
```

### 测试特定场景

#### 标准应用场景测试

```bash
mvn test -DskipTests=false -Dtest=StandardLoggingConfigSpec
```

这个测试验证标准应用场景的日志配置是否正确，包括：

1. 控制台日志输出格式（包括彩色输出）
2. 文件日志输出
3. 不同环境（local、dev、test、prod）的配置

#### 测试环境场景测试

```bash
mvn test -DskipTests=false -Dtest=TestingLoggingConfigSpec
```

这个测试验证测试环境场景的日志配置是否正确，包括：

1. 控制台日志输出格式
2. 文件日志输出
3. 常用测试框架的日志级别

#### 早期初始化场景测试

```bash
mvn test -DskipTests=false -Dtest=EarlyInitLoggingConfigSpec
```

这个测试验证早期初始化场景的日志配置是否正确，包括：

1. 在 Spring Boot 初始化前是否能正确格式化日志
2. 控制台日志输出格式（不使用 Spring Boot 的彩色日志）
3. 文件日志输出

### 验证日志效果

要验证日志效果是否符合预期，可以查看测试输出中的日志格式。例如，早期初始化场景的日志格式应该是标准的 Logback 格式，不包含
Spring Boot 特有的转换模式（如 `%clr` 和 `%wEx`）。

您也可以通过修改测试类中的断言来验证特定的日志格式要求。例如，在 `EarlyInitLoggingConfigSpec` 类中，有一个测试方法
`should use standard Logback pattern for console output`，它验证控制台日志格式是否使用标准的 Logback 模式。

### 测试实际应用

除了单元测试外，您还可以在实际应用中测试日志配置：

1. 在您的应用中引入 `radp-logging-spring-boot-starter` 依赖
2. 根据应用场景选择合适的日志配置模板
3. 启动应用并观察日志输出

对于 Testcontainers 等早期初始化场景，请确保使用早期初始化场景的配置，以避免出现 `%PARSER_ERROR` 错误。
