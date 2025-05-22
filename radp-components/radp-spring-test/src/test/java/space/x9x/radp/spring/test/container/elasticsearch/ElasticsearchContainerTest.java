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

package space.x9x.radp.spring.test.container.elasticsearch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link ElasticsearchContainer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class ElasticsearchContainerTest {

    private ElasticsearchContainer elasticsearchContainer;

    @BeforeEach
    void setUp() {
        // Create and start the Elasticsearch container
        elasticsearchContainer = new ElasticsearchContainer();
        elasticsearchContainer.start();
    }

    @AfterEach
    void tearDown() {
        // Stop the container
        if (elasticsearchContainer != null && elasticsearchContainer.isRunning()) {
            elasticsearchContainer.stop();
        }
    }

    @Test
    void testContainerIsRunning() {
        // Verify that the container is running
        assertTrue(elasticsearchContainer.isRunning());
    }

    @Test
    void testHttpConnection() throws Exception {
        // Get the HTTP URL from the container
        String httpUrl = elasticsearchContainer.getHttpHostAddress();

        // Create a connection to the Elasticsearch server
        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Verify that we can connect to the server
        int responseCode = connection.getResponseCode();
        assertTrue(responseCode >= 200 && responseCode < 300, "Expected successful HTTP response");
    }

    @Test
    void testGetHttpHostAddress() {
        // Verify that getHttpHostAddress returns a non-empty string
        String httpUrl = elasticsearchContainer.getHttpHostAddress();
        assertTrue(httpUrl != null && !httpUrl.isEmpty());

        // Verify that the URL contains the host and port
        assertTrue(httpUrl.contains(elasticsearchContainer.getHost()));
        assertTrue(httpUrl.contains(String.valueOf(
                elasticsearchContainer.getMappedPort(ElasticsearchContainer.ELASTICSEARCH_PORT))));
    }
}