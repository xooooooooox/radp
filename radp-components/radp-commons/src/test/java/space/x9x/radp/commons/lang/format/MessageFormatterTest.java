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

package space.x9x.radp.commons.lang.format;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link MessageFormatter}. This class contains unit tests to verify the
 * functionality of the MessageFormatter class, particularly its ability to format
 * messages by replacing placeholders with values.
 *
 * @author x9x
 * @since 2025-05-28 19:28
 */
class MessageFormatterTest {

	@Test
	void test_arrayFormat() {
		String messagePattern = "hello {}";
		Object[] params = { "world" };
		String message = MessageFormatter.arrayFormat(messagePattern, params).getMessage();
		assertThat(message).isEqualTo("hello world");
	}

}
