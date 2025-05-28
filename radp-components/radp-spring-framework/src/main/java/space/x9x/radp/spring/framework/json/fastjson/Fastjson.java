package space.x9x.radp.spring.framework.json.fastjson;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.google.common.collect.Lists;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.ArrayUtils;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.framework.json.JSON;

/**
 * @author IO x9x
 * @since 2024-09-26 12:58
 */
public class Fastjson implements JSON {
    @Override
    public <T> String toJSONString(T object) {
        SerializeFilter[] filters = loadFilters();
        return ArrayUtils.isNotEmpty(filters) ?
                com.alibaba.fastjson.JSON.toJSONString(object, filters) :
                com.alibaba.fastjson.JSON.toJSONString(object);
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
            return null;
        }
        List<SerializeFilter> filters = Lists.newArrayList();
        for (String extension : extensions) {
            FastjsonFilter filter = extensionLoader.getExtension(extension);
            filters.add(filter);
        }
        return filters.toArray(new SerializeFilter[0]);
    }
}
