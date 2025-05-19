package space.x9x.radp.spring.test.container.nginx;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * Nginx container for testing.
 * This class provides a convenient way to start and manage a Nginx container for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
public class NginxContainer extends org.testcontainers.containers.NginxContainer {

    /**
     * Default Docker image name for Nginx.
     */
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("nginx");

    /**
     * Default Docker image tag for Nginx.
     * Using Nginx 1.25.1 as the default version.
     */
    private static final String DEFAULT_TAG = "1.25.1";

    /**
     * Default HTTP port for Nginx.
     */
    public static final int NGINX_PORT = 80;

    /**
     * Default startup timeout for the Nginx container.
     */
    private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(30);

    /**
     * Creates a new Nginx container with the default image and startup timeout.
     */
    public NginxContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Nginx container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public NginxContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Nginx container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public NginxContainer(DockerImageName dockerImageName) {
        this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Nginx container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     */
    public NginxContainer(DockerImageName dockerImageName, Duration startupTimeout) {
        super(dockerImageName);
        waitingFor(Wait.forHttp("/").withStartupTimeout(startupTimeout));
    }

    /**
     * Gets the HTTP URL for connecting to the Nginx container.
     *
     * @return the HTTP URL
     */
    public String getHttpUrl() {
        return String.format("http://%s:%d", getHost(), getMappedPort(NGINX_PORT));
    }
}