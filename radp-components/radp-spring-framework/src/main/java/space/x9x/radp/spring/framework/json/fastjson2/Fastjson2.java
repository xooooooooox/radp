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

package space.x9x.radp.spring.framework.json.fastjson2;

import com.alibaba.fastjson2.filter.Filter;
import com.google.common.collect.Lists;
import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.ArrayUtils;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.framework.json.JSON;

import java.util.List;
import java.util.Set;

/**
 * @author x9x
 * @since 2024-09-26 13:21
 */
public class Fastjson2 implements JSON {
    @Override
    public <T> String toJSONString(T object) {
        Filter[] filters = loadFilters();
        return ArrayUtils.isNotEmpty(filters) ?
                com.alibaba.fastjson2.JSON.toJSONString(object, filters) :
                com.alibaba.fastjson2.JSON.toJSONString(object);
    }

    @Override
    public <T> T parseObject(String text, Class<T> clazz) {
        return com.alibaba.fastjson2.JSON.parseObject(text, clazz);
    }

    @Override
    public <T> List<T> parseList(String text, Class<T> clazz) {
        return com.alibaba.fastjson2.JSON.parseArray(text, clazz);
    }

    private Filter[] loadFilters() {
        ExtensionLoader<Fastjson2Filter> extensionLoader = ExtensionLoader.getExtensionLoader(Fastjson2Filter.class);
        Set<String> extensions = extensionLoader.getSupportedExtensions();
        if (CollectionUtils.isEmpty(extensions)) {
            return null;
        }
        List<Filter> filters = Lists.newArrayList();
        for (String extension : extensions) {
            Fastjson2Filter filter = extensionLoader.getExtension(extension);
            filters.add(filter);
        }

        return filters.toArray(new Filter[0]);
    }
}
