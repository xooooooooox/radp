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

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;

import space.x9x.radp.solutions.dict.core.DictItem;
import space.x9x.radp.solutions.dict.core.DictService;
import space.x9x.radp.solutions.dict.core.provider.DictDataProvider;

/**
 * 默认的字典服务实现.
 *
 * @author x9x
 * @since 2025-11-07 15:55
 */
@RequiredArgsConstructor
public class DefaultDictService implements DictService {

	private final DictDataProvider provider;

	@Override
	public String getLabel(String type, String value) {
		if (value == null) {
			return null;
		}
		for (DictItem item : this.getItems(type)) {
			if (value.equals(item.getValue())) {
				return item.getLabel();
			}
		}
		return value;
	}

	@Override
	public String getValue(String type, String label) {
		if (label == null) {
			return null;
		}
		for (DictItem item : this.getItems(type)) {
			if (label.equals(item.getLabel())) {
				return item.getValue();
			}
		}
		return label;
	}

	@Override
	public List<DictItem> getItems(String type) {
		List<DictItem> items = this.provider.getItems(type);
		return items == null ? Collections.emptyList() : items;
	}

}
