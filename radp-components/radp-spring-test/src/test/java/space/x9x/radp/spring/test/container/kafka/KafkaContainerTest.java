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

package space.x9x.radp.spring.test.container.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link KafkaContainer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class KafkaContainerTest {

    private KafkaContainer kafkaContainer;
    private String testTopic;
    private String testMessage;

    @BeforeEach
    void setUp() {
        // Create and start the Kafka container
        kafkaContainer = new KafkaContainer();
        kafkaContainer.start();

        // Generate unique topic and message for each test
        testTopic = "test-topic-" + UUID.randomUUID().toString().substring(0, 8);
        testMessage = "test-message-" + UUID.randomUUID().toString();
    }

    @AfterEach
    void tearDown() {
        // Stop the container
        if (kafkaContainer != null && kafkaContainer.isRunning()) {
            kafkaContainer.stop();
        }
    }

    @Test
    void testContainerIsRunning() {
        // Verify that the container is running
        assertTrue(kafkaContainer.isRunning());
    }

    @Test
    void testKafkaConnection() throws ExecutionException, InterruptedException {
        // Create a topic
        try (AdminClient adminClient = createAdminClient()) {
            NewTopic newTopic = new NewTopic(testTopic, 1, (short) 1);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
        }

        // Produce a message
        try (KafkaProducer<String, String> producer = createProducer()) {
            producer.send(new ProducerRecord<>(testTopic, "key", testMessage)).get();
        }

        // Consume the message
        try (KafkaConsumer<String, String> consumer = createConsumer()) {
            consumer.subscribe(Collections.singleton(testTopic));

            // Poll for records
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));

            // Verify that we received the message
            assertFalse(records.isEmpty());

            // Get the first record
            ConsumerRecord<String, String> record = records.iterator().next();

            // Verify the record content
            assertEquals("key", record.key());
            assertEquals(testMessage, record.value());
        }
    }

    @Test
    void testGetKafkaConnectionString() {
        // Verify that the connection string is not null or empty
        String connectionString = kafkaContainer.getKafkaConnectionString();
        assertNotNull(connectionString);
        assertFalse(connectionString.isEmpty());

        // Verify that the connection string is the same as getBootstrapServers
        assertEquals(kafkaContainer.getBootstrapServers(), connectionString);
    }

    private AdminClient createAdminClient() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        return AdminClient.create(props);
    }

    private KafkaProducer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private KafkaConsumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + UUID.randomUUID().toString());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }
}