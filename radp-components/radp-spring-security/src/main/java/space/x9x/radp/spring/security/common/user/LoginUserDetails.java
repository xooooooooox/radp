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

package space.x9x.radp.spring.security.common.user;

import java.io.Serial;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author x9x
 * @since 2025-11-23 00:30
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class LoginUserDetails implements UserDetails {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Login username.
	 */
	private String username;

	/**
	 * Encoded user password.
	 */
	private String password;

	/**
	 * Granted authorities for this user.
	 */
	private Collection<? extends GrantedAuthority> authorities;

	/**
	 * Whether the account is non-expired.
	 */
	@Builder.Default
	private boolean accountNonExpired = true;

	/**
	 * Whether the account is non-locked.
	 */
	@Builder.Default
	private boolean accountNonLocked = true;

	/**
	 * Whether the credentials are non-expired.
	 */
	@Builder.Default
	private boolean credentialsNonExpired = true;

	/**
	 * Whether the account is enabled.
	 */
	@Builder.Default
	private boolean enabled = true;

	/**
	 * Whether to remember user (longer token validity).
	 */
	@Builder.Default
	private boolean rememberMe = false;

	/**
	 * @return 权限列表
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
