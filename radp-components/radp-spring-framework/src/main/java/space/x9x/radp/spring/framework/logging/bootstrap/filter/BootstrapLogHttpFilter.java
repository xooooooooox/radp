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

package space.x9x.radp.spring.framework.logging.bootstrap.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.MDC;

import org.springframework.core.env.Environment;

import space.x9x.radp.commons.lang.StrUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.framework.logging.MdcConstants;
import space.x9x.radp.spring.framework.web.util.ServletUtils;

/**
 * HTTP filter that adds application and request information to the MDC (Mapped Diagnostic
 * Context) for logging purposes. This filter enriches logs with contextual information
 * such as application name, active profile, request URI, and IP addresses.
 *
 * @author x9x
 * @since 2024-09-30 11:10
 */
@RequiredArgsConstructor
public class BootstrapLogHttpFilter extends HttpFilter {

	private static final long serialVersionUID = 1L;

	/**
	 * Flag to enable or disable MDC logging. When false, no MDC context is added
	 */
	@Setter
	private boolean enabledMdc = false;

	/**
	 * Spring environment used to retrieve application properties and profiles.
	 */
	private final Environment env;

	/**
	 * Filters HTTP requests to add application and request information to the MDC. This
	 * method extracts information from the request and environment, then adds it to the
	 * MDC context for use in logging statements.
	 * @param req the HTTP servlet request being processed
	 * @param res the HTTP servlet response being generated
	 * @param chain the filter chain for invoking the next filter
	 * @throws IOException if an I/O error occurs during request processing
	 * @throws ServletException if a servlet error occurs during request processing
	 */
	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		if (this.enabledMdc) {
			String appName = StrUtils.trimToEmpty(this.env.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
			String profile = StrUtils.trimToEmpty(String.join(Strings.COMMA, getActiveProfiles(this.env)));
			String requestURI = ServletUtils.getRequestURI(req);
			String remoteUser = ServletUtils.getRemoteUser(req);
			String remoteAddr = IpConfigUtils.parseIpAddress(req);
			String localAddr = ServletUtils.getLocalAddr(req);

			MDC.put(MdcConstants.APP, appName);
			MDC.put(MdcConstants.PROFILE, profile);
			MDC.put(MdcConstants.REQUEST_URI, requestURI);
			MDC.put(MdcConstants.REMOTE_USER, remoteUser);
			MDC.put(MdcConstants.REMOTE_ADDR, remoteAddr);
			MDC.put(MdcConstants.LOCAL_ADDR, localAddr);
		}

		chain.doFilter(req, res);
	}

	/**
	 * Gets the active profiles from the Spring environment. If no active profiles are
	 * set, returns the default profiles.
	 * @param env the Spring environment to get profiles from
	 * @return an array of active profile names, or default profiles if no active profiles
	 * are set
	 */
	private static String[] getActiveProfiles(Environment env) {
		String[] profiles = env.getActiveProfiles();
		if (profiles.length == 0) {
			return env.getDefaultProfiles();
		}
		return profiles;
	}

}
