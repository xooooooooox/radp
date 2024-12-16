package com.x9x.radp.spring.framework.bootstrap.utils;

import com.x9x.radp.commons.lang.ArrayUtils;
import lombok.experimental.UtilityClass;
import org.springframework.core.env.Environment;

/**
 * @author x9x
 * @since 2024-09-28 21:04
 */
@UtilityClass
public class SpringProfileUtils {
    public static String[] getActiveProfiles(Environment env) {
        String[] activeProfiles = env.getActiveProfiles();
        if (ArrayUtils.isEmpty(activeProfiles)) {
            String[] defaultProfiles = env.getDefaultProfiles();
            if (ArrayUtils.isEmpty(defaultProfiles)) {
                return new String[0];
            }
            return defaultProfiles;
        }
        return activeProfiles;
    }
}
