package space.x9x.radp.spring.test.container.mysql;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * MySQL 8 container for testing.
 * This class provides a convenient way to start and manage a MySQL 8 container for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
public class MySQL8Container extends MySQLContainer<MySQL8Container> {

    /**
     * Default Docker image name for MySQL.
     */
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("mysql");

    /**
     * Default Docker image tag for MySQL.
     * Using MySQL 8.0.33 as the default version.
     */
    private static final String DEFAULT_TAG = "8.0.33";

    /**
     * Default startup timeout for the MySQL container.
     */
    private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(60);

    /**
     * Creates a new MySQL container with the default image and startup timeout.
     */
    public MySQL8Container() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MySQL container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public MySQL8Container(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MySQL container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public MySQL8Container(DockerImageName dockerImageName) {
        this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MySQL container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     */
    public MySQL8Container(DockerImageName dockerImageName, Duration startupTimeout) {
        super(dockerImageName);
        waitingFor(Wait.forListeningPort().withStartupTimeout(startupTimeout));
    }

    /**
     * Gets the JDBC URL for connecting to the MySQL container.
     * This is a convenience method that returns the same value as getJdbcUrl().
     *
     * @return the JDBC URL
     */
    public String getJdbcConnectionUrl() {
        return getJdbcUrl();
    }
}