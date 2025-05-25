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

package space.x9x.radp.smoke.tests.test.container.mysql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * @author x9x
 * @since 2025-05-24 13:00
 */
@SpringBootTest
@Testcontainers
public class SpringBootMysqlContainerTest {

    @SuppressWarnings("resource")
    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("test")
            .withCreateContainerCmdModifier(cmd -> cmd.withPlatform("linux/arm64"));
//            .withLogConsumer(outputFrame -> System.out.println(outputFrame.getUtf8String())); // testContainer 调试技巧: 容器日志

    @Configuration
    static class TestConfig {
        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(MY_SQL_CONTAINER.getJdbcUrl());
            dataSource.setUsername(MY_SQL_CONTAINER.getUsername());
            dataSource.setPassword(MY_SQL_CONTAINER.getPassword());
            return dataSource;
        }
    }

    @Autowired
    private DataSource dataSource;

    @Test
    void testSpringDataSource() throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute("CREATE TABLE test(id INT)");
        }
    }
}
