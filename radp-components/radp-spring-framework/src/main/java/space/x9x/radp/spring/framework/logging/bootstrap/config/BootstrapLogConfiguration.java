package space.x9x.radp.spring.framework.logging.bootstrap.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import space.x9x.radp.spring.framework.logging.bootstrap.filter.BootstrapLogHttpFilter;

import java.io.IOException;

/**
 * 引导日志配置
 *
 * @author x9x
 * @since 2024-09-30 11:09
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class BootstrapLogConfiguration {

    /**
     * Creates and configures a BootstrapLogHttpFilter bean.
     * This filter is used to log HTTP requests and responses during application bootstrap.
     *
     * @param environment         the Spring environment for configuration properties
     * @param bootstrapLogConfigs provider for bootstrap log configuration
     * @return a configured BootstrapLogHttpFilter instance
     */
    @Bean
    public BootstrapLogHttpFilter logHttpFilter(Environment environment,
                                                ObjectProvider<BootstrapLogConfig> bootstrapLogConfigs) {
        BootstrapLogConfig config = bootstrapLogConfigs.getIfUnique(BootstrapLogConfig::new);
        BootstrapLogHttpFilter httpFilter = new BootstrapLogHttpFilter(environment);
        httpFilter.setEnabledMdc(config.isEnabledMdc());
        return httpFilter;
    }

    /**
     * Filter registration class for the BootstrapLogHttpFilter.
     * This class implements the Filter interface and delegates all filter operations
     * to the autowired BootstrapLogHttpFilter instance.
     */
    @WebFilter(filterName = "bootstrapLogHttpFilter", urlPatterns = "/*")
    public static class CustomFilterRegistration implements Filter {

        @Autowired
        private BootstrapLogHttpFilter bootstrapLogHttpFilter;

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            bootstrapLogHttpFilter.init(filterConfig);
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            bootstrapLogHttpFilter.doFilter(request, response, chain);
        }

        @Override
        public void destroy() {
            bootstrapLogHttpFilter.destroy();
        }
    }
}
