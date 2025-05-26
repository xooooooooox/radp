/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
     * If no active profiles are set, return the default profiles.
     * If no default profiles are set, return an empty array.
     *
     * @param env the Spring environment
     * @return an array of active profile names or default profiles if no active profiles are set
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
