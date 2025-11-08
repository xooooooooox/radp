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

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.types.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines the structure for message queue (MQ) messages. This abstract class provides a
 * base for all event types in the system.
 *
 * @author x9x
 * @since 2024-11-28 14:43
 * @param <T> the type of data contained in the event
 */
@Data
public abstract class BaseEvent<T> {

	/**
	 * Builds an event message from the provided data.
	 * @param data the data to include in the event message
	 * @return a constructed event message containing the data
	 */
	public abstract EventMessage<T> buildEventMessage(T data);

	/**
	 * Returns the topic name for this event.
	 * @return the topic name as a String
	 */
	public abstract String topic();

	/**
	 * Inner class representing the structure of an event message.
	 *
	 * @param <T> the type of data contained in the event message
	 */
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EventMessage<T> {

		/**
		 * Unique identifier for the event message.
		 */
		private String id;

		/**
		 * Timestamp when the event message was created.
		 */
		private LocalDateTime timestamp;

		/**
		 * The actual data payload of the event message.
		 */
		private T data;

	}

}
