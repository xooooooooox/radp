package space.x9x.radp.spring.test.container.support;

import org.junit.jupiter.api.Test;
import org.testcontainers.utility.DockerImageName;
import space.x9x.radp.spring.test.container.kafka.KafkaContainer;
import space.x9x.radp.spring.test.container.mysql.MySQL8Container;
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
