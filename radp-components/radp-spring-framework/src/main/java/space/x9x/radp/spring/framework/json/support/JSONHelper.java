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

package space.x9x.radp.spring.framework.json.support;

import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.framework.json.JSON;

/**
 * @author IO x9x
 * @since 2024-09-26 12:53
 */
public class JSONHelper {

	/**
	 * 获取默认 JSON 实现
	 * @return JSON 实例
	 */
	public static JSON json() {
		return ExtensionLoader.getExtensionLoader(JSON.class).getDefaultExtension();
	}

	/**
	 * 获取指定 JSON 实现
	 * @param spi 扩展点名称
	 * @return JSON 实例
	 */
	public static JSON json(String spi) {
		return ExtensionLoader.getExtensionLoader(JSON.class).getExtension(spi);
	}

}
