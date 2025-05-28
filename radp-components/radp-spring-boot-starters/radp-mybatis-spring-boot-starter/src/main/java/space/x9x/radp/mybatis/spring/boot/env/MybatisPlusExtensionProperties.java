package space.x9x.radp.mybatis.spring.boot.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * Configuration properties for MyBatis-Plus extensions. This class defines settings for
 * MyBatis-Plus extensions, particularly for automatic field filling functionality like
 * creation and modification timestamps.
 *
 * @author IO x9x
 * @since 2024-09-30 13:25
 */
@ConfigurationProperties(prefix = MybatisPlusExtensionProperties.PREFIX)
@Data
public class MybatisPlusExtensionProperties {

    /**
     * Configuration prefix for MyBatis-Plus extension properties.
     * This constant is used as part of the property path in application configuration.
     */
    public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "mybatis-plus.extension";

    /**
     * Property path for enabling automatic field filling.
     * This constant represents the full property path to enable/disable automatic field filling.
     */
    public static final String AUTO_FILL_ENABLED = PREFIX + ".auto-fill.enabled";

    private final AutoFill autoFill = new AutoFill();

    /**
     * Configuration properties for automatic field filling functionality.
     * This inner class contains settings related to automatically filling
     * creation and modification timestamp fields in entity objects.
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
