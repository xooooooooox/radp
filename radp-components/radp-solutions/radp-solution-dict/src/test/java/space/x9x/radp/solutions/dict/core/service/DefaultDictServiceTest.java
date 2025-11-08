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

package space.x9x.radp.solutions.dict.core.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import space.x9x.radp.solutions.dict.core.DictItem;
import space.x9x.radp.solutions.dict.core.provider.DictDataProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultDictServiceTest {

	private static final String TYPE = "sex";

	private DefaultDictService newServiceReturning(List<DictItem> items) {
		DictDataProvider provider = new DictDataProvider() {
			@Override
			public List<DictItem> getItems(String type) {
				return items;
			}
		};
		return new DefaultDictService(provider);
	}

	@Test
	@DisplayName("getLabel should return mapped label when value matches")
	void getLabel_hit() {
		List<DictItem> items = Arrays.asList(new DictItem("男", "1"), new DictItem("女", "2"));
		DefaultDictService service = newServiceReturning(items);
		assertEquals("男", service.getLabel(TYPE, "1"));
		assertEquals("女", service.getLabel(TYPE, "2"));
	}

	@Test
	@DisplayName("getLabel should return original value when not found")
	void getLabel_miss() {
		List<DictItem> items = Collections.singletonList(new DictItem("男", "1"));
		DefaultDictService service = newServiceReturning(items);
		assertEquals("3", service.getLabel(TYPE, "3"));
	}

	@Test
	@DisplayName("getLabel should return null when input value is null")
	void getLabel_nullInput() {
		DefaultDictService service = newServiceReturning(Collections.emptyList());
		assertNull(service.getLabel(TYPE, null));
	}

	@Test
	@DisplayName("getValue should return mapped value when label matches")
	void getValue_hit() {
		List<DictItem> items = Arrays.asList(new DictItem("男", "1"), new DictItem("女", "2"));
		DefaultDictService service = newServiceReturning(items);
		assertEquals("1", service.getValue(TYPE, "男"));
		assertEquals("2", service.getValue(TYPE, "女"));
	}

	@Test
	@DisplayName("getValue should return original label when not found")
	void getValue_miss() {
		List<DictItem> items = Collections.singletonList(new DictItem("男", "1"));
		DefaultDictService service = newServiceReturning(items);
		assertEquals("未知", service.getValue(TYPE, "未知"));
	}

	@Test
	@DisplayName("getValue should return null when input label is null")
	void getValue_nullInput() {
		DefaultDictService service = newServiceReturning(Collections.emptyList());
		assertNull(service.getValue(TYPE, null));
	}

	@Test
	@DisplayName("getItems should return empty list when provider returns null")
	void getItems_providerNull() {
		DefaultDictService service = newServiceReturning(null);
		List<DictItem> result = service.getItems(TYPE);
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("getItems should pass through provider result when not null")
	void getItems_providerNonNull() {
		List<DictItem> items = Collections.singletonList(new DictItem("男", "1"));
		DefaultDictService service = newServiceReturning(items);
		List<DictItem> result = service.getItems(TYPE);
		assertEquals(1, result.size());
		assertEquals("男", result.get(0).getLabel());
		assertEquals("1", result.get(0).getValue());
	}

}
