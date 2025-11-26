# About RADP

> RADP 快速开发平台：致力于提供企业开发的一站式解决方案。

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

## 功能特性

- **依赖管理和插件封装**：统一管理依赖版本，解决依赖冲突问题，并提供常用 Maven 插件的封装，让开发者减少在构建工具所消耗的时间。
- **常用组件集成与封装**：在 Spring 官方的基础上扩展，提供 `XxlJob`，`CAT`，`Netty`，`Arthas` 等组件的集成。
- **组件适配及扩展点**：针对现有主流技术点进行高级抽象，提供 `消息队列`，`缓存`，`短信平台`，`邮件`，`Excel` 等组件的集成。
- **通用场景解决方案**：提供 `多级缓存`，`分布式锁`，`分布式唯一 ID`，`幂等性处理`，`业务流程编排`，`最终一致性`，`全链路标记`，
  `脱敏` 等解决方案工具。
- **脚手架封装**：提供 DDD 应用架构，MVC 应用架构等脚手架，统一开发者的编码风格, 封装流水线编排, 简化工程 DevOPS 化过程等

## 组件构成

- **radp-dependencies**: 依赖管理组件，管理全局依赖的版本。
- **radp-parent**: 构建管理组件，封装常用插件，提供开箱即用的配置。
- **radp-commons**: 基础工具组件，基于 `Apache Commons`、`Google Guava` 、`HuTool` 扩展。
- **radp-extensions**: 扩展点组件，参考 `Dubbo` 扩展点改造，轻量级实现组件的扩展。
- **radp-spring-framework**: 基础框架组件，支持自定义错误码、异常解析器。
- **radp-spring-data**: 数据存储组件，扩展了 `Mybatis`、`Redis`、`Flyway`、`Liquibase` 等组件。
- **radp-spring-security**: 授权认证组件，扩展了 `Spring Security OAuth2`、`Jwt`、`Shiro` 等组件。
- **radp-spring-integration**: 第三方集成组件，扩展了 `RocketMQ`、`Kafka`、`Netty`、`XxlJob` 等组件。
- **radp-spring-boot**: `Spring Boot`组件，根据实际的使用场景进行扩展。
- **radp-spring-boot-starters**: `Spring Boot`组件自动装配，对官方原生组件无感知增强，并扩充未集成的组件。
- **radp-spring-boot-test**: `Spring Boot`组件测试，对官方原生组件进行扩展。
- **radp-spring-cloud**: `Spring Cloud`组件，扩展了 `Nacos`、`Sentinel`、`Zookeeper` 等组件。
- **radp-spring-cloud-starters**: `Spring Cloud`组件自动装配，基于 `Spring Cloud Starters` 扩展。
- **radp-spring-test**: `Spring`测试组件，扩展了 `TestContainer`测试容器和嵌入式的中间件，单元测试。

## 分支与版本号

`0.11` 版本之前, 主分支 `main` 基于 `JDK8` 以及 `Spring Boot 2.7.x` 进行开发.

`0.11` 版本之后, 将同时维护两个主分支 `main` 以及 `future`, 且调整版本号命名规则.

从 `3.27` 版本开始, 调整分支名 `future` 为 `3.x`.

版本号命名规则 `<major>.<minor>.<patch>`

- `major`: 与 Spring Boot Version 相同
- `minor`: 用于表征 radp 本身的功能更新, 一般情况下两个分支 `minor` 版本相同
- `patch`: 两个分支可能不同, 用于表征 radp 本身的 bug 修复

| RADP Version | Branch | JDK Version | Spring Boot Version | Spring Cloud Version |
|:------------:|:------:|:-----------:|:-------------------:|:--------------------:|
| <=0.11, 2.12 |  main  |      8      |       2.7.12        |                      |
|     2.12     |  main  |      8      |       2.7.12        |                      |
|    >2.12     |  main  |      8      |       2.7.18        |                      |
|    <3.27     | future |     17      |        3.2.3        |       2023.0.0       |
|    >=3.15    | future |     17      |        3.4.4        |       2024.0.0       |
|    >=3.21    | future |     17      |        3.4.5        |       2024.0.0       |
|    >=3.27    |  3.x   |     17      |        3.4.5        |       2024.0.0       |
