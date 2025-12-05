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

package space.x9x.radp.solutions.tenant.dataobject;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TenantBasePO} and its Lombok-generated builder.
 */
class TenantBasePOTests {

	/**
	 * A minimal concrete implementation used for testing the abstract base class.
	 * Annotated with {@code @SuperBuilder} so that Lombok generates a builder which
	 * exercises {@code TenantBasePO.TenantBasePOBuilder} as part of the chain.
	 */
	@SuperBuilder
	@NoArgsConstructor
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	static class TestTenantPO extends TenantBasePO {

		// no extra fields

	}

	@Test
	void setterGetter_shouldWorkForTenantId() {
		TenantBasePO po = new TestTenantPO();
		po.setTenantId(123L);
		assertThat(po.getTenantId()).isEqualTo(123L);
	}

	@Test
	void builder_shouldCreateInstanceAndPopulateTenantId() {
		TestTenantPO po = TestTenantPO.builder().tenantId(456L).build();
		assertThat(po.getTenantId()).isEqualTo(456L);
		assertThat(po.toString()).contains("tenantId=456");
	}

	@Test
	void equalsAndHashCode_shouldConsiderTenantId() {
		TestTenantPO a = TestTenantPO.builder().tenantId(1L).build();
		TestTenantPO b = TestTenantPO.builder().tenantId(1L).build();
		TestTenantPO c = TestTenantPO.builder().tenantId(2L).build();

		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());
		assertThat(a).isNotEqualTo(c);
	}

}
