package space.x9x.radp.spring.boot.logging.env;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.framework.bootstrap.utils.SpringProfileUtils;
import space.x9x.radp.spring.framework.logging.MdcConstants;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 全局日志环境解析后置处理
 * @author x9x
 * @since 2024-09-27 21:16
 */
public class BootstrapLogEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (environment.containsProperty(BootstrapLogProperties.ENABLED) || Boolean.parseBoolean(StringUtils.trimToEmpty(environment.getProperty(BootstrapLogProperties.ENABLED)))) {
            String appName = StringUtils.trimToEmpty(environment.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
            String profile = StringUtils.trimToEmpty(String.join(Strings.COMMA, SpringProfileUtils.getActiveProfiles(environment)));
            MDC.put(MdcConstants.APP, appName);
            MDC.put(MdcConstants.PROFILE, profile);
            MDC.put(MdcConstants.LOCAL_ADDR, IpConfigUtils.getIpAddress());
        }
    }
}
