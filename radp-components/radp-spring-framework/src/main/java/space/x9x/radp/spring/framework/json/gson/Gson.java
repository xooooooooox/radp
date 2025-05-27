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

package space.x9x.radp.spring.framework.json.gson;

import com.google.common.reflect.TypeToken;
import space.x9x.radp.spring.framework.json.JSON;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author IO x9x
 * @since 2024-09-26 13:25
 */
public class Gson implements JSON {

	private final com.google.gson.Gson gson = new com.google.gson.Gson();

	@Override
	public <T> String toJSONString(T object) {
		return gson.toJson(object);
	}

	@Override
	public <T> T parseObject(String text, Class<T> clazz) {
		return gson.fromJson(text, clazz);
	}

	@Override
	public <T> List<T> parseList(String text, Class<T> clazz) {
		Type listType = new TypeToken<List<T>>() {
		}.getType();

		return gson.fromJson(text, listType);
	}

}
