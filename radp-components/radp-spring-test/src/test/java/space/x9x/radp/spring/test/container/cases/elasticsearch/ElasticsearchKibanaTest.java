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

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.Map;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author IO x9x
 * @since 2025-05-25 14:36
 */
@Testcontainers
class ElasticsearchKibanaTest {

    private static final Network NETWORK = Network.newNetwork();

    @Container
    public GenericContainer<?> elasticsearch = new GenericContainer<>("docker.elastic.co/elasticsearch/elasticsearch:7.17.9")
            .withNetwork(NETWORK)
            .withNetworkAliases("elasticsearch")
            .withExposedPorts(9200)
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false");

    @Container
    public GenericContainer<?> kibana = new GenericContainer<>("docker.elastic.co/kibana/kibana:7.17.9")
            .withNetwork(NETWORK)
            .withNetworkAliases("kibana")
            .withExposedPorts(5601)
            .withEnv("ELASTICSEARCH_URL", "http://elasticsearch:9200")
            .withEnv("ELASTICSEARCH_HOSTS", "http://elasticsearch:9200")
            .dependsOn(elasticsearch);

    @Test
    void testElasticsearchKibana() throws Exception {
        // 创建 Elasticsearch 客户端
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", elasticsearch.getMappedPort(9200), "http")));

        // 索引数据
        IndexRequest indexRequest = new IndexRequest("logs")
                .source(Map.of("message", "Test log", "level", "INFO"));
        client.index(indexRequest, RequestOptions.DEFAULT);

        // 搜索数据
        SearchRequest searchRequest = new SearchRequest("logs");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("message", "Test"));
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        assertEquals(1, Objects.requireNonNull(response.getHits().getTotalHits()).value);

        client.close();
    }
}
