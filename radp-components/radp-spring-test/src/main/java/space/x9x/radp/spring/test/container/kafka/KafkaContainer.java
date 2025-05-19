package space.x9x.radp.spring.test.container.kafka;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * Kafka container for testing.
 * This class provides a convenient way to start and manage a Kafka container for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
public class KafkaContainer extends GenericContainer<KafkaContainer> {

    /**
     * Default Kafka port.
     */
    public static final int KAFKA_PORT = 9092;

    /**
     * Default Docker image name for Kafka.
     */
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("confluentinc/cp-kafka");

    /**
     * Default Docker image tag for Kafka.
     * Using Confluent Platform 7.4.0 as the default version.
     */
    private static final String DEFAULT_TAG = "7.4.0";

    /**
     * Default startup timeout for the Kafka container.
     */
    private static final Duration DEFAULT_STARTUP_TIMEOUT = Duration.ofSeconds(120);

    /**
     * Creates a new Kafka container with the default image and startup timeout.
     */
    public KafkaContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Kafka container with the specified version and default startup timeout.
     *
     * @param version the Kafka version to use
     */
    public KafkaContainer(String version) {
        this(DEFAULT_IMAGE_NAME.withTag(version), DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Kafka container with the specified image name and default startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     */
    public KafkaContainer(DockerImageName dockerImageName) {
        this(dockerImageName, DEFAULT_STARTUP_TIMEOUT);
    }

    /**
     * Creates a new Kafka container with the specified image name and startup timeout.
     *
     * @param dockerImageName the Docker image name to use
     * @param startupTimeout  the startup timeout
     */
    public KafkaContainer(DockerImageName dockerImageName, Duration startupTimeout) {
        super(dockerImageName);

        withExposedPorts(KAFKA_PORT);
        withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:" + KAFKA_PORT);
        withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:" + KAFKA_PORT);
        withEnv("KAFKA_BROKER_ID", "1");
        withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1");
        withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1");
        withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1");
        withEnv("KAFKA_LOG_FLUSH_INTERVAL_MESSAGES", "10000");
        withEnv("KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS", "0");
        withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true");

        waitingFor(Wait.forLogMessage(".*started \\(kafka.server.KafkaServer\\).*", 1)
                .withStartupTimeout(startupTimeout));
    }

    /**
     * Gets the bootstrap servers string for connecting to the Kafka container.
     *
     * @return the bootstrap servers string
     */
    public String getBootstrapServers() {
        return String.format("%s:%d", getHost(), getMappedPort(KAFKA_PORT));
    }

    /**
     * Gets the bootstrap servers string for connecting to the Kafka container.
     * This is a convenience method that returns the same value as getBootstrapServers().
     *
     * @return the bootstrap servers string
     */
    public String getKafkaConnectionString() {
        return getBootstrapServers();
    }
}
