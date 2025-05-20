# 标准应用场景日志配置 (Standard Application Logging)

## 概述

这个日志配置适用于常规的 Spring Boot 应用，提供完整的日志功能，包括彩色控制台输出、文件日志、按级别分类等。

## 特点

1. 使用 Spring Boot 的日志扩展（如 %clr 和 %wEx 转换模式）
2. 提供彩色控制台输出，提高日志可读性
3. 完整的文件日志配置，包括滚动策略
4. 按日志级别分类存储（debug、info、warn、error）
5. 支持不同的环境配置（local、dev、test、prod）
6. 异步日志处理提升性能

## 使用方法

在 Spring Boot 应用中，创建或修改 `src/main/resources/logback-spring.xml` 文件，内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <statusListener class="ch.qos.logback.core.status.NopStatusListener"/>
    <include resource="logback/scenarios/standard/logback-spring.xml"/>
</configuration>
```

## 适用场景

- 常规的 Spring Boot 应用
- 微服务应用
- Web 应用
- 任何需要完整日志功能的生产环境应用

## 配置说明

### 日志级别配置

在 `application.yml` 或 `application.properties` 中配置：

```yaml
logging:
  level:
    root: info
    your.package.name: debug
```

### 日志文件路径配置

在 `application.yml` 或 `application.properties` 中配置：

```yaml
logging:
  file:
    path: /path/to/logs
    name: myapp
```

### 环境特定配置

标准应用场景配置支持不同的环境配置：

- **local**: 开发环境，通常输出更详细的日志
- **dev**: 开发服务器环境
- **test**: 测试环境
- **prod**: 生产环境

可以通过 Spring 的 profile 机制激活不同的环境配置：

```yaml
spring:
  profiles:
    active: dev
```

## 日志输出示例

控制台输出示例：

```
2023-11-01 12:34:56.789 | INFO  | main com.example.MyApplication | Application started successfully
2023-11-01 12:35:01.234 | ERROR | http-nio-8080-exec-1 com.example.controller.UserController | Failed to process request
java.lang.IllegalArgumentException: Invalid user ID
    at com.example.service.UserService.getUser(UserService.java:42)
    at com.example.controller.UserController.getUser(UserController.java:25)
    ...
```

## 与其他日志配置的区别

### 与测试环境场景的区别

- 标准应用场景配置更加完整，提供全面的日志功能
- 支持环境特定配置（local、dev、test、prod）
- 按日志级别分类存储

### 与早期初始化场景的区别

- 标准应用场景配置依赖 Spring Boot 的日志扩展，提供更丰富的功能
- 早期初始化场景配置不依赖 Spring Boot 的日志扩展，适用于 Spring Boot 初始化前的日志记录