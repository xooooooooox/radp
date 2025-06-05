# RADP - 快速应用开发平台

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

## 介绍

RADP 是一个全面的面向企业级 Java 开发的一站式解决方案。通过对依赖的标准化管理、通用组件的整合以及开箱即用的工具支持，RADP
显著降低了开发的复杂性和后期维护成本。基于 Spring 生态系统构建，它为开发健壮、可扩展和易维护的应用程序提供了统一的平台。

## 核心特性

### 统一依赖管理与插件封装

RADP 集中管理库版本以防止依赖冲突，并封装了常用的 Maven 插件。这确保了构建的一致性，并使开发人员免于管理复杂构建配置的繁琐工作。

- Spring Boot、Spring Cloud 和其他依赖的集中版本管理
- 为常见任务预配置的 Maven 插件
- 针对不同环境和场景优化的构建配置文件

### 通用组件集成

基于 Spring 生态系统，RADP 集成并扩展了多种核心组件：

- **Spring Framework 扩展**：增强的 Spring Framework 组件和额外的实用工具
- **Spring Boot Starters**：针对常见技术和模式的自定义启动器
- **Spring Cloud 集成**：简化的 Spring Cloud 微服务开发
- **数据库访问**：与 MyBatis、Liquibase、HikariCP 和 Druid 的集成
- **API 文档**：用于 API 文档的 SpringDoc OpenAPI 集成
- **监控与诊断**：与 CAT、Arthas 等工具的集成
- **分布式系统**：支持 XxlJob（分布式调度）、Dubbo 等
- **安全**：增强的 Spring Security 集成

### 可扩展适配层

RADP 为主流企业技术提供抽象层和集成点：

- **消息队列**：Kafka、RabbitMQ 和其他消息系统
- **缓存**：多级缓存策略和 Redis 集成
- **通信**：短信平台、邮件集成等
- **数据处理**：用于导入、导出和处理数据的 Excel 处理
- **通用场景**：分布式锁、分布式唯一 ID、幂等性处理、业务流程编排、最终一致性、全链路标记、脱敏等解决方案

### 设计模式框架

RADP 提供常见设计模式的抽象和实现，以简化其应用：

- **行为模式**：策略、观察者、命令等
- **创建模式**：工厂、建造者、单例等
- **结构模式**：适配器、装饰器、代理等
- **复合模式**：决策树和其他复杂模式

### 脚手架与架构模板

RADP 提供脚手架工具和推荐的架构模式，帮助快速启动开发：

- **项目原型**：针对不同项目规模和复杂性的多种原型
  - 标准版（基于 DDD 的多模块）
  - 轻量版（简化的 DDD）
  - 简单版（单模块）
  - 微型版（最小设置）
  - 启动器版（用于创建自定义启动器）
- **架构模式**：支持 DDD（领域驱动设计）和 MVC 模式
- **DevOps 集成**：Docker 配置、CI/CD 模板（GitHub Actions、GitLab CI）
- **测试支持**：针对不同测试场景的综合测试工具

## 快速开始

### 前提条件

- JDK 8 或 JDK 11
- Maven 3.6.0 或更高版本

### 安装

将 RADP 作为父项目添加到您的 Maven 项目中：

```xml

<parent>
	<groupId>space.x9x.radp</groupId>
	<artifactId>radp-parent</artifactId>
	<version>LATEST_VERSION</version>
</parent>
```

或者添加特定组件作为依赖：

```xml

<dependency>
	<groupId>space.x9x.radp</groupId>
	<artifactId>radp-spring-boot-starters</artifactId>
	<version>LATEST_VERSION</version>
	<type>pom</type>
</dependency>
```

### 使用原型创建新项目

RADP 提供了几种 Maven 原型，可以快速引导新项目：

```bash
# 创建标准 DDD 项目
mvn archetype:generate \
  -DarchetypeGroupId=space.x9x.radp \
  -DarchetypeArtifactId=scaffold-std \
  -DarchetypeVersion=LATEST_VERSION \
  -DgroupId=com.example \
  -DartifactId=my-project \
  -Dversion=1.0-SNAPSHOT
```

可用的原型：

- `scaffold-std`：标准的基于 DDD 的多模块项目
- `scaffold-lite`：轻量级 DDD 项目
- `scaffold-simple`：简单的单模块项目
- `scaffold-tiny`：最小项目设置
- `scaffold-starter`：用于创建自定义启动器的模板

## 项目结构

RADP 组织为几个主要模块：

- **radp-components**：核心组件和扩展
  - radp-dependencies：依赖管理
  - radp-parent：带有构建配置的父 POM
  - radp-commons：通用工具
  - radp-spring-framework：Spring Framework 扩展
  - radp-spring-boot：Spring Boot 扩展
  - radp-spring-cloud：Spring Cloud 扩展
  - radp-spring-boot-starters：自定义 Spring Boot 启动器
  - radp-design-pattern-framework：设计模式抽象
- **radp-archetypes**：项目模板和脚手架
- **radp-agents**：基于代理的扩展
- **radp-plugins**：Maven 插件和扩展
- **radp-tests**：测试工具和框架

## 文档

完整文档可在 [https://xooooooooox.github.io/radp](https://xooooooooox.github.io/radp) 获取。

## 版本控制

RADP 遵循[语义化版本控制](https://semver.org/)
。有关可用版本，请参阅[此存储库上的标签](https://github.com/xooooooooox/radp/tags)。

## 贡献

欢迎贡献！请随时提交拉取请求。

> 感谢以下贡献者对本项目的贡献。🎉🎉🙏🙏

<a href="https://github.com/xooooooooox/radp/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=xooooooooox/radp" />
</a>

## Star 历史

![Star History Chart](https://api.star-history.com/svg?repos=xooooooooox/radp&type=Date)

## 许可证

[Apache 2.0 License—](./LICENSE) - 版权所有 (C) 2024 xooooooooox
和[贡献者](https://github.com/xooooooooox/radp/graphs/contributors)
