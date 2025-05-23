#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.types.common;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import lombok.experimental.UtilityClass;

/**
 * RedisKey 常量
 * 格式: 引用名:模块名(或功能点):前缀
 *
 * @author x9x
 * @since 2024-10-20 18:45
 */
@UtilityClass
public class RedisKeyConstants {
    // TODO 2024/10/20: 这只是个示例
    public static final String SCAFFOLD_DEMO_REDIS_KEY = "scaffold:lite:demo";


    public static <T> String buildRedisKey(String keyPrefix, T key) {
        return StringUtils.join(keyPrefix, Strings.COLON, String.valueOf(key));
    }
}
