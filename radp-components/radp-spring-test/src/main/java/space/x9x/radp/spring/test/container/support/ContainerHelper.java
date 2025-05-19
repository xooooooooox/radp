package space.x9x.radp.spring.test.container.support;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import space.x9x.radp.spring.test.container.kafka.KafkaContainer;
import space.x9x.radp.spring.test.container.mysql.MySQL8Container;
import space.x9x.radp.spring.test.container.redis.RedisContainer;
import space.x9x.radp.spring.test.container.zookeeper.ZookeeperContainer;

import java.time.Duration;

/**
 * Helper class for working with containers.
 * This class provides utility methods for creating, configuring, and managing containers for testing.
 * It supports various container types including Redis, MySQL, Zookeeper, and Kafka.
 *
 * @author x9x
 * @since 2024-10-30
 */
@Slf4j
public class ContainerHelper {

    // Private constructor to prevent instantiation
    private ContainerHelper() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Creates a new Redis container with the default image and exposed port.
     *
     * @return a new Redis container
     */
    public static RedisContainer redisContainer() {
        return new RedisContainer();
    }

    /**
     * Creates a new Redis container with the specified image and default exposed port.
     *
     * @param dockerImageName the Docker image name to use
     * @return a new Redis container
     */
    public static RedisContainer redisContainer(String dockerImageName) {
        return new RedisContainer(dockerImageName);
    }

    /**
     * Creates a new MySQL 8 container with the default image and exposed port.
     *
     * @return a new MySQL 8 container
     */
    public static MySQL8Container mysql8Container() {
        return new MySQL8Container();
    }

    /**
     * Creates a new MySQL 8 container with the specified image and default exposed port.
     *
     * @param dockerImageName the Docker image name to use
     * @return a new MySQL 8 container
     */
    public static MySQL8Container mysql8Container(String dockerImageName) {
        return new MySQL8Container(dockerImageName);
    }

    /**
     * Creates a new MySQL 8 container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     * @return a new MySQL 8 container
     */
    public static MySQL8Container mysql8Container(DockerImageName dockerImageName, Duration startupTimeout) {
        return new MySQL8Container(dockerImageName, startupTimeout);
    }

    /**
     * Creates a new Zookeeper container with the default image and exposed port.
     *
     * @return a new Zookeeper container
     */
    public static ZookeeperContainer zookeeperContainer() {
        return new ZookeeperContainer();
    }

    /**
     * Creates a new Zookeeper container with the specified version and default exposed port.
     *
     * @param version the Zookeeper version to use
     * @return a new Zookeeper container
     */
    public static ZookeeperContainer zookeeperContainer(String version) {
        return new ZookeeperContainer(version);
    }

    /**
     * Creates a new Zookeeper container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     * @return a new Zookeeper container
     */
    public static ZookeeperContainer zookeeperContainer(DockerImageName dockerImageName, Duration startupTimeout) {
        return new ZookeeperContainer(dockerImageName, startupTimeout);
    }

    /**
     * Creates a new Kafka container with the default image and exposed port.
     *
     * @return a new Kafka container
     */
    public static KafkaContainer kafkaContainer() {
        return new KafkaContainer();
    }

    /**
     * Creates a new Kafka container with the specified version and default exposed port.
     *
     * @param version the Kafka version to use
     * @return a new Kafka container
     */
    public static KafkaContainer kafkaContainer(String version) {
        return new KafkaContainer(version);
    }

    /**
     * Creates a new Kafka container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     * @return a new Kafka container
     */
    public static KafkaContainer kafkaContainer(DockerImageName dockerImageName, Duration startupTimeout) {
        return new KafkaContainer(dockerImageName, startupTimeout);
    }

    /**
     * Starts the container if it's not already running.
     * This method handles exceptions that might occur during container startup.
     *
     * @param container the container to start
     * @param <T>       the container type
     * @return the started container
     * @throws RuntimeException if the container fails to start
     */
    public static <T extends GenericContainer<T>> T startContainer(T container) {
        if (!container.isRunning()) {
            try {
                log.info("Starting container: {}", container.getDockerImageName());
                container.start();
                log.info("Container started successfully: {}", container.getDockerImageName());
            } catch (Exception e) {
                log.error("Failed to start container: {}", container.getDockerImageName(), e);
                throw new RuntimeException("Failed to start container: " + container.getDockerImageName(), e);
            }
        } else {
            log.debug("Container already running: {}", container.getDockerImageName());
        }
        return container;
    }

    /**
     * Stops the container if it's running.
     * This method handles exceptions that might occur during container shutdown.
     *
     * @param container the container to stop
     * @param <T>       the container type
     */
    public static <T extends GenericContainer<T>> void stopContainer(T container) {
        if (container != null && container.isRunning()) {
            try {
                log.info("Stopping container: {}", container.getDockerImageName());
                container.stop();
                log.info("Container stopped successfully: {}", container.getDockerImageName());
            } catch (Exception e) {
                log.error("Failed to stop container: {}", container.getDockerImageName(), e);
            }
        } else if (container != null) {
            log.debug("Container not running: {}", container.getDockerImageName());
        }
    }
}
