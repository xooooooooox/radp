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

package space.x9x.radp.solutions.tenant.context;

import java.util.Optional;

import org.springframework.util.Assert;

/**
 * Simple thread-local holder for the current tenant id. Application gateways or web
 * filters should populate it at the beginning of each request. Autofill strategies and
 * other infrastructure components can then read the tenant id without depending on web
 * APIs directly.
 *
 * @author RADP x9x
 * @since 2025-11-11 20:58
 */
public final class TenantContextHolder {

	private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

	private TenantContextHolder() {
	}

	/**
	 * Set the current tenant id. Passing {@code null} clears the context.
	 * @param tenantId logical tenant identifier
	 */
	public static void setTenantId(Long tenantId) {
		if (tenantId == null) {
			clear();
		}
		else {
			TENANT_ID.set(tenantId);
		}
	}

	/**
	 * @return current tenant id or {@code null} when unset
	 */
	public static Long getTenantId() {
		return TENANT_ID.get();
	}

	/**
	 * @return optional containing the tenant id when available
	 */
	public static Optional<Long> getTenantIdOptional() {
		return Optional.ofNullable(getTenantId());
	}

	/**
	 * Obtain the tenant id or throw if absent.
	 * @return tenant id
	 * @throws IllegalStateException if no tenant id is bound
	 */
	public static Long requireTenantId() {
		return getTenantIdOptional().orElseThrow(() -> new IllegalStateException("tenant id is not bound"));
	}

	/**
	 * Clear the current tenant from the holder.
	 */
	public static void clear() {
		TENANT_ID.remove();
	}

	/**
	 * Run the provided action with the supplied tenant id, restoring the previous context
	 * afterward.
	 * @param tenantId tenant identifier to use during the action
	 * @param runnable action to run
	 */
	public static void withTenantId(Long tenantId, Runnable runnable) {
		Assert.notNull(runnable, "runnable must not be null");
		Long previous = getTenantId();
		try {
			setTenantId(tenantId);
			runnable.run();
		}
		finally {
			setTenantId(previous);
		}
	}

}
