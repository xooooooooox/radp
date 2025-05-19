package space.x9x.radp.spring.test.container.mysql;

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
 * Tests for {@link MySQL8Container}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class MySQL8ContainerTest {

    private MySQL8Container mysql8Container;

    @BeforeEach
    void setUp() {
        // Create and start the MySQL container
        mysql8Container = new MySQL8Container();
        mysql8Container.start();
    }

    @AfterEach
    void tearDown() {
        // Stop the container
        if (mysql8Container != null && mysql8Container.isRunning()) {
            mysql8Container.stop();
        }
    }

    @Test
    void testContainerIsRunning() {
        // Verify that the container is running
        assertTrue(mysql8Container.isRunning());
    }

    @Test
    void testJdbcConnection() throws Exception {
        // Get the JDBC URL, username, and password from the container
        String jdbcUrl = mysql8Container.getJdbcUrl();
        String username = mysql8Container.getUsername();
        String password = mysql8Container.getPassword();

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
        assertEquals(mysql8Container.getJdbcUrl(), mysql8Container.getJdbcConnectionUrl());
    }
}