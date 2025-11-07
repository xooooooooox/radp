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

package space.x9x.radp.solutions.dict.core;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典服务抽象.
 *
 * @author IO x9x
 * @since 2025-11-07 15:51
 */
public interface DictService {

	/**
	 * 根据字典类型与值，获取对应的标签.
	 * @param type 字典类型
	 * @param value 字典值
	 * @return 标签（找不到返回原值）
	 */
	String getLabel(String type, String value);

	/**
	 * 根据字典类型与标签，获取对应的值.
	 * @param type 字典类型
	 * @param label 标签
	 * @return 值（找不到返回原标签）
	 */
	String getValue(String type, String label);

	/**
	 * 获取该字典类型的所有条目.
	 * @param type 字典类型
	 * @return 字典项列表
	 */
	List<DictItem> getItems(String type);

	/**
	 * 获取该字典类型的所有标签.
	 * @param type 字典类型
	 * @return 标签列表
	 */
	default List<String> getLabels(String type) {
		return getItems(type).stream().map(DictItem::getLabel).collect(Collectors.toList());
	}

}
