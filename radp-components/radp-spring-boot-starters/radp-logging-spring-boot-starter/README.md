# RADP Logging Spring Boot Starter

## 简介

RADP Logging Spring Boot Starter 是一个预配置的日志解决方案，为 Spring Boot 应用程序提供了完整的日志功能。它基于 Logback 构建，提供了开箱即用的配置，同时保持了高度的可定制性。

## 特性

- **两种模式支持**：
  - Spring Boot 模式：利用 Spring Boot 的日志扩展（如 `%clr` 和 `%wEx` 转换模式）
  - 经典 Logback 模式：适用于非 Spring Boot 应用

- **控制台输出**：
  - 彩色日志输出，提高可读性
  - 可自定义的日志格式
  - 可根据环境禁用控制台输出

- **文件日志**：
  - 主日志文件（记录所有级别的日志）
  - 按日志级别分类存储（debug、info、warn、error）
  - 灵活的日志滚动策略（基于大小和时间）
  - 可配置的日志保留策略

- **性能优化**：
  - 异步日志写入
  - 可配置的队列大小和丢弃策略

- **环境感知**：
  - 根据 Spring profiles 自动调整日志配置
  - 开发环境（local、dev、test）：启用控制台和文件日志
  - 生产环境（sit、uat、prod）：仅启用文件日志，禁用控制台输出

## 安装

在 Maven 项目中添加以下依赖：

```xml
<dependency>
    <groupId>space.x9x.radp</groupId>
    <artifactId>radp-logging-spring-boot-starter</artifactId>
    <version>${radp.version}</version>
</dependency>
```

## 配置

### 基本配置

在 `application.yml` 或 `application.properties` 中进行配置：

```yaml
# 指定日志配置文件
logging:
  config: classpath:logback/springboot/templates/logback-spring.xml  # Spring Boot 应用
  # 或者
  # config: classpath:logback/classic/templates/logback-classic.xml  # 非 Spring Boot 应用
  
  # 日志文件路径
  file:
    path: ./logs
    
  # 日志级别配置
  level:
    root: info
    your.package.name: debug
```

### 自定义日志格式

```yaml
logging:
  # 自定义控制台日志格式
  pattern:
    console: "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx"
    # 自定义文件日志格式
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p ${PID:- } --- [%t] %-40.40logger{39} : %m%n%wEx"
    
  # 自定义字符集
  charset:
    console: UTF-8
    file: UTF-8
```

### 环境变量配置

以下环境变量可用于调整日志滚动策略：

- `LOGBACK_ROLLINGPOLICY_CLEAN_HISTORY_ON_START`: 启动时是否清理历史日志（默认：false）
- `LOGBACK_ROLLINGPOLICY_MAX_FILE_SIZE`: 单个日志文件的最大大小（默认：10MB）
- `LOGBACK_ROLLINGPOLICY_TOTAL_SIZE_CAP`: 日志文件的总大小上限（默认：1GB）
- `LOGBACK_ROLLINGPOLICY_MAX_HISTORY`: 日志文件的保留天数（默认：30）

## 使用示例

### 基本使用

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyService {
    private static final Logger log = LoggerFactory.getLogger(MyService.class);
    
    public void doSomething() {
        log.trace("跟踪信息");
        log.debug("调试信息");
        log.info("一般信息");
        log.warn("警告信息");
        log.error("错误信息");
    }
}
```

### 测试环境配置

对于测试环境，可以使用 `logback-test.xml` 或 `logback-classic-test.xml`：

```yaml
logging:
  config: classpath:logback/springboot/templates/logback-test.xml
```

## 自定义

如果需要更高级的定制，可以：

1. 创建自己的 Logback 配置文件，并引入 RADP 提供的组件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 引入 RADP 提供的组件 -->
    <include resource="logback/springboot/components/common.xml"/>
    <include resource="logback/springboot/components/console.xml"/>
    
    <!-- 自定义配置 -->
    <logger name="your.package" level="DEBUG"/>
    
    <root level="INFO">
        <appender-ref ref="radp_console_appender"/>
    </root>
</configuration>
```

2. 在 `application.yml` 中指定自定义配置文件：

```yaml
logging:
  config: classpath:your-custom-logback.xml
```

## 注意事项

- 确保 `spring.application.name` 已设置，它将用作日志文件名
- 在生产环境中，建议使用外部配置来覆盖默认设置
- 对于容器化部署，建议将日志路径设置为容器内的挂载卷