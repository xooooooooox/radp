package space.x9x.radp.spring.framework.bootstrap.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.env.Environment;
import space.x9x.radp.commons.lang.ArrayUtils;

/**
 * @author x9x
 * @since 2024-09-28 21:04
 */
@UtilityClass
public class SpringProfileUtils {
    /**
     * Gets the active profiles from the Spring environment.
     * If no active profiles are set, returns the default profiles.
     * If no default profiles are set, returns an empty array.
     *
     * @param env the Spring environment
     * @return an array of active profile names, or default profiles if no active profiles are set
     */
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
