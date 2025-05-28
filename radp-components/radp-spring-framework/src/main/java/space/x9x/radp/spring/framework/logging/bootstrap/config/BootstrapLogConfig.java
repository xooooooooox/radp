package space.x9x.radp.spring.framework.logging.bootstrap.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 引导日志配置
 *
 * @author IO x9x
 * @since 2024-09-28 20:48
 */
@EqualsAndHashCode
@ToString
@Data
public class BootstrapLogConfig {
    /**
     * 是否保存到 MDC
     */
    private boolean enabledMdc = true;
}
