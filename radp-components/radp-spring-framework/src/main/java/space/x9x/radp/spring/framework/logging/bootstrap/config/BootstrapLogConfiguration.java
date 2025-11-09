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

package space.x9x.radp.spring.framework.logging.bootstrap.config;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import space.x9x.radp.spring.framework.logging.bootstrap.filter.BootstrapLogHttpFilter;

/**
 * Configuration class for bootstrap logging functionality. This class sets up the
 * necessary beans for HTTP request logging during application bootstrap and runtime. It
 * configures a servlet filter that adds contextual information to the logging MDC (Mapped
 * Diagnostic Context).
 *
 * <p>
 * The configuration includes:
 * <ul>
 * <li>A BootstrapLogHttpFilter bean that captures and logs HTTP request information</li>
 * <li>A filter registration component that integrates the filter into the servlet
 * container</li>
 * </ul>
 *
 * <p>
 * This configuration is automatically imported when the
 * {@link space.x9x.radp.spring.framework.logging.EnableBootstrapLog} annotation is used
 * on a Spring configuration class.
 *
 * @author x9x
 * @since 2024-09-30 11:09
 * @see space.x9x.radp.spring.framework.logging.EnableBootstrapLog
 * @see space.x9x.radp.spring.framework.logging.bootstrap.filter.BootstrapLogHttpFilter
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class BootstrapLogConfiguration {

	/**
	 * Creates and configures a BootstrapLogHttpFilter bean. This filter is used to log
	 * HTTP requests and responses during application bootstrap.
	 * @param environment the Spring environment for configuration properties
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
	 * Filter registration class for the BootstrapLogHttpFilter. This class implements the
	 * Filter interface and delegates all filter operations to the autowired
	 * BootstrapLogHttpFilter instance.
	 */
	@WebFilter(filterName = "bootstrapLogHttpFilter", urlPatterns = "/*")
	@Component
	public static class CustomFilterRegistration implements Filter {

		/**
		 * The BootstrapLogHttpFilter instance to which all filter operations are
		 * delegated. This field is initialized through constructor injection.
		 */
		private final BootstrapLogHttpFilter bootstrapLogHttpFilter;

		/**
		 * Constructs a new CustomFilterRegistration with the specified
		 * BootstrapLogHttpFilter.
		 * @param bootstrapLogHttpFilter the filter to delegate all operations to
		 */
		public CustomFilterRegistration(BootstrapLogHttpFilter bootstrapLogHttpFilter) {
			this.bootstrapLogHttpFilter = bootstrapLogHttpFilter;
		}

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
			this.bootstrapLogHttpFilter.init(filterConfig);
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			this.bootstrapLogHttpFilter.doFilter(request, response, chain);
		}

		@Override
		public void destroy() {
			this.bootstrapLogHttpFilter.destroy();
		}

	}

}
