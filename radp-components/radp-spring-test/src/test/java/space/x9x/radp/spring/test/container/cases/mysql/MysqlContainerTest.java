/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.test.container.cases.mysql;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author x9x
 * @since 2025-05-24 12:32
 */
@Testcontainers
class MysqlContainerTest {

    @SuppressWarnings("resource")
    @Container
    private final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("password");

    @Test
    void testCreateTable() throws SQLException {
        String sql = "CREATE TABLE users (id INT,name VARCHAR(255))";
        String jdbcUrl = mysql.getJdbcUrl();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, mysql.getUsername(), mysql.getPassword())) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            assertTrue(true);
        }
    }
}
