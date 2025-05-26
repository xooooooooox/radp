# RADP Spring Test

[![Maven Central Version](https://img.shields.io/maven-central/v/space.x9x.radp/radp-spring-test?style=for-the-badge)](https://central.sonatype.com/artifact/space.x9x.radp/radp-spring-test)

## Introduction

RADP Spring Test provides enhanced testing capabilities for Spring applications, with a focus on container-based and
embedded testing. It offers a comprehensive solution for testing Spring applications with various databases and services
using both TestContainers and embedded components.

## Features

- **TestContainers Integration**:
  - Support for various databases: PostgreSQL, MySQL, MariaDB, MongoDB
  - Support for messaging systems: Kafka
  - Support for caching: Redis
  - Support for search engines: Elasticsearch
  - Support for service discovery: Zookeeper, Nacos
  - Support for web servers: Nginx

- **Container Management**:
  - Container reuse capabilities for faster test execution
  - Custom wait strategies for container readiness
  - Simplified container configuration and setup

- **Embedded Testing**:
  - Embedded Redis for in-memory cache testing
  - Integration with Spring's testing framework

- **Testing Utilities**:
  - JUnit 5 integration
  - Spock Framework support for behavior-driven testing
  - AssertJ for fluent assertions

## Getting Started

### Installation

Add the dependency to your Maven project:

```xml

<dependency>
    <groupId>space.x9x.radp</groupId>
    <artifactId>radp-spring-test</artifactId>
    <version>${radp.version}</version>
    <scope>test</scope>
</dependency>
```

### Usage

#### TestContainers Example

Here's an example of using TestContainers with container reuse for Redis:

```java

@Testcontainers
class RedisContainerTest {

    @Container
    private static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:6.2.6-alpine")
            .withExposedPorts(6379)
            .withReuse(true); // Enable container reuse

    @Test
    void testRedisConnection() {
        assertTrue(REDIS_CONTAINER.isRunning(), "Redis container should be running");
        int mappedPort = REDIS_CONTAINER.getMappedPort(6379);

        // Use the container for testing
        // ...
    }
}
```

#### Container Reuse

TestContainers supports container reuse to improve test execution speed and reduce resource consumption:

1. **Global Enablement**: Create a `.testcontainers.properties` file in your home directory with
   `testcontainers.reuse.enable=true`
2. **Per-Container Enablement**: Call `.withReuse(true)` when creating a container

Important considerations:

- Set a unique identifier for reused containers with `.withLabel("reuse.UUID", "xxxx")`
- Reused containers are not automatically stopped after tests
- Reuse is best for read-only tests, as state modifications may affect subsequent tests

#### Custom Wait Strategies

You can define custom wait strategies to ensure containers are fully ready before tests begin:

```java

@Container
private final GenericContainer<?> customWaitContainer = new GenericContainer<>("nginx:1.21.6")
        .withExposedPorts(80)
        .waitingFor(Wait.forHttp("/")
                .forStatusCode(200)
                .withStartupTimeout(Duration.ofSeconds(30)));
```

## Advanced Usage

### Database Container Example

Testing with a MySQL database container:

```java

@Testcontainers
class MysqlContainerTest {

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testDatabaseConnection() {
        assertTrue(MYSQL_CONTAINER.isRunning(), "MySQL container should be running");

        // Use JDBC URL from container for connection
        String jdbcUrl = MYSQL_CONTAINER.getJdbcUrl();

        // Test database operations
        // ...
    }
}
```

### Embedded Redis Example

Using embedded Redis for testing:

```java
class EmbeddedRedisTest {

    private RedisServer redisServer;

    @BeforeEach
    void setup() {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterEach
    void tearDown() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }

    @Test
    void testWithEmbeddedRedis() {
        // Test with embedded Redis
        // ...
    }
}
```

## License

[Apache 2.0 License—](../../LICENSE)Copyright © 2024 xooooooooox
and [contributors](https://github.com/xooooooooox/radp/graphs/contributors)