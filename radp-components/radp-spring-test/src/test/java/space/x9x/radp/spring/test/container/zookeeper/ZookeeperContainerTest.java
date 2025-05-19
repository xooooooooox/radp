package space.x9x.radp.spring.test.container.zookeeper;

import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ZookeeperContainer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class ZookeeperContainerTest {

    private ZookeeperContainer zookeeperContainer;
    private ZooKeeper zooKeeper;

    @BeforeEach
    void setUp() throws Exception {
        // Create and start the Zookeeper container
        zookeeperContainer = new ZookeeperContainer();
        zookeeperContainer.start();

        // Create a ZooKeeper client to connect to the container
        final CountDownLatch connectedSignal = new CountDownLatch(1);
        zooKeeper = new ZooKeeper(zookeeperContainer.getConnectionString(), 3000, event -> {
            if (event.getState() == org.apache.zookeeper.Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });

        // Wait for the connection to be established
        connectedSignal.await();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up resources
        if (zooKeeper != null) {
            zooKeeper.close();
        }

        if (zookeeperContainer != null && zookeeperContainer.isRunning()) {
            zookeeperContainer.stop();
        }
    }

    @Test
    void testContainerIsRunning() {
        // Verify that the container is running
        assertTrue(zookeeperContainer.isRunning());
    }

    @Test
    void testZookeeperConnection() throws Exception {
        // Create a node in ZooKeeper
        String path = "/test";
        byte[] data = "test-data".getBytes();
        zooKeeper.create(path, data, org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE,
                org.apache.zookeeper.CreateMode.PERSISTENT);

        // Get the data from the node
        byte[] retrievedData = zooKeeper.getData(path, false, null);

        // Verify that the data matches what we set
        assertArrayEquals(data, retrievedData);
    }

    @Test
    void testGetConnectionString() {
        // Verify that the connection string is not null or empty
        String connectionString = zookeeperContainer.getConnectionString();
        assertNotNull(connectionString);
        assertFalse(connectionString.isEmpty());

        // Verify that the connection string contains the host and port
        assertTrue(connectionString.contains(zookeeperContainer.getHost()));
        assertTrue(connectionString.contains(String.valueOf(
                zookeeperContainer.getMappedPort(ZookeeperContainer.DEFAULT_CLIENT_PORT))));
    }
}