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

package space.x9x.radp.spring.framework.test;

import org.junit.jupiter.api.Test;

import space.x9x.radp.commons.lang.format.MessageFormatter;
import space.x9x.radp.spring.framework.type.exception.asserts.ServerAssert;

import static org.assertj.core.api.Assertions.assertThat;

class ServerAssertTests {

	@Test
	void test() {
		String messagePattern = "hello {}";
		Object[] params = { "world" };
		String message = MessageFormatter.arrayFormat(messagePattern, params).getMessage();
		assertThat(message).isEqualTo("hello world");
		try {
			ServerAssert.notNull(null, "TEST_0001", "world");
		}
		catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("hello world");
		}
	}

}
