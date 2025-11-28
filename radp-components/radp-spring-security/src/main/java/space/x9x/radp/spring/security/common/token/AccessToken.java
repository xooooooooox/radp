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

package space.x9x.radp.spring.security.common.token;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import space.x9x.radp.spring.security.jwt.constants.JwtConstants;

/**
 * @author x9x
 * @since 2025-11-22 23:49
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class AccessToken {

	/**
	 * 访问令牌.
	 */
	private String value;

	/**
	 * 刷新令牌.
	 */
	private RefreshToken refreshToken;

	/**
	 * 过期时间.
	 */
	private Instant expiration;

	/**
	 * 授权类型.
	 */
	private String tokenType = JwtConstants.BEARER_TYPE;

	/**
	 * 作用域.
	 */
	private Set<String> scopes;

	@Builder.Default
	private Map<String, Object> additionalInformation = Collections.emptyMap();

	/**
	 * Remaining lifetime in seconds.
	 * @return seconds until expiration; 0 if already expired or no expiration set
	 */
	public int getExpiresIn() {
		// 返回剩余秒速(小于 0 时 返回 0)
		long secondsRemaining = 0;
		if (this.expiration != null) {
			secondsRemaining = this.expiration.getEpochSecond() - Instant.now().getEpochSecond();
		}
		return Math.toIntExact(Math.max(secondsRemaining, 0L));
	}

	/**
	 * @param delta 秒
	 */
	public void setExpiresIn(int delta) {
		setExpiration(Instant.now().plusSeconds(delta));
	}

	/**
	 * Whether the token is expired.
	 * @return true if the expiration is before now; false otherwise
	 */
	public boolean isExpired() {
		return this.expiration != null && this.expiration.isBefore(Instant.now());
	}

}
