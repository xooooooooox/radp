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

package space.x9x.radp.solutions.dict.core.provider;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import space.x9x.radp.solutions.dict.autoconfigure.DictProperties;
import space.x9x.radp.solutions.dict.core.DictItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryDictDataProviderTest {

	@Test
	@DisplayName("should return items from properties map when type exists")
	void return_items_when_type_exists() {
		DictProperties properties = new DictProperties();
		Map<String, List<DictItem>> types = new HashMap<>();
		types.put("sex", Arrays.asList(new DictItem("男", "1"), new DictItem("女", "2")));
		properties.setTypes(types);

		MemoryDictDataProvider provider = new MemoryDictDataProvider(properties);

		List<DictItem> result = provider.getItems("sex");
		assertEquals(2, result.size());
		assertEquals("男", result.get(0).getLabel());
		assertEquals("1", result.get(0).getValue());
		assertEquals("女", result.get(1).getLabel());
		assertEquals("2", result.get(1).getValue());
	}

	@Test
	@DisplayName("should return empty list for missing type or empty map")
	void return_empty_when_missing() {
		DictProperties properties1 = new DictProperties();
		properties1.setTypes(Collections.emptyMap());
		MemoryDictDataProvider provider1 = new MemoryDictDataProvider(properties1);
		assertTrue(provider1.getItems("unknown").isEmpty());

		DictProperties properties2 = new DictProperties();
		// default constructor already has empty map
		MemoryDictDataProvider provider2 = new MemoryDictDataProvider(properties2);
		assertTrue(provider2.getItems("unknown").isEmpty());
	}

}
