package space.x9x.radp.spring.test.container.elasticsearch;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * Elasticsearch container for testing.
 * This class provides a convenient way to start and manage an Elasticsearch container for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
public class ElasticsearchContainer extends org.testcontainers.elasticsearch.ElasticsearchContainer {

    /**
     * Default Docker image name for Elasticsearch.
     */
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch");

    /**
     * Default Docker image tag for Elasticsearch.
     * Using Elasticsearch 8.7.1 as the default version.
     */
    private static final String DEFAULT_TAG = "8.7.1";

    /**
     * Default HTTP port for Elasticsearch.
     */
    public static final int ELASTICSEARCH_PORT = 9200;

    /**
     * Default startup timeout for the Elasticsearch container.
     */
    private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(120);

    /**
     * Creates a new Elasticsearch container with the default image and startup timeout.
     */
    public ElasticsearchContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Elasticsearch container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public ElasticsearchContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Elasticsearch container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public ElasticsearchContainer(DockerImageName dockerImageName) {
        this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Elasticsearch container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     */
    public ElasticsearchContainer(DockerImageName dockerImageName, Duration startupTimeout) {
        super(dockerImageName);
        waitingFor(Wait.forHttp("/").withStartupTimeout(startupTimeout));
    }

    /**
     * Gets the HTTP URL for connecting to the Elasticsearch container.
     *
     * @return the HTTP URL
     */
    public String getHttpHostAddress() {
        return String.format("http://%s:%d", getHost(), getMappedPort(ELASTICSEARCH_PORT));
    }
}
