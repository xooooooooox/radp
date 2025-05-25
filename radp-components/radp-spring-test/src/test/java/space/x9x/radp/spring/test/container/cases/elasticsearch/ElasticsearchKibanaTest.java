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

package space.x9x.radp.spring.test.container.cases.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author x9x
 * @since 2025-05-25 14:36
 */
@Testcontainers
class ElasticsearchKibanaTest {

    private static final Network NETWORK = Network.newNetwork();

    @Container
    public final GenericContainer<?> elasticsearch = new GenericContainer<>("docker.elastic.co/elasticsearch/elasticsearch:7.17.9")
            .withNetwork(NETWORK)
            .withNetworkAliases("elasticsearch")
            .withExposedPorts(9200)
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false")
            .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
            .waitingFor(Wait.forHttp("/_cluster/health?wait_for_status=yellow")
                    .forPort(9200)
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(2)));

    @Container
    public final GenericContainer<?> kibana = new GenericContainer<>("docker.elastic.co/kibana/kibana:7.17.9")
            .withNetwork(NETWORK)
            .withNetworkAliases("kibana")
            .withExposedPorts(5601)
            .withEnv("ELASTICSEARCH_HOSTS", "http://elasticsearch:9200")
            .dependsOn(elasticsearch)
            .waitingFor(Wait.forHttp("/api/status")
                    .forPort(5601)
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(2)));

    @Test
    void testElasticsearchKibana() throws Exception {
        // 创建 Elasticsearch 客户端
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", elasticsearch.getMappedPort(9200), "http"))
                .build();

        // 创建传输层
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // 创建 API 客户端
        ElasticsearchClient client = new ElasticsearchClient(transport);

        // 索引数据
        Map<String, Object> document = new HashMap<>();
        document.put("message", "Test log");
        document.put("level", "INFO");
        client.index(i -> i
                .index("logs")
                .document(document));

        // 等待索引刷新
        client.indices().refresh();

        // 搜索数据
        SearchResponse<JsonData> response = client.search(s -> s
                .index("logs")
                .query(q -> q
                    .match(m -> m
                        .field("message")
                        .query("Test")
                    )
                ),
                JsonData.class);

        Assertions.assertNotNull(response.hits().total());
        assertEquals(1, response.hits().total().value());

        // 关闭客户端
        transport.close();
    }
}
