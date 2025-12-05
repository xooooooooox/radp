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

import java.util.List;

import space.x9x.radp.solutions.dict.core.DictItem;

/**
 * 字典数据提供者 SPI.
 *
 * @author RADP x9x
 * @since 2025-11-07 16:01
 */
public interface DictDataProvider {

	/**
	 * 获取某字典类型的所有条目.
	 * @param type 字典类型
	 * @return 字典项集合（可为空集合，非 null）
	 */
	List<DictItem> getItems(String type);

}
