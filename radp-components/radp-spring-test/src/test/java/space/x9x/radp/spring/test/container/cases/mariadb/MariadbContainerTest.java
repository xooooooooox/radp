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

package space.x9x.radp.spring.test.container.cases.mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author IO x9x
 * @since 2025-05-24 12:32
 */
@Testcontainers
class MariadbContainerTest {

	@SuppressWarnings("resource")
	@Container
	private final MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:10.11").withDatabaseName("test")
		.withUsername("root")
		.withPassword("password");

	@Test
	void testCreateTable() throws SQLException {
		String sql = "CREATE TABLE users (id INT, name VARCHAR(255))";
		String jdbcUrl = mariadb.getJdbcUrl();
		try (Connection connection = DriverManager.getConnection(jdbcUrl, mariadb.getUsername(),
				mariadb.getPassword())) {
			Statement statement = connection.createStatement();
			statement.execute(sql);
			assertThat(true).isTrue();
		}
	}

}
