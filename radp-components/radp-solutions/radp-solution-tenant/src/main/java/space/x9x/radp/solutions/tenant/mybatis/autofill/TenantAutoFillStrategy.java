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

package space.x9x.radp.solutions.tenant.mybatis.autofill;

import java.util.Objects;

import org.apache.ibatis.reflection.MetaObject;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import space.x9x.radp.solutions.tenant.context.TenantContextHolder;
import space.x9x.radp.solutions.tenant.dataobject.TenantBasePO;
import space.x9x.radp.spring.data.mybatis.autofill.AbstractAutoFillStrategy;

/**
 * Autofill strategy that supplies {@code tenantId} values for entities extending
 * {@link TenantBasePO}. Runs alongside the default
 * {@link space.x9x.radp.spring.data.mybatis.autofill.BasePOAutoFillStrategy} so audit
 * fields keep working out of the box.
 *
 * @author x9x
 * @since 2025-11-11 20:58
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class TenantAutoFillStrategy extends AbstractAutoFillStrategy<TenantBasePO> {

	/**
	 * Creates a tenant autofill strategy targeting {@link TenantBasePO}.
	 */
	public TenantAutoFillStrategy() {
		super(TenantBasePO.class);
	}

	@Override
	protected void doInsertFill(TenantBasePO entity, MetaObject metaObject) {
		fillTenantIdWhenMissing(entity);
	}

	@Override
	protected void doUpdateFill(TenantBasePO entity, MetaObject metaObject) {
		fillTenantIdWhenMissing(entity);
	}

	private void fillTenantIdWhenMissing(TenantBasePO entity) {
		if (Objects.nonNull(entity.getTenantId())) {
			return;
		}
		TenantContextHolder.getTenantIdOptional().ifPresent(entity::setTenantId);
	}

}
