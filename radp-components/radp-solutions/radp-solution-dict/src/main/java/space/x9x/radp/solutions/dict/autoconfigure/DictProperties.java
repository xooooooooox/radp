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

package space.x9x.radp.solutions.dict.autoconfigure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import space.x9x.radp.solutions.dict.core.DictItem;
import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * 字典配置属性.
 * <ul>
 * <li>provider: 使用的提供者（memory|db），默认 memory</li>
 * <li>types: 内存模式下，定义各字典类型的条目</li>
 * </ul>
 *
 * @author x9x
 * @since 2025-11-07 15:53
 */
@Data
@ConfigurationProperties(prefix = DictProperties.PREFIX)
public class DictProperties {

	/**
	 * Configuration properties prefix for dictionary settings.
	 */
	public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "dict";

	/**
	 * 提供者类型：memory 或 db.
	 */
	private String provider = "memory";

	/**
	 * 在 memory 模式下，配置的字典类型及其条目.
	 */
	private Map<String, List<DictItem>> types = new HashMap<>();

}
