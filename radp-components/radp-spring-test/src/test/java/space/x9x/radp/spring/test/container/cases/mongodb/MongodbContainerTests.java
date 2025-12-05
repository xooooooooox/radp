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

package space.x9x.radp.spring.test.container.cases.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author RADP x9x
 * @since 2025-05-25 16:42
 */
@Testcontainers
class MongodbContainerTests {

	@Container
	private final MongoDBContainer mongodb = new MongoDBContainer("mongo:4.4.10");

	@Test
	void testMongoDbConnection() {
		// Get the connection string from the container
		String connectionString = mongodb.getConnectionString();

		// Create a MongoDB client
		try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			// Get the database
			MongoDatabase database = mongoClient.getDatabase("test");

			// Create a collection
			database.createCollection("users");

			// Get the collection
			MongoCollection<Document> collection = database.getCollection("users");

			// Create a document
			Document document = new Document("name", "John Doe").append("age", 30)
				.append("email", "john.doe@example.com");

			// Insert the document
			collection.insertOne(document);

			// Find the document
			Document foundDocument = collection.find(new Document("name", "John Doe")).first();

			// Verify the document was inserted correctly
			assertThat(foundDocument).isNotNull();
			assertThat(foundDocument.getString("name")).isEqualTo("John Doe");
			assertThat(foundDocument.getInteger("age")).isEqualTo(30);
			assertThat(foundDocument.getString("email")).isEqualTo("john.doe@example.com");
		}
	}

}
