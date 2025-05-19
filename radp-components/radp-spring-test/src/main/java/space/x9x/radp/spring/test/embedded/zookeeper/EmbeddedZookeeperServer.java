package space.x9x.radp.spring.test.embedded.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.server.embedded.ZooKeeperServerEmbedded;
import space.x9x.radp.spring.test.embedded.EmbeddedServer;

import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Embedded Zookeeper server for testing.
 * This class provides a convenient way to start and manage a Zookeeper server for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
@Slf4j
public class EmbeddedZookeeperServer implements EmbeddedServer {

    /**
     * Default port for the embedded Zookeeper server.
     */
    public static final int DEFAULT_PORT = 2181;

    private final ZooKeeperServerEmbedded zooKeeperServer;
    private boolean isRunning = false;

    /**
     * Creates a new EmbeddedZookeeperServer with default settings.
     * This constructor initializes the Zookeeper server with default configuration.
     */
    public EmbeddedZookeeperServer() {
        Properties configuration = new Properties();
        configuration.put("clientPort", String.valueOf(DEFAULT_PORT));
        configuration.put("host", "localhost");
        configuration.put("ticktime", "2000");
        configuration.put("initLimit", "10");
        configuration.put("syncLimit", "5");
        configuration.put("dataDir", "/tmp/zookeeper");

        try {
            String path = EmbeddedZookeeperServer.class.getClassLoader().getResource("").toURI().toString();
            URI uri = new URL(path).toURI();
            this.zooKeeperServer = ZooKeeperServerEmbedded.builder()
                    .baseDir(Paths.get(uri))
                    .configuration(configuration)
                    .build();
        } catch (Exception e) {
            log.error("Failed to initialize embedded Zookeeper server", e);
            throw new RuntimeException("Failed to initialize embedded Zookeeper server", e);
        }
    }

    @Override
    public EmbeddedServer port(int port) {
        // Note: Port cannot be changed after initialization
        // This method is provided for API compatibility
        return this;
    }

    @Override
    public void startup() {
        try {
            this.zooKeeperServer.start();
            this.isRunning = true;
            log.info("Embedded Zookeeper server started on port {}", DEFAULT_PORT);
        } catch (Exception e) {
            log.error("Failed to start embedded Zookeeper server", e);
        }
    }

    @Override
    public void shutdown() {
        if (!isRunning()) {
            return;
        }
        try {
            this.zooKeeperServer.close();
            this.isRunning = false;
            log.info("Embedded Zookeeper server stopped");
        } catch (Exception e) {
            log.error("Failed to stop embedded Zookeeper server", e);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
