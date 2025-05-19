package space.x9x.radp.spring.test.container.mariadb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link MariaDBContainer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class MariaDBContainerTest {

    private MariaDBContainer mariaDBContainer;

    @BeforeEach
    void setUp() {
        // Create and start the MariaDB container
        mariaDBContainer = new MariaDBContainer();
        mariaDBContainer.start();
    }

    @AfterEach
    void tearDown() {
        // Stop the container
        if (mariaDBContainer != null && mariaDBContainer.isRunning()) {
            mariaDBContainer.stop();
        }
    }

    @Test
    void testContainerIsRunning() {
        // Verify that the container is running
        assertTrue(mariaDBContainer.isRunning());
    }

    @Test
    void testJdbcConnection() throws Exception {
        // Get the JDBC URL, username, and password from the container
        String jdbcUrl = mariaDBContainer.getJdbcUrl();
        String username = mariaDBContainer.getUsername();
        String password = mariaDBContainer.getPassword();

        // Create a connection to the database
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Verify that the connection is valid
            assertTrue(connection.isValid(5));

            // Create a test table
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE test_table (id INT, name VARCHAR(255))");
                statement.execute("INSERT INTO test_table VALUES (1, 'test')");

                // Query the table
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM test_table")) {
                    // Verify that we can retrieve the data
                    assertTrue(resultSet.next());
                    assertEquals(1, resultSet.getInt("id"));
                    assertEquals("test", resultSet.getString("name"));
                }
            }
        }
    }

    @Test
    void testGetJdbcConnectionUrl() {
        // Verify that getJdbcConnectionUrl returns the same value as getJdbcUrl
        assertEquals(mariaDBContainer.getJdbcUrl(), mariaDBContainer.getJdbcConnectionUrl());
    }
}