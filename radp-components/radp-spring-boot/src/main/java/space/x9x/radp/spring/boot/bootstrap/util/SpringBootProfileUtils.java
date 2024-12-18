package space.x9x.radp.spring.boot.bootstrap.util;

import space.x9x.radp.spring.framework.bootstrap.constant.SpringProfiles;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * @author x9x
 * @since 2024-09-28 21:37
 */
@UtilityClass
public class SpringBootProfileUtils {
    public static void addDefaultProfile(SpringApplication application) {
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put(SpringProperties.SPRING_PROFILE_DEFAULT, SpringProfiles.SPRING_PROFILE_DEVELOPMENT);
        application.setDefaultProperties(defaultProperties);
    }
}
