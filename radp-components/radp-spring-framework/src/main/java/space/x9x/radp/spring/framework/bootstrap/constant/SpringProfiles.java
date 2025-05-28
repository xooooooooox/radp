package space.x9x.radp.spring.framework.bootstrap.constant;

import lombok.experimental.UtilityClass;

/**
 * @author IO x9x
 * @since 2024-09-28 21:39
 */
@UtilityClass
public class SpringProfiles {
    /**
     * 本地环境
     */
    public static final String SPRING_PROFILE_LOCALHOST = "local";

    /**
     * 开发环境
     */
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

    /**
     * 生产环境
     */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    /**
     * 预发布环境
     */
    public static final String SPRING_PROFILE_STAGING = "staging";

    /**
     * 测试环境
     */
    public static final String SPRING_PROFILE_TEST = "test";

    /**
     * 演示环境
     */
    public static final String SPRING_PROFILE_DEMO = "demo";
}
