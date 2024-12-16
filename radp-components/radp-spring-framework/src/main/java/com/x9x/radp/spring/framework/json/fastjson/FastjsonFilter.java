package com.x9x.radp.spring.framework.json.fastjson;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.x9x.radp.extension.SPI;

/**
 * @author x9x
 * @since 2024-09-26 13:04
 */
@SPI
public interface FastjsonFilter extends SerializeFilter {
}
