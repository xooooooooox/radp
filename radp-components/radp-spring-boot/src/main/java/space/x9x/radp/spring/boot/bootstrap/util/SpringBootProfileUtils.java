package space.x9x.radp.spring.boot.bootstrap.util;

import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProfiles;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing Spring Boot application profiles.
 * This class provides methods to configure default profiles for Spring Boot applications,
 * making it easier to set up development environments consistently.
 *
 * @author x9x
 * @since 2024-09-28 21:37
 */
@UtilityClass
public class SpringBootProfileUtils {
    /**
     * Adds the development profile as the default profile for a Spring Boot application.
     * This method sets the "spring.profiles.default" property to the development profile,
     * ensuring that the application runs in development mode by default if no other profile is specified.
     *
     * @param application the Spring Boot application to configure with the default profile
     */
    public static void addDefaultProfile(SpringApplication application) {
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put(SpringProperties.SPRING_PROFILE_DEFAULT, SpringProfiles.SPRING_PROFILE_DEVELOPMENT);
        application.setDefaultProperties(defaultProperties);
    }
}
