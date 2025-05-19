package space.x9x.radp.spring.test.container.mongodb;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * MongoDB container for testing.
 * This class provides a convenient way to start and manage a MongoDB container for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
public class MongoDBContainer extends org.testcontainers.containers.MongoDBContainer {

    /**
     * Default Docker image name for MongoDB.
     */
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("mongo");

    /**
     * Default Docker image tag for MongoDB.
     * Using MongoDB 6.0.6 as the default version.
     */
    private static final String DEFAULT_TAG = "6.0.6";

    /**
     * Default port for MongoDB.
     */
    public static final int MONGODB_PORT = 27017;

    /**
     * Default startup timeout for the MongoDB container.
     */
    private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(60);

    /**
     * Creates a new MongoDB container with the default image and startup timeout.
     */
    public MongoDBContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MongoDB container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public MongoDBContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MongoDB container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public MongoDBContainer(DockerImageName dockerImageName) {
        this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new MongoDB container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     */
    public MongoDBContainer(DockerImageName dockerImageName, Duration startupTimeout) {
        super(dockerImageName);
        waitingFor(Wait.forLogMessage(".*waiting for connections.*", 1).withStartupTimeout(startupTimeout));
    }

    /**
     * Gets the connection string for connecting to the MongoDB container.
     *
     * @return the connection string
     */
    public String getConnectionString() {
        return String.format("mongodb://%s:%d", getHost(), getMappedPort(MONGODB_PORT));
    }
}