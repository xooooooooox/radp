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

package space.x9x.radp.spring.security.jwt.filter;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.PathMatcher;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.spring.framework.web.util.ServletUtils;
import space.x9x.radp.spring.security.common.token.AccessToken;
import space.x9x.radp.spring.security.jwt.constants.JwtConstants;
import space.x9x.radp.spring.security.jwt.token.JwtTokenProvider;

/**
 * JwtAuthorizationFilter is a custom authorization filter that extends
 * BasicAuthenticationFilter to handle JWT-based authentication and authorization.
 * <p>
 * This filter intercepts HTTP requests to extract and validate the JWT access token from
 * the request headers, perform authentication, and populate the SecurityContext with the
 * authenticated user's details.
 *
 * @author RADP x9x
 * @since 2025-11-23 02:21
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final JwtTokenProvider jwtTokenProvider;

	private final PathMatcher pathMatcher;

	/**
	 * Constructs a JWT authorization filter.
	 * @param authenticationManager authentication manager used by the filter
	 * @param jwtTokenProvider provider to validate and parse JWT tokens
	 * @param pathMatcher matcher for anonymous URL patterns
	 */
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			PathMatcher pathMatcher) {
		super(authenticationManager);
		this.jwtTokenProvider = jwtTokenProvider;
		this.pathMatcher = pathMatcher;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!isAnonymousUrls(request)) {
			AccessToken accessToken = resolveToken(request);
			if (accessToken == null) {
				ServletUtils.wrap(response, HttpServletResponse.SC_UNAUTHORIZED, "0400", "token is required");
				return;
			}
			try {
				this.jwtTokenProvider.validateToken(accessToken);
			}
			catch (Exception ex) {
				ServletUtils.wrap(response, HttpServletResponse.SC_UNAUTHORIZED, "0400", ex.getMessage());
				return;
			}
			Authentication authentication = this.jwtTokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}

	private boolean isAnonymousUrls(HttpServletRequest request) {
		List<String> anonymousUrls = this.jwtTokenProvider.getJwtConfig().getAnonymousUrls();
		if (CollectionUtils.isEmpty(anonymousUrls)) {
			return false;
		}
		// 这里不使用 request.getRequestURI() 的原因：
		// 1. requestURI 通常包含 contextPath，而我们的匿名 URL 配置一般是以应用内路径（不含 contextPath）为基准，
		// 如果直接用 requestURI 去匹配会因为前缀不同导致匹配失败。
		// 2. 在经过反向代理或网关时，请求的真实访问路径可能被重写，servletPath 更接近应用内部实际处理的路径，
		// 与 Spring 的路径匹配策略（如 AntPathMatcher）更一致。
		// 因此这里使用 getServletPath() 来做路径匹配，避免因为 contextPath 或代理重写导致的匹配误差。
		String servletPath = request.getServletPath();
		return anonymousUrls.stream().anyMatch(url -> this.pathMatcher.match(url, servletPath));
	}

	private AccessToken resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(this.jwtTokenProvider.getJwtConfig().getHeader());
		if (StringUtil.isNotBlank(bearerToken) && bearerToken.startsWith(JwtConstants.BEARER_PREFIX)) {
			return AccessToken.builder().value(bearerToken.substring(JwtConstants.BEARER_PREFIX.length())).build();
		}
		return null;
	}

}
