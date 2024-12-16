package com.x9x.radp.spring.framework.json.fastjson2;

import com.alibaba.fastjson2.filter.Filter;
import com.google.common.collect.Lists;
import com.x9x.radp.commons.collections.CollectionUtils;
import com.x9x.radp.commons.lang.ArrayUtils;
import com.x9x.radp.extension.ExtensionLoader;
import com.x9x.radp.spring.framework.json.JSON;

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
