package space.x9x.radp.spring.framework.logging.bootstrap.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import space.x9x.radp.spring.framework.logging.bootstrap.filter.BootstrapLogHttpFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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
     * This filter is responsible for logging HTTP requests and responses during application bootstrap.
     * It can also populate the MDC (Mapped Diagnostic Context) with request information for logging.
     *
     * @param environment         the Spring environment, used to access configuration properties
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
     * Servlet filter registration for the bootstrap log HTTP filter.
     * This class acts as a wrapper around the BootstrapLogHttpFilter, delegating all filter
     * operations to it. It's annotated with @WebFilter to automatically register
     * the filter in the servlet container for all URL patterns.
     */
    @WebFilter(filterName = "bootstrapLogHttpFilter", urlPatterns = "/*")
    @Component
    public static class CustomFilterRegistration implements Filter {

        private final BootstrapLogHttpFilter bootstrapLogHttpFilter;

        public CustomFilterRegistration(BootstrapLogHttpFilter bootstrapLogHttpFilter) {
            this.bootstrapLogHttpFilter = bootstrapLogHttpFilter;
        }

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
