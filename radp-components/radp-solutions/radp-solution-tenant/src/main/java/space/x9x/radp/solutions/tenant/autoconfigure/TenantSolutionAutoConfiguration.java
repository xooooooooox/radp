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

package space.x9x.radp.solutions.tenant.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import space.x9x.radp.solutions.tenant.mybatis.autofill.TenantAutoFillStrategy;
import space.x9x.radp.spring.data.mybatis.autofill.AutoFillStrategy;

/**
 * Autoconfiguration that contributes the tenant autofill strategy when the RADP MyBatis
 * autofill infrastructure is present.
 *
 * @author x9x
 * @since 2025-11-11 20:58
 */
@AutoConfiguration
@ConditionalOnClass(AutoFillStrategy.class)
public class TenantSolutionAutoConfiguration {

	/**
	 * Registers the tenant autofill strategy bean.
	 * @return a {@link TenantAutoFillStrategy} bean
	 */
	@Bean
	@ConditionalOnMissingBean
	public TenantAutoFillStrategy tenantAutoFillStrategy() {
		return new TenantAutoFillStrategy();
	}

}
