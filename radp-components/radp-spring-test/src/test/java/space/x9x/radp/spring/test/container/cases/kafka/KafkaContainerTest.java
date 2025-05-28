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

package space.x9x.radp.spring.test.container.cases.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 假设你正在开发一个订单处理系统，订单通过 Kafka 消息队列传递给下游服务。你需要测试生产者和消费者的正确性。
 *
 * @author IO x9x
 * @see <a href="https://java.testcontainers.org/modules/kafka/">Kafka Module for Java</a>
 * @since 2025-05-25 12:41
 */
@Testcontainers
@Slf4j
class KafkaContainerTest {

	/*
	 * KafkaContainer 类默认只信任 官方 apache/kafka 镜像。 把 confluentinc/cp-kafka:7.4.0
	 * 直接塞给它时，Testcontainers 会做一次“镜像兼容性校验”，校验失败就抛出这条 java.lang.IllegalStateException:
	 * Failed to verify that image 'confluentinc/cp-kafka:7.4.0' is a compatible
	 * substitute for 'apache/kafka'. 从 Testcontainers 1.20+ 开始，官方把 Confluent
	 * 镜像独立成了一个专用包装类 org.testcontainers.kafka.ConfluentKafkaContainer，而且只对 7.4.0 及以上
	 * 版本做了适配
	 */
	@Container
	private final ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka:7.5.0")); // 启动一个 Kafka 示例, 默认包含
																	// Zookeeper

	@Test
	void testProducerAndConsumer() throws Exception {
		String bootstrapServers = kafka.getBootstrapServers();
		String topic = "orders";

		// 创建主题
		Properties adminProps = new Properties();
		adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		try (AdminClient adminClient = AdminClient.create(adminProps)) {
			NewTopic newTopic = new NewTopic(topic, 1, (short) 1); // 创建一个分区，一个副本的主题
			adminClient.createTopics(Collections.singleton(newTopic)).all().get();
			log.info("Topic {} created successfully", topic);
		}

		// 配置生产者
		Properties producerProperties = new Properties();
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// 发送消息
		KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties);
		producer.send(new ProducerRecord<>(topic, "order1", "Order #1")).get();
		producer.close();

		// 配置消费者
		Properties consumerProperties = new Properties();
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
		consumer.subscribe(Collections.singletonList(topic));

		// 消费消息
		ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
		consumer.close();

		assertThat(records.count()).isEqualTo(1);
	}

}
