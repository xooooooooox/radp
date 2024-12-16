package com.x9x.radp.spring.framework.json.support;

import com.x9x.radp.extension.ExtensionLoader;
import com.x9x.radp.spring.framework.json.JSON;

/**
 * @author x9x
 * @since 2024-09-26 12:53
 */
public class JSONHelper {
    /**
     * 获取默认 JSON 实现
     *
     * @return JSON 实例
     */
    public static JSON json() {
        return ExtensionLoader.getExtensionLoader(JSON.class).getDefaultExtension();
    }

    /**
     * 获取指定 JSON 实现
     *
     * @param spi 扩展点名称
     * @return JSON 实例
     */
    public static JSON json(String spi) {
        return ExtensionLoader.getExtensionLoader(JSON.class).getExtension(spi);
    }
}
