# 测试环境日志配置 (Testing Environment Logging)

## 概述

这个日志配置专门用于单元测试和集成测试场景，提供简洁的配置和良好的可读性。

## 特点

1. 使用 Spring Boot 的日志扩展（如 %clr 和 %wEx 转换模式）
2. 提供彩色控制台输出，提高日志可读性
3. 简化的文件日志配置
4. 更高的默认日志级别（便于调试）
5. 对常用测试框架的日志级别进行了优化

## 使用方法

在需要进行单元测试或集成测试的项目中，创建或修改 `src/test/resources/logback-test.xml` 文件，内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <statusListener class="ch.qos.logback.core.status.NopStatusListener"/>
    <include resource="logback/scenarios/testing/logback-test.xml"/>
</configuration>
```

## 适用场景

- 单元测试
- 集成测试
- 任何需要在测试环境中记录日志的场景

## 配置说明

### 日志级别配置

在 XML 配置文件中直接设置：

```xml
<root level="debug">
    <!-- appenders -->
</root>

<logger name="your.package.name" level="trace"/>
```

或者在 `application-test.yml` 或 `application-test.properties` 中配置：

```yaml
logging:
  level:
    root: info
    your.package.name: debug
```

### 日志文件路径配置

在 `application-test.yml` 或 `application-test.properties` 中配置：

```yaml
logging:
  file:
    path: /path/to/logs
    name: myapp-test
```

或者在启动测试时通过系统属性指定：

```
-DlogHome=/path/to/logs -DlogNamePrefix=myapp-test
```

## 与其他日志配置的区别

### 与标准应用场景的区别

- 测试环境配置更加简洁，移除了不必要的复杂性
- 默认使用更高的日志级别，便于调试
- 对测试框架的日志级别进行了优化

### 与早期初始化场景的区别

- 测试环境配置依赖 Spring Boot 的日志扩展，提供更丰富的功能
- 早期初始化场景配置不依赖 Spring Boot 的日志扩展，适用于 Spring Boot 初始化前的日志记录