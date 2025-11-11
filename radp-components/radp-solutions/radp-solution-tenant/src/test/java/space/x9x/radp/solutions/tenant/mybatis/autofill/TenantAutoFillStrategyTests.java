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

import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import space.x9x.radp.solutions.tenant.context.TenantContextHolder;
import space.x9x.radp.solutions.tenant.dataobject.TenantBasePO;

import static org.assertj.core.api.Assertions.assertThat;

class TenantAutoFillStrategyTests {

	private final TenantAutoFillStrategy strategy = new TenantAutoFillStrategy();

	@AfterEach
	void tearDown() {
		TenantContextHolder.clear();
	}

	@Test
	void insertShouldFillTenantIdFromContext() {
		TenantContextHolder.setTenantId(99L);
		DemoTenantPO entity = new DemoTenantPO();

		this.strategy.insertFill(entity, SystemMetaObject.forObject(entity));

		assertThat(entity.getTenantId()).isEqualTo(99L);
	}

	@Test
	void updateShouldKeepExistingTenantId() {
		TenantContextHolder.setTenantId(123L);
		DemoTenantPO entity = new DemoTenantPO();
		entity.setTenantId(88L);

		this.strategy.updateFill(entity, SystemMetaObject.forObject(entity));

		assertThat(entity.getTenantId()).isEqualTo(88L);
	}

	private static class DemoTenantPO extends TenantBasePO {

	}

}
