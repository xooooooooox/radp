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

package space.x9x.radp.commons.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author RADP x9x
 * @since 2025-11-23 12:40
 */
class MessageFormatUtilsTest {

	@DisplayName("场景:无异常不带占位符")
	@Test
	void testFormat() {
		String msg = "hello";
		String result;
		result = MessageFormatUtils.format(msg);
		System.out.println(result);
		assertThat(result).isEqualTo(msg);
		result = MessageFormatUtils.format(msg, "world");
		System.out.println(result);
		assertThat(result).isEqualTo(msg);
	}

	@DisplayName("场景:无异常带占位符")
	@Test
	void testFormat_with_placeholder() {
		String msg = "hello {}";
		String result;
		result = MessageFormatUtils.format(msg);
		System.out.println(result);
		assertThat(result).isEqualTo(msg);
		result = MessageFormatUtils.format(msg, "world");
		System.out.println(result);
		assertThat(result).isEqualTo("hello world");
		result = MessageFormatUtils.format(msg, "world", "a");
		System.out.println(result);
		assertThat(result).isEqualTo("hello world");
		result = MessageFormatUtils.format(msg, "world", "a", "b");
		System.out.println(result);
		assertThat(result).isEqualTo("hello world");
	}

}
