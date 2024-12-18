package space.x9x.radp.dubbo.spring.cloud.autoconfigure;

import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * @author x9x
 * @since 2024-10-01 23:41
 */
public class DubboAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {

    private static final String MATCH_KEY = "dubbo.enabled";
    private static final String[] IGNORE_CLASSES = {
            "org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboEndpointAnnotationAutoConfiguration",
            "org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboMetricsAutoConfiguration",
            "org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboEndpointAutoConfiguration",
            "org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboHealthIndicatorAutoConfiguration",
            "org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboEndpointMetadataAutoConfiguration",
    };

    private Environment environment;

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean disabled = !Boolean.parseBoolean(environment.getProperty(MATCH_KEY, Conditions.TRUE));
        boolean[] match = new boolean[autoConfigurationClasses.length];
        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            int index = i;
            match[i]=!disabled || Arrays.stream(IGNORE_CLASSES).noneMatch(e -> e.equals(autoConfigurationClasses[index]));
        }
        return match;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
