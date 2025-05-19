package space.x9x.radp.spring.test.container.support;

import org.junit.jupiter.api.Test;
import org.testcontainers.utility.DockerImageName;
import space.x9x.radp.spring.test.container.elasticsearch.ElasticsearchContainer;
import space.x9x.radp.spring.test.container.kafka.KafkaContainer;
import space.x9x.radp.spring.test.container.mariadb.MariaDBContainer;
import space.x9x.radp.spring.test.container.mongodb.MongoDBContainer;
import space.x9x.radp.spring.test.container.mysql.MySQL8Container;
import space.x9x.radp.spring.test.container.nginx.NginxContainer;
import space.x9x.radp.spring.test.container.redis.RedisContainer;
import space.x9x.radp.spring.test.container.zookeeper.ZookeeperContainer;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ContainerHelper}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class ContainerHelperTest {

    @Test
    void testRedisContainer() {
        // Test creating a Redis container with default settings
        RedisContainer container = ContainerHelper.redisContainer();
        assertNotNull(container);
        assertEquals("redis:latest", container.getDockerImageName().toString());

        // Test creating a Redis container with custom image
        String customImage = "redis:7.0";
        RedisContainer customContainer = ContainerHelper.redisContainer(customImage);
        assertNotNull(customContainer);
        assertEquals(customImage, customContainer.getDockerImageName().toString());
    }

    @Test
    void testMySql8Container() {
        // Test creating a MySQL 8 container with default settings
        MySQL8Container container = ContainerHelper.mysql8Container();
        assertNotNull(container);
        assertTrue(container.getDockerImageName().toString().contains("mysql"));

        // Test creating a MySQL 8 container with custom image
        String customImage = "mysql:8.0.33";
        MySQL8Container customContainer = ContainerHelper.mysql8Container(customImage);
        assertNotNull(customContainer);
        assertEquals(customImage, customContainer.getDockerImageName().toString());

        // Test creating a MySQL 8 container with custom image and timeout
        DockerImageName imageName = DockerImageName.parse(customImage);
        Duration timeout = Duration.ofSeconds(120);
        MySQL8Container timeoutContainer = ContainerHelper.mysql8Container(imageName, timeout);
        assertNotNull(timeoutContainer);
        assertEquals(imageName.toString(), timeoutContainer.getDockerImageName().toString());
    }

    @Test
    void testZookeeperContainer() {
        // Test creating a Zookeeper container with default settings
        ZookeeperContainer container = ContainerHelper.zookeeperContainer();
        assertNotNull(container);
        assertTrue(container.getDockerImageName().toString().contains("zookeeper"));

        // Test creating a Zookeeper container with custom version
        String customVersion = "3.7.1";
        ZookeeperContainer versionContainer = ContainerHelper.zookeeperContainer(customVersion);
        assertNotNull(versionContainer);
        assertTrue(versionContainer.getDockerImageName().toString().contains(customVersion));

        // Test creating a Zookeeper container with custom image and timeout
        DockerImageName imageName = DockerImageName.parse("zookeeper:3.8.0");
        Duration timeout = Duration.ofSeconds(120);
        ZookeeperContainer timeoutContainer = ContainerHelper.zookeeperContainer(imageName, timeout);
        assertNotNull(timeoutContainer);
        assertEquals(imageName.toString(), timeoutContainer.getDockerImageName().toString());
    }

    @Test
    void testKafkaContainer() {
        // Test creating a Kafka container with default settings
        KafkaContainer container = ContainerHelper.kafkaContainer();
        assertNotNull(container);
        assertTrue(container.getDockerImageName().toString().contains("kafka"));

        // Test creating a Kafka container with custom version
        String customVersion = "7.3.0";
        KafkaContainer versionContainer = ContainerHelper.kafkaContainer(customVersion);
        assertNotNull(versionContainer);
        assertTrue(versionContainer.getDockerImageName().toString().contains(customVersion));

        // Test creating a Kafka container with custom image and timeout
        DockerImageName imageName = DockerImageName.parse("confluentinc/cp-kafka:7.4.0");
        Duration timeout = Duration.ofSeconds(120);
        KafkaContainer timeoutContainer = ContainerHelper.kafkaContainer(imageName, timeout);
        assertNotNull(timeoutContainer);
        assertEquals(imageName.toString(), timeoutContainer.getDockerImageName().toString());
    }

    // Note: We're not testing startContainer and stopContainer methods directly
    // because they require actual container instances to be started and stopped.
    // These methods are tested indirectly through the container-specific tests
    // in other test classes.

    @Test
    void testElasticsearchContainer() {
        // Test creating an Elasticsearch container with default settings
        ElasticsearchContainer container = ContainerHelper.elasticsearchContainer();
        assertNotNull(container);
        assertTrue(container.getDockerImageName().toString().contains("elasticsearch"));

        // Test creating an Elasticsearch container with custom image
        String customImage = "docker.elastic.co/elasticsearch/elasticsearch:8.6.0";
        ElasticsearchContainer customContainer = ContainerHelper.elasticsearchContainer(customImage);
        assertNotNull(customContainer);
        assertEquals(customImage, customContainer.getDockerImageName().toString());

        // Test creating an Elasticsearch container with custom image and timeout
        DockerImageName imageName = DockerImageName.parse(customImage);
        Duration timeout = Duration.ofSeconds(180);
        ElasticsearchContainer timeoutContainer = ContainerHelper.elasticsearchContainer(imageName, timeout);
        assertNotNull(timeoutContainer);
        assertEquals(imageName.toString(), timeoutContainer.getDockerImageName().toString());
    }

    @Test
    void testMongoDBContainer() {
        // Test creating a MongoDB container with default settings
        MongoDBContainer container = ContainerHelper.mongoDBContainer();
        assertNotNull(container);
        assertTrue(container.getDockerImageName().toString().contains("mongo"));

        // Test creating a MongoDB container with custom image
        String customImage = "mongo:6.0.0";
        MongoDBContainer customContainer = ContainerHelper.mongoDBContainer(customImage);
        assertNotNull(customContainer);
        assertEquals(customImage, customContainer.getDockerImageName().toString());

        // Test creating a MongoDB container with custom image and timeout
        DockerImageName imageName = DockerImageName.parse(customImage);
        Duration timeout = Duration.ofSeconds(90);
        MongoDBContainer timeoutContainer = ContainerHelper.mongoDBContainer(imageName, timeout);
        assertNotNull(timeoutContainer);
        assertEquals(imageName.toString(), timeoutContainer.getDockerImageName().toString());
    }

    @Test
    void testNginxContainer() {
        // Test creating a Nginx container with default settings
        NginxContainer container = ContainerHelper.nginxContainer();
        assertNotNull(container);
        assertTrue(container.getDockerImageName().toString().contains("nginx"));

        // Test creating a Nginx container with custom image
        String customImage = "nginx:1.24.0";
        NginxContainer customContainer = ContainerHelper.nginxContainer(customImage);
        assertNotNull(customContainer);
        assertEquals(customImage, customContainer.getDockerImageName().toString());

        // Test creating a Nginx container with custom image and timeout
        DockerImageName imageName = DockerImageName.parse(customImage);
        Duration timeout = Duration.ofSeconds(45);
        NginxContainer timeoutContainer = ContainerHelper.nginxContainer(imageName, timeout);
        assertNotNull(timeoutContainer);
        assertEquals(imageName.toString(), timeoutContainer.getDockerImageName().toString());
    }

    @Test
    void testMariaDBContainer() {
        // Test creating a MariaDB container with default settings
        MariaDBContainer container = ContainerHelper.mariaDBContainer();
        assertNotNull(container);
        assertTrue(container.getDockerImageName().toString().contains("mariadb"));

        // Test creating a MariaDB container with custom image
        String customImage = "mariadb:11.0.0";
        MariaDBContainer customContainer = ContainerHelper.mariaDBContainer(customImage);
        assertNotNull(customContainer);
        assertEquals(customImage, customContainer.getDockerImageName().toString());

        // Test creating a MariaDB container with custom image and timeout
        DockerImageName imageName = DockerImageName.parse(customImage);
        Duration timeout = Duration.ofSeconds(90);
        MariaDBContainer timeoutContainer = ContainerHelper.mariaDBContainer(imageName, timeout);
        assertNotNull(timeoutContainer);
        assertEquals(imageName.toString(), timeoutContainer.getDockerImageName().toString());
    }

    @Test
    void testPrivateConstructor() {
        // Test that the private constructor throws an exception when called via reflection
        Exception exception = assertThrows(Exception.class, () -> {
            var constructor = ContainerHelper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        // Verify that the exception message indicates that the class should not be instantiated
        assertTrue(exception.getCause().getMessage().contains("should not be instantiated"));
    }
}
