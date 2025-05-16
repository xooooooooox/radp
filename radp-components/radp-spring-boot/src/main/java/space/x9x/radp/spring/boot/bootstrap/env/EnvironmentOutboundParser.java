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
     * Converts the Spring Environment to a string representation.
     * This method is used to transform environment properties into a string format
     * suitable for outbound communication or logging.
     *
     * @param env the Spring Environment to convert
     * @return a string representation of the environment
     */
    String toString(Environment env);
}
