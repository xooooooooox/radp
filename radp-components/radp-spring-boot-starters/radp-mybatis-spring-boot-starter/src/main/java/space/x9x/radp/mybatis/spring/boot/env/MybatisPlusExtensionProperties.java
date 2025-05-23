package space.x9x.radp.mybatis.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * MybatisPlus 扩展自动装配
 *
 * @author x9x
 * @since 2024-09-30 13:25
 */
@ConfigurationProperties(prefix = MybatisPlusExtensionProperties.PREFIX)
@Data
public class MybatisPlusExtensionProperties {

    /**
     * Configuration prefix for MyBatis-Plus extension properties.
     */
    public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "mybatis-plus.extension";
    /**
     * Configuration property key for enabling automatic field filling.
     */
    public static final String AUTO_FILL_ENABLED = PREFIX + ".auto-fill.enabled";

    private final AutoFill autoFill = new AutoFill();

    /**
     * Configuration properties for automatic field filling feature.
     */
    @Data
    public static class AutoFill {
        /**
         * 是否开启自动填充创建时间和更新时间字段,默认步开启
         */
        private boolean enabled = false;
        /**
         * 创建时间字段名(Java Property name not Database column name)
         */
        private String createdDataFieldName = "createdDate";
        /**
         * 更新时间字段名(Java Property name not Database column name)
         */
        private String lastModifiedDateFieldName = "lastModifiedDate";
    }
}
