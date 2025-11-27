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

package space.x9x.radp.mybatis.spring.boot.autofill;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.Test;

import space.x9x.radp.spring.data.mybatis.autofill.AbstractAutoFillStrategy;
import space.x9x.radp.spring.data.mybatis.autofill.BasePO;
import space.x9x.radp.spring.data.mybatis.autofill.BasePOAutoFillStrategy;
import space.x9x.radp.spring.data.mybatis.autofill.StrategyDelegatingMetaObjectHandler;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers the documented autofill scenarios:
 * <ul>
 * <li>Scenario 1 – BasePO with default column names.</li>
 * <li>Scenario 3 – Custom base class with bespoke strategy.</li>
 * </ul>
 */
class StrategyDelegatingMetaObjectHandlerTests {

	@Test
	void scenarioOneShouldPopulateBasePoAuditFields() {
		StrategyDelegatingMetaObjectHandler handler = new StrategyDelegatingMetaObjectHandler(
				Collections.singletonList(new BasePOAutoFillStrategy(null)));

		DemoPO entity = new DemoPO();
		MetaObject metaObject = SystemMetaObject.forObject(entity);

		handler.insertFill(metaObject);

		assertThat(entity.getCreatedAt()).isNotNull();
		assertThat(entity.getUpdatedAt()).isNotNull();
		assertThat(entity.getCreator()).isNull();
		assertThat(entity.getUpdater()).isNull();

		LocalDateTime originalUpdatedAt = entity.getUpdatedAt();

		handler.updateFill(SystemMetaObject.forObject(entity));

		assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
		assertThat(entity.getUpdater()).isNull();
	}

	@Test
	void scenarioThreeShouldInvokeCustomStrategyWhenBaseDoesNotMatch() {
		CustomAutoFillStrategy customStrategy = new CustomAutoFillStrategy();
		StrategyDelegatingMetaObjectHandler handler = new StrategyDelegatingMetaObjectHandler(
				Arrays.asList(customStrategy, new BasePOAutoFillStrategy(null)));

		CustomBasePO entity = new CustomBasePO();
		handler.insertFill(SystemMetaObject.forObject(entity));

		assertThat(entity.getField1()).isEqualTo("insert");
		assertThat(entity.getField2()).isEqualTo("insert");

		handler.updateFill(SystemMetaObject.forObject(entity));

		assertThat(entity.getField1()).isEqualTo("insert");
		assertThat(entity.getField2()).isEqualTo("update");
	}

	@Test
	void shouldInvokeAllStrategiesSupportingSameEntity() {
		TenantAwareAutoFillStrategy tenantStrategy = new TenantAwareAutoFillStrategy();
		StrategyDelegatingMetaObjectHandler handler = new StrategyDelegatingMetaObjectHandler(
				Arrays.asList(tenantStrategy, new BasePOAutoFillStrategy(null)));

		TenantAwarePO entity = new TenantAwarePO();
		handler.insertFill(SystemMetaObject.forObject(entity));

		assertThat(entity.getTenantId()).isEqualTo(TenantAwareAutoFillStrategy.TENANT_ID);
		assertThat(entity.getCreatedAt()).isNotNull(); // filled by BasePO strategy
	}

	private static class DemoPO extends BasePO {

	}

	private static class CustomBasePO {

		private String field1;

		private String field2;

		String getField1() {
			return this.field1;
		}

		void setField1(String field1) {
			this.field1 = field1;
		}

		String getField2() {
			return this.field2;
		}

		void setField2(String field2) {
			this.field2 = field2;
		}

	}

	private static class CustomAutoFillStrategy extends AbstractAutoFillStrategy<CustomBasePO> {

		CustomAutoFillStrategy() {
			super(CustomBasePO.class);
		}

		@Override
		protected void doInsertFill(CustomBasePO entity, MetaObject metaObject) {
			if (entity.getField1() == null) {
				entity.setField1("insert");
			}
			entity.setField2("insert");
		}

		@Override
		protected void doUpdateFill(CustomBasePO entity, MetaObject metaObject) {
			entity.setField2("update");
		}

	}

	private static class TenantAwarePO extends BasePO {

		private Long tenantId;

		Long getTenantId() {
			return this.tenantId;
		}

		void setTenantId(Long tenantId) {
			this.tenantId = tenantId;
		}

	}

	private static class TenantAwareAutoFillStrategy extends AbstractAutoFillStrategy<TenantAwarePO> {

		static final long TENANT_ID = 42L;

		TenantAwareAutoFillStrategy() {
			super(TenantAwarePO.class);
		}

		@Override
		protected void doInsertFill(TenantAwarePO entity, MetaObject metaObject) {
			entity.setTenantId(TENANT_ID);
		}

		@Override
		protected void doUpdateFill(TenantAwarePO entity, MetaObject metaObject) {
			// no-op for test
		}

	}

}
