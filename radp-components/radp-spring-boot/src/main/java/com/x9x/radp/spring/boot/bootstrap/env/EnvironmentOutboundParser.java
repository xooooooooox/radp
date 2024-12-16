package com.x9x.radp.spring.boot.bootstrap.env;

import com.x9x.radp.extension.SPI;
import org.springframework.core.env.Environment;

/**
 * Spring Boot 运行环境出站解析器
 * @author x9x
 * @since 2024-09-28 21:47
 */
@SPI
public interface EnvironmentOutboundParser {

    String toString(Environment env);
}
