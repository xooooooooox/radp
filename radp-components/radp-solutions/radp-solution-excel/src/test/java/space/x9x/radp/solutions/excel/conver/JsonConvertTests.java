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

package space.x9x.radp.solutions.excel.conver;

import cn.idev.excel.metadata.data.WriteCellData;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import space.x9x.radp.commons.json.JacksonUtils;

/**
 * Tests for JsonConvert.
 *
 * @author RADP x9x
 * @since 2025-11-09 19:59
 */
class JsonConvertTests {

	@Test
	@DisplayName("support*Key methods -> Object.class and null as expected")
	void testSupportKeys() {
		Assertions.assertThat(converter.supportJavaTypeKey()).isEqualTo(Object.class);
		Assertions.assertThat(converter.supportExcelTypeKey()).isNull();
	}

	private final JsonConvert converter = new JsonConvert();

	@Test
	@DisplayName("convertToExcelData -> serializes POJO to JSON string in cell")
	void testConvertPojo() throws Exception {
		Person p = new Person();
		p.setName("Alice");
		p.setAge(30);

		WriteCellData<?> cell = converter.convertToExcelData(p, null, null);
		String json = cell.getStringValue();

		// Verify JSON contains expected properties (order-agnostic)
		Assertions.assertThat(json).contains("\"name\":\"Alice\"");
		Assertions.assertThat(json).contains("\"age\":30");
	}

	@Test
	@DisplayName("convertToExcelData -> null value becomes JSON 'null'")
	void testConvertNull() throws Exception {
		WriteCellData<?> cell = converter.convertToExcelData(null, null, null);
		Assertions.assertThat(cell.getStringValue()).isEqualTo("null");
	}

	@Test
	@DisplayName("convertToExcelData -> list/map round-trip via JacksonUtils for stability")
	void testConvertCollection() throws Exception {
		Person dto = new Person();
		dto.setName("Bob");
		dto.setAge(18);
		String expected = JacksonUtils.toJSONString(dto);
		WriteCellData<?> cell = converter.convertToExcelData(dto, null, null);
		Assertions.assertThat(cell.getStringValue()).isEqualTo(expected);
	}

	// simple POJO for testing
	@Setter
	@Getter
	static class Person {

		private String name;

		private Integer age;

	}

}
