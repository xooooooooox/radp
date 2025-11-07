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

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;

import space.x9x.radp.solutions.dict.autoconfigure.DictProperties;
import space.x9x.radp.solutions.dict.core.DictItem;

/**
 * 基于配置文件的内存字典数据提供者.
 *
 * @author IO x9x
 * @since 2025-11-07 16:09
 */
@RequiredArgsConstructor
public class MemoryDictDataProvider implements DictDataProvider {

	/**
	 * Dictionary properties configuration containing type-to-items mappings.
	 */
	private final DictProperties properties;

	@Override
	public List<DictItem> getItems(String type) {
		List<DictItem> items = this.properties.getTypes().get(type);
		return items == null ? Collections.emptyList() : items;
	}

}
