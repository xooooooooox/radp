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

package space.x9x.radp.spring.security.jwt.token;

import java.util.Map;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.spring.framework.error.http.UnauthorizedException;
import space.x9x.radp.spring.security.common.token.AccessToken;
import space.x9x.radp.spring.security.common.user.LoginUserDetails;
import space.x9x.radp.spring.security.jwt.constants.JwtConstants;

/**
 * @author x9x
 * @since 2025-11-23 01:47
 */
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * Authenticate the given user and issue a new {@link AccessToken}.
	 * @param loginUserDetails authenticated user details
	 * @param claims extra custom claims to include in the token, may be {@code null}
	 * @return a newly created access token representing the authenticated user
	 */
	public AccessToken authenticate(LoginUserDetails loginUserDetails, Map<String, Object> claims) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					loginUserDetails.getUsername(), loginUserDetails.getPassword());
			Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (authentication.getAuthorities() != null) {
				if (claims == null) {
					claims = Maps.newHashMap();
				}
				StringBuilder authorities = new StringBuilder();
				authentication.getAuthorities()
					.forEach(authority -> authorities.append(authority.getAuthority()).append(Strings.COMMA));
				claims.put(JwtConstants.AUTHORITIES_KEY, authorities.toString());
			}

			return this.jwtTokenProvider.createToken(authentication, loginUserDetails.isRememberMe(), claims);
		}
		catch (BadCredentialsException ex) {
			log.error("JWT authenticated failed due to bad credentials：{}", ex.getMessage(), ex);
			throw new UnauthorizedException(ex.getMessage());
		}
		catch (Exception ex) {
			log.error("JWT authenticated failed, caught exception: {}", ex.getMessage(), ex);
			throw ex;
		}
	}

}
