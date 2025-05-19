package space.x9x.radp.spring.test.container.mariadb;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * MariaDB container for testing.
 * This class provides a convenient way to start and manage a MariaDB container for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
public class MariaDBContainer extends org.testcontainers.containers.MariaDBContainer<MariaDBContainer> {

    /**
     * Default Docker image name for MariaDB.
     */
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("mariadb");

    /**
     * Default Docker image tag for MariaDB.
     * Using MariaDB 11.1.2 as the default version.
     */
    private static final String DEFAULT_TAG = "11.1.2";

    /**
     * Default startup timeout for the MariaDB container.
     */
    private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(60);

    /**
     * Creates a new MariaDB container with the default image and startup timeout.
     */
    public MariaDBContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MariaDB container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public MariaDBContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MariaDB container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public MariaDBContainer(DockerImageName dockerImageName) {
        this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MariaDB container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     */
    public MariaDBContainer(DockerImageName dockerImageName, Duration startupTimeout) {
        super(dockerImageName);
        waitingFor(Wait.forListeningPort().withStartupTimeout(startupTimeout));
    }

    /**
     * Gets the JDBC URL for connecting to the MariaDB container.
     * This is a convenience method that returns the same value as getJdbcUrl().
     *
     * @return the JDBC URL
     */
    public String getJdbcConnectionUrl() {
        return getJdbcUrl();
    }
}