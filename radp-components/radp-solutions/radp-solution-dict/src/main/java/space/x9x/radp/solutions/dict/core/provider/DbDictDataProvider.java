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
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.ObjectProvider;

import space.x9x.radp.solutions.dict.core.DictItem;

/**
 * 基于数据库的字典数据提供者. 需要应用侧提供 {@link DictDataQuery} 实现。
 *
 * @author RADP x9x
 * @since 2025-11-07 16:04
 */
@RequiredArgsConstructor
@Slf4j
public class DbDictDataProvider implements DictDataProvider {

	/**
	 * Provider for obtaining the {@link DictDataQuery} instance. Used to dynamically
	 * access the dictionary data query implementation.
	 */
	private final ObjectProvider<DictDataQuery> queryProvider;

	@Override
	public List<DictItem> getItems(String type) {
		DictDataQuery query = this.queryProvider.getIfAvailable();
		if (query == null) {
			log.warn("[radp-dict] DB provider enabled but no DictDataQuery bean found. Return empty for type={}", type);
			return Collections.emptyList();
		}
		List<DictItem> list = query.getItemsByType(type);
		return (list != null) ? list : Collections.emptyList();
	}

}
