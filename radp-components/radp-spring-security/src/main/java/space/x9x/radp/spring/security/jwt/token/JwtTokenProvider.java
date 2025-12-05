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

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringConstants;
import space.x9x.radp.spring.framework.error.http.UnauthorizedException;
import space.x9x.radp.spring.security.common.token.AccessToken;
import space.x9x.radp.spring.security.jwt.config.JwtConfig;
import space.x9x.radp.spring.security.jwt.constants.JwtConstants;

/**
 * @author RADP x9x
 * @since 2025-11-22 22:49
 */
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtTokenProvider implements InitializingBean {

	private final JwtConfig jwtConfig;

	private final JwtTokenStore jwtTokenStore;

	private Key key;

	private JwtParser jwtParser;

	private long tokenValidityInMilliseconds;

	private long tokenValidityInMillisecondsForRememberMe;

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = (this.jwtConfig.getBase64Secret() != null)
				? Decoders.BASE64.decode(this.jwtConfig.getSecret()) : this.jwtConfig.getSecret().getBytes();

		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.jwtParser = Jwts.parserBuilder().setSigningKey(this.key).build();
		this.tokenValidityInMilliseconds = 1000 * this.jwtConfig.getTokenValidityInSeconds();
		this.tokenValidityInMillisecondsForRememberMe = 1000 * this.jwtConfig.getTokenValidityInSecondsForRememberMe();
	}

	/**
	 * Create a JWT {@link AccessToken} using the given {@link Authentication} as subject.
	 * Populates authorities into claims when present.
	 * @param authentication spring Security authentication
	 * @param rememberMe whether to use the longer remember-me validity
	 * @param claims extra claims to include in the token (mutable)
	 * @return newly created access token
	 */
	public AccessToken createToken(Authentication authentication, boolean rememberMe, Map<String, Object> claims) {
		if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
			String authorities = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(StringConstants.COMMA));
			claims.put(JwtConstants.AUTHORITIES_KEY, authorities);
		}
		return createToken(authentication.getName(), rememberMe, claims);
	}

	/**
	 * Create a JWT {@link AccessToken} for a given subject.
	 * @param subject jwt subject (typically username)
	 * @param rememberMe whether to use the longer remember-me validity
	 * @param claims extra claims to include in the token
	 * @return newly created access token
	 */
	public AccessToken createToken(String subject, boolean rememberMe, Map<String, Object> claims) {
		Instant expiration;
		if (rememberMe) {
			expiration = Instant.now().plusMillis(this.tokenValidityInMillisecondsForRememberMe);
		}
		else {
			expiration = Instant.now().plusMillis(this.tokenValidityInMilliseconds);
		}

		String value = Jwts.builder()
			.setSubject(subject)
			.addClaims(claims)
			.signWith(this.key, SignatureAlgorithm.HS512)
			.setExpiration(Date.from(expiration))
			.compact();

		AccessToken accessToken = AccessToken.builder().value(value).expiration(expiration).build();

		if (this.jwtTokenStore != null) {
			this.jwtTokenStore.storeAccessToken(accessToken);
		}
		return accessToken;
	}

	/**
	 * Validate the given access token.
	 * @param accessToken jwt access token wrapper to validate (must not be {@code null})
	 * @throws UnauthorizedException if the token is invalid, expired, or otherwise not
	 * accepted
	 */
	public void validateToken(@NotNull AccessToken accessToken) {
		try {
			if (this.jwtTokenStore != null && !this.jwtTokenStore.validateAccessToken(accessToken)) {
				throw new UnauthorizedException("access token is invalid"); // 非法令牌
			}
			this.jwtParser.parseClaimsJws(accessToken.getValue());
		}
		catch (ExpiredJwtException ex) {
			log.debug(ex.getMessage(), ex);
			throw new UnauthorizedException("access token expired");
		}
		catch (UnsupportedJwtException ex) {
			log.debug(ex.getMessage(), ex);
			throw new UnauthorizedException("Unsupported access token");
		}
		catch (MalformedJwtException ex) {
			log.debug(ex.getMessage(), ex);
			throw new UnauthorizedException("Malformed access token");
		}
		catch (SignatureException ex) {
			log.debug(ex.getMessage(), ex);
			throw new UnauthorizedException("Invalid access token signature");
		}
		catch (IllegalArgumentException ex) {
			log.debug(ex.getMessage(), ex);
			throw new UnauthorizedException("Invalid access token arguments");
		}
	}

	/**
	 * Remove the given token from the token store, if a store is configured.
	 * @param accessToken token to clear from the store
	 */
	public void clearToken(AccessToken accessToken) {
		if (this.jwtTokenStore != null) {
			this.jwtTokenStore.removeAccessToken(accessToken);
		}
	}

	/**
	 * Build an {@link Authentication} object from the given {@link AccessToken}.
	 * <p>
	 * Typically used to reconstruct the Spring Security authentication for the current
	 * request based on the JWT.
	 * </p>
	 * @param accessToken jwt access token wrapper
	 * @return an {@link Authentication} representing the token’s principal and
	 * authorities
	 */
	public Authentication getAuthentication(AccessToken accessToken) {
		Claims claims = parseClaims(accessToken);

		List<? extends GrantedAuthority> authorities = Collections.emptyList();
		if (claims.containsKey(JwtConstants.AUTHORITIES_KEY)) {
			authorities = Arrays
				.stream(claims.get(JwtConstants.AUTHORITIES_KEY).toString().split(StringConstants.COMMA))
				.filter(auth -> !auth.trim().isEmpty())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		}

		User principal = new User(claims.getSubject(), StringConstants.EMPTY, authorities);
		return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
	}

	/**
	 * Parse and return the JWT {@link Claims} contained in the given {@link AccessToken}.
	 * @param accessToken jwt access token wrapper
	 * @return parsed JWT claims
	 * @throws ExpiredJwtException if the token is expired
	 * @throws UnsupportedJwtException if the token uses an unsupported format or
	 * algorithm
	 * @throws MalformedJwtException if the token string is not a valid JWT
	 * @throws SignatureException if the token signature is invalid
	 * @throws IllegalArgumentException if the token value is {@code null} or otherwise
	 * invalid
	 */
	public Claims parseClaims(AccessToken accessToken) {
		return this.jwtParser.parseClaimsJws(accessToken.getValue()).getBody();
	}

}
