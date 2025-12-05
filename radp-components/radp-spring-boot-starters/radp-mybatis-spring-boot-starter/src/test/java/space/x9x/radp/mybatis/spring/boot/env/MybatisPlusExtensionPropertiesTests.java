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

package space.x9x.radp.mybatis.spring.boot.env;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MybatisPlusExtensionProperties} and its inner classes to contribute
 * coverage for configuration holder types.
 */
class MybatisPlusExtensionPropertiesTests {

	@Test
	void defaults_shouldBeAsExpected_andSettersShouldWork() {
		MybatisPlusExtensionProperties props = new MybatisPlusExtensionProperties();

		// default AutoFill disabled
		assertThat(props.getAutoFill().isEnabled()).isFalse();
		props.getAutoFill().setEnabled(true);
		assertThat(props.getAutoFill().isEnabled()).isTrue();

		// default SqlRewrite
		assertThat(props.getSqlRewrite().isEnabled()).isFalse();
		assertThat(props.getSqlRewrite().getScope()).isEqualTo(MybatisPlusExtensionProperties.SqlRewrite.Scope.BASE_PO);
		assertThat(props.getSqlRewrite().getCreatedColumnName()).isNotBlank();
		assertThat(props.getSqlRewrite().getLastModifiedColumnName()).isNotBlank();
		assertThat(props.getSqlRewrite().getCreatorColumnName()).isNotBlank();
		assertThat(props.getSqlRewrite().getUpdaterColumnName()).isNotBlank();

		props.getSqlRewrite().setEnabled(true);
		props.getSqlRewrite().setScope(MybatisPlusExtensionProperties.SqlRewrite.Scope.GLOBAL);
		props.getSqlRewrite().setCreatedColumnName("c");
		props.getSqlRewrite().setLastModifiedColumnName("u");
		props.getSqlRewrite().setCreatorColumnName("cr");
		props.getSqlRewrite().setUpdaterColumnName("up");

		assertThat(props.getSqlRewrite().isEnabled()).isTrue();
		assertThat(props.getSqlRewrite().getScope()).isEqualTo(MybatisPlusExtensionProperties.SqlRewrite.Scope.GLOBAL);
		assertThat(props.getSqlRewrite().getCreatedColumnName()).isEqualTo("c");
		assertThat(props.getSqlRewrite().getLastModifiedColumnName()).isEqualTo("u");
		assertThat(props.getSqlRewrite().getCreatorColumnName()).isEqualTo("cr");
		assertThat(props.getSqlRewrite().getUpdaterColumnName()).isEqualTo("up");
	}

}
