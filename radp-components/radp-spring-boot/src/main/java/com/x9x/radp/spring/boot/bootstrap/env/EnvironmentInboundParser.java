package com.x9x.radp.spring.boot.bootstrap.env;

import com.x9x.radp.extension.SPI;
import org.springframework.core.env.Environment;

/**
 * Spring Boot 运行环境入站解析器
 *
 * @author x9x
 * @since 2024-09-28 21:46
 */
@SPI
public interface EnvironmentInboundParser {

    /**
     * 返回解析内容
     *
     * @param env 运行环境
     * @return 解析内容
     */
    String toString(Environment env);
}
