package space.x9x.radp.spring.test.embedded.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import space.x9x.radp.spring.test.embedded.EmbeddedServer;

/**
 * Embedded Kafka server for testing.
 * This class provides a convenient way to start and manage a Kafka server for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
@Slf4j
public class EmbeddedKafkaServer implements EmbeddedServer {

    /**
     * Default port for the embedded Kafka server.
     */
    private static final int DEFAULT_PORT = 9092;

    /**
     * Default port for the embedded ZooKeeper server used by Kafka.
     */
    private static final int DEFAULT_ZOOKEEPER_PORT = 2181;

    /**
     * Default number of brokers in the embedded Kafka cluster.
     */
    private static final int DEFAULT_BROKER_COUNT = 1;

    private final EmbeddedKafkaRule embeddedKafka;
    private EmbeddedKafkaBroker kafkaBroker;
    private boolean isRunning = false;
    private int kafkaPort = DEFAULT_PORT;
    private int zookeeperPort = DEFAULT_ZOOKEEPER_PORT;

    /**
     * Creates a new EmbeddedKafkaServer with default settings.
     * This constructor initializes the Kafka broker with the default configuration.
     */
    public EmbeddedKafkaServer() {
        embeddedKafka = new EmbeddedKafkaRule(DEFAULT_BROKER_COUNT, true, DEFAULT_PORT);
        embeddedKafka.kafkaPorts(DEFAULT_PORT);
    }

    @Override
    public EmbeddedServer port(int port) {
        this.kafkaPort = port;
        embeddedKafka.kafkaPorts(port);
        return this;
    }

    /**
     * Sets the ZooKeeper port for the embedded Kafka server.
     * Note: This method may not be effective depending on the version of spring-kafka-test.
     *
     * @param port the ZooKeeper port
     * @return this instance for method chaining
     */
    public EmbeddedKafkaServer zookeeperPort(int port) {
        this.zookeeperPort = port;
        return this;
    }

    @Override
    public void startup() {
        try {
            embeddedKafka.before();
            kafkaBroker = embeddedKafka.getEmbeddedKafka();
            this.isRunning = true;
            log.info("Embedded Kafka server started on port {}", kafkaPort);
        } catch (Exception e) {
            this.isRunning = false;
            log.error("Failed to start embedded Kafka server", e);
            throw new RuntimeException("Failed to start embedded Kafka server", e);
        }
    }

    @Override
    public void shutdown() {
        if (!isRunning()) {
            return;
        }
        try {
            embeddedKafka.after();
            this.isRunning = false;
            log.info("Embedded Kafka server stopped");
        } catch (Exception e) {
            log.error("Failed to stop embedded Kafka server", e);
            throw new RuntimeException("Failed to stop embedded Kafka server", e);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Gets the broker addresses as a comma-separated string.
     *
     * @return the broker addresses
     */
    public String getBrokerAddresses() {
        if (kafkaBroker != null) {
            return kafkaBroker.getBrokersAsString();
        }
        return null;
    }
}
