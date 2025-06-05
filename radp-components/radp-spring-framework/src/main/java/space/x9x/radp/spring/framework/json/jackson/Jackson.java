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

package space.x9x.radp.spring.framework.json.jackson;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import space.x9x.radp.commons.json.JacksonUtils;
import space.x9x.radp.spring.framework.json.JSON;

/**
 * Jackson implementation of the JSON interface that provides JSON serialization and
 * deserialization functionality using the Jackson library.
 *
 * @author IO x9x
 * @since 2024-09-26 11:37
 */
public class Jackson implements JSON {

	@Override
	public <T> String toJSONString(T object) {
		return JacksonUtils.toJSONString(object);
	}

	@Override
	public <T> T parseObject(String text, Class<T> clazz) {
		return JacksonUtils.parseObject(text, clazz);
	}

	@Override
	public <T> List<T> parseList(String text, Class<T> clazz) {
		return JacksonUtils.parseList(text, clazz);
	}

	/**
	 * Returns the default ObjectMapper instance. This method provides access to the
	 * configured Jackson ObjectMapper for custom serialization/deserialization needs.
	 * @return the default ObjectMapper instance from JacksonUtils
	 */
	public ObjectMapper getObjectMapper() {
		return JacksonUtils.getDefaultObjectMapper();
	}

}
