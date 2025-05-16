package space.x9x.radp.spring.boot.bootstrap.env;

import org.springframework.core.env.Environment;
import space.x9x.radp.extension.SPI;

/**
 * Spring Boot 运行环境出站解析器
 * @author x9x
 * @since 2024-09-28 21:47
 */
@SPI
public interface EnvironmentOutboundParser {

    /**
     * Converts environment information to a string representation.
     * This method is responsible for parsing the Spring Environment and
     * generating a string representation of relevant outbound information,
     * such as server addresses, ports, or other external connection details.
     *
     * @param env the Spring Environment to parse
     * @return a string representation of the environment's outbound information
     */
    String toString(Environment env);
}
