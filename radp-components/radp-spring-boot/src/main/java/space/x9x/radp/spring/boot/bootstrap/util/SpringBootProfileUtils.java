package space.x9x.radp.spring.boot.bootstrap.util;

import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProfiles;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author x9x
 * @since 2024-09-28 21:37
 */
@UtilityClass
public class SpringBootProfileUtils {
    /**
     * Adds a default profile to a Spring Boot application.
     * This method sets the default Spring profile to the development profile
     * if no profile is explicitly specified when the application starts.
     *
     * @param application the Spring Boot application to configure
     */
    public static void addDefaultProfile(SpringApplication application) {
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put(SpringProperties.SPRING_PROFILE_DEFAULT, SpringProfiles.SPRING_PROFILE_DEVELOPMENT);
        application.setDefaultProperties(defaultProperties);
    }
}
