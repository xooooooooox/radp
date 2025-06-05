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

package space.x9x.radp.spring.test.container.cases.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author IO x9x
 * @since 2025-05-23 15:44
 */
@Testcontainers // 管理容器生命周期
class PostgresContainerTest {

	@SuppressWarnings("resource")
	@Container // TestContainers 会自动启动和停止它
	private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15").withDatabaseName("test")
		.withUsername("postgres")
		.withPassword("password");

	@Test
	void testSimpleQuery() throws SQLException {
		// 链接数据库并执行查询
		Connection connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(),
				postgres.getPassword());
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT 1");
		resultSet.next();
		int result = resultSet.getInt(1);
		assertThat(result).isEqualTo(1);
	}

}
