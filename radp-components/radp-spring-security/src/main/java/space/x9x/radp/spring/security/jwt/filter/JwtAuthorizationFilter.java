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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.PathMatcher;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.spring.framework.web.util.ServletUtils;
import space.x9x.radp.spring.security.common.token.AccessToken;
import space.x9x.radp.spring.security.jwt.constants.JwtConstants;
import space.x9x.radp.spring.security.jwt.token.JwtTokenProvider;

/**
 * @author x9x
 * @since 2025-11-23 02:21
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final JwtTokenProvider jwtTokenProvider;

	private final PathMatcher pathMatcher;

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
				ServletUtils.wrap(response, HttpServletResponse.SC_UNAUTHORIZED, "1000", "token is required");
				return;
			}
			try {
				this.jwtTokenProvider.validateToken(accessToken);
			}
			catch (Exception ex) {
				ServletUtils.wrap(response, HttpServletResponse.SC_UNAUTHORIZED, "1000", ex.getMessage());
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
		String requestURI = request.getRequestURI();
		return anonymousUrls.stream().anyMatch(url -> this.pathMatcher.match(url, requestURI));
	}

	private AccessToken resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(this.jwtTokenProvider.getJwtConfig().getHeader());
		if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(JwtConstants.BEARER_PREFIX)) {
			return AccessToken.builder().value(bearerToken.substring(JwtConstants.BEARER_PREFIX.length())).build();
		}
		return null;
	}

}
