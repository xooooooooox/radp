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

package space.x9x.radp.spring.framework.json.fastjson;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.google.common.collect.Lists;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.ArrayUtil;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.framework.json.JSON;

/**
 * Implementation of the JSON interface using the Alibaba Fastjson library. This class
 * provides methods for serializing objects to JSON strings and deserializing JSON strings
 * back to objects using Fastjson.
 *
 * @author x9x
 * @since 2024-09-26 12:58
 */
public class Fastjson implements JSON {

	@Override
	public <T> String toJSONString(T object) {
		SerializeFilter[] filters = loadFilters();
		return ArrayUtil.isNotEmpty(filters) ? com.alibaba.fastjson.JSON.toJSONString(object, filters)
				: com.alibaba.fastjson.JSON.toJSONString(object);
	}

	@Override
	public <T> T parseObject(String text, Class<T> clazz) {
		return com.alibaba.fastjson.JSON.parseObject(text, clazz);
	}

	@Override
	public <T> List<T> parseList(String text, Class<T> clazz) {
		return com.alibaba.fastjson.JSON.parseArray(text, clazz);
	}

	private SerializeFilter[] loadFilters() {
		ExtensionLoader<FastjsonFilter> extensionLoader = ExtensionLoader.getExtensionLoader(FastjsonFilter.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		if (CollectionUtils.isEmpty(extensions)) {
			return new SerializeFilter[0];
		}
		List<SerializeFilter> filters = Lists.newArrayList();
		for (String extension : extensions) {
			FastjsonFilter filter = extensionLoader.getExtension(extension);
			filters.add(filter);
		}
		return filters.toArray(new SerializeFilter[0]);
	}

}
