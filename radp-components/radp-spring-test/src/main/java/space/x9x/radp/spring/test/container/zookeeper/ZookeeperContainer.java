package space.x9x.radp.spring.test.container.zookeeper;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * Zookeeper container for testing.
 * This class provides a convenient way to start and manage a Zookeeper container for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
public class ZookeeperContainer extends GenericContainer<ZookeeperContainer> {

    /**
     * Default Docker image name for Zookeeper.
     */
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("zookeeper");

    /**
     * Default Docker image tag for Zookeeper.
     * Using Zookeeper 3.8.1 as the default version.
     */
    private static final String DEFAULT_TAG = "3.8.1";

    /**
     * Default client port for Zookeeper.
     */
    public static final int DEFAULT_CLIENT_PORT = 2181;

    /**
     * Default admin port for Zookeeper.
     */
    public static final int DEFAULT_ADMIN_PORT = 8080;

    /**
     * Default startup timeout for the Zookeeper container.
     */
    public static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(60);

    /**
     * Creates a new Zookeeper container with the default image and startup timeout.
     */
    public ZookeeperContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Zookeeper container with the specified version and default startup timeout.
     *
     * @param version the Zookeeper version to use
     */
    public ZookeeperContainer(String version) {
        this(DEFAULT_IMAGE_NAME.withTag(version), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Zookeeper container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public ZookeeperContainer(DockerImageName dockerImageName) {
        this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Zookeeper container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     */
    public ZookeeperContainer(DockerImageName dockerImageName, Duration startupTimeout) {
        super(dockerImageName);
        withExposedPorts(DEFAULT_CLIENT_PORT, DEFAULT_ADMIN_PORT);
        waitingFor(Wait.forListeningPort().withStartupTimeout(startupTimeout));
    }

    /**
     * Gets the connection string for connecting to the Zookeeper container.
     *
     * @return the connection string in the format host:port
     */
    public String getConnectionString() {
        return getHost() + ":" + getMappedPort(DEFAULT_CLIENT_PORT);
    }
}