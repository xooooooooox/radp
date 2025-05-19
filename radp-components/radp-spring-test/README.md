# RADP Spring Test

RADP Spring Test is a testing library that provides utilities for creating and managing test environments for Spring
applications. It supports both embedded servers and Docker containers for various services like Redis, MySQL, Zookeeper,
and Kafka.

## Features

- **Embedded Servers**: Run lightweight embedded servers for testing without external dependencies
    - Redis
    - Zookeeper
    - Kafka

- **Docker Containers**: Run services in Docker containers for more realistic testing environments
    - Redis
    - MySQL 8
    - Zookeeper
    - Kafka
  - Elasticsearch
  - MongoDB
  - Nginx
  - MariaDB

## Installation

Add the following dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>space.x9x.radp</groupId>
    <artifactId>radp-spring-test</artifactId>
    <version>${radp.version}</version>
    <scope>test</scope>
</dependency>
```

## Usage

### Embedded Servers

#### Redis

```java
// Create and start a Redis server with default settings
EmbeddedServer redisServer = EmbeddedServerHelper.redisServer();
EmbeddedServerHelper.

startServer(redisServer);

// Use the Redis server in your tests
// ...

// Stop the server when done
EmbeddedServerHelper.

stopServer(redisServer);
```

#### Zookeeper

```java
// Create and start a Zookeeper server
EmbeddedServer zookeeperServer = EmbeddedServerHelper.zookeeperServer();
EmbeddedServerHelper.

startServer(zookeeperServer);

// Use the Zookeeper server in your tests
// ...

// Stop the server when done
EmbeddedServerHelper.

stopServer(zookeeperServer);
```

#### Kafka

```java
// Create and start a Kafka server with custom ports
EmbeddedKafkaServer kafkaServer = EmbeddedServerHelper.kafkaServer(9093, 2182);
EmbeddedServerHelper.

startServer(kafkaServer);

// Use the Kafka server in your tests
String brokerAddresses = kafkaServer.getBrokerAddresses();

// Stop the server when done
EmbeddedServerHelper.

stopServer(kafkaServer);
```

### Docker Containers

#### Redis

```java
// Create and start a Redis container
RedisContainer redisContainer = ContainerHelper.redisContainer();
ContainerHelper.

startContainer(redisContainer);

// Use the Redis container in your tests
String redisUrl = redisContainer.getRedisConnectionUrl();

// Stop the container when done
ContainerHelper.

stopContainer(redisContainer);
```

#### MySQL 8

```java
// Create and start a MySQL 8 container
MySQL8Container mysqlContainer = ContainerHelper.mysql8Container();
ContainerHelper.

startContainer(mysqlContainer);

// Use the MySQL container in your tests
String jdbcUrl = mysqlContainer.getJdbcConnectionUrl();
String username = mysqlContainer.getUsername();
String password = mysqlContainer.getPassword();

// Stop the container when done
ContainerHelper.

stopContainer(mysqlContainer);
```

#### Zookeeper

```java
// Create and start a Zookeeper container
ZookeeperContainer zookeeperContainer = ContainerHelper.zookeeperContainer();
ContainerHelper.

startContainer(zookeeperContainer);

// Use the Zookeeper container in your tests
String connectionString = zookeeperContainer.getConnectionString();

// Stop the container when done
ContainerHelper.

stopContainer(zookeeperContainer);
```

#### Kafka

```java
// Create and start a Kafka container
KafkaContainer kafkaContainer = ContainerHelper.kafkaContainer();
ContainerHelper.

startContainer(kafkaContainer);

// Use the Kafka container in your tests
String bootstrapServers = kafkaContainer.getBootstrapServers();

// Stop the container when done
ContainerHelper.

stopContainer(kafkaContainer);
```

#### Elasticsearch

```java
// Create and start an Elasticsearch container
ElasticsearchContainer elasticsearchContainer = ContainerHelper.elasticsearchContainer();
ContainerHelper.

startContainer(elasticsearchContainer);

// Use the Elasticsearch container in your tests
String httpUrl = elasticsearchContainer.getHttpHostAddress();

// Stop the container when done
ContainerHelper.

stopContainer(elasticsearchContainer);
```

#### MongoDB

```java
// Create and start a MongoDB container
MongoDBContainer mongoDBContainer = ContainerHelper.mongoDBContainer();
ContainerHelper.

startContainer(mongoDBContainer);

// Use the MongoDB container in your tests
String connectionString = mongoDBContainer.getConnectionString();

// Stop the container when done
ContainerHelper.

stopContainer(mongoDBContainer);
```

#### Nginx

```java
// Create and start a Nginx container
NginxContainer nginxContainer = ContainerHelper.nginxContainer();
ContainerHelper.

startContainer(nginxContainer);

// Use the Nginx container in your tests
String httpUrl = nginxContainer.getHttpUrl();

// Stop the container when done
ContainerHelper.

stopContainer(nginxContainer);
```

#### MariaDB

```java
// Create and start a MariaDB container
MariaDBContainer mariaDBContainer = ContainerHelper.mariaDBContainer();
ContainerHelper.

startContainer(mariaDBContainer);

// Use the MariaDB container in your tests
String jdbcUrl = mariaDBContainer.getJdbcConnectionUrl();
String username = mariaDBContainer.getUsername();
String password = mariaDBContainer.getPassword();

// Stop the container when done
ContainerHelper.

stopContainer(mariaDBContainer);
```

## Configuration Options

### Embedded Servers

- **Redis**
    - Port: Default is 6379
    - Password: Optional

- **Zookeeper**
    - Port: Default is 2181

- **Kafka**
    - Kafka Port: Default is 9092
    - Zookeeper Port: Default is 2181

### Docker Containers

- **Redis**
    - Image: Default is "redis:latest"

- **MySQL 8**
    - Image: Default is "mysql:8.0.33"
    - Startup Timeout: Default is 60 seconds

- **Zookeeper**
    - Image: Default is "zookeeper:3.8.1"
    - Startup Timeout: Default is 60 seconds

- **Kafka**
    - Image: Default is "confluentinc/cp-kafka:7.4.0"
    - Startup Timeout: Default is 120 seconds

- **Elasticsearch**
  - Image: Default is "docker.elastic.co/elasticsearch/elasticsearch:8.7.1"
  - Startup Timeout: Default is 120 seconds

- **MongoDB**
  - Image: Default is "mongo:6.0.6"
  - Startup Timeout: Default is 60 seconds

- **Nginx**
  - Image: Default is "nginx:1.25.1"
  - Startup Timeout: Default is 30 seconds

- **MariaDB**
  - Image: Default is "mariadb:11.1.2"
  - Startup Timeout: Default is 60 seconds

## Troubleshooting

### Common Issues

- **Port conflicts**: If you encounter port conflicts, you can specify custom ports when creating servers or containers.

- **Docker not available**: Ensure Docker is installed and running on your machine when using container-based tests.

- **Memory issues**: Embedded servers and containers can consume significant memory. Adjust your JVM memory settings if
  needed.

### Logging

Both embedded servers and containers log their status to the console. You can adjust the log level in your logging
configuration to see more or less information.

## License

This project is licensed under the terms of the license provided with the RADP framework.
