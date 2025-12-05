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

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MybatisPluginProperties}.
 */
class MybatisPluginPropertiesTests {

	@Test
	void defaults_shouldBeAsExpected_andSettersShouldWork() {
		MybatisPluginProperties props = new MybatisPluginProperties();
		assertThat(props.getSqlLog().isEnabled()).isFalse();
		assertThat(props.getSqlLog().getSlownessThreshold()).isEqualTo(Duration.ofMillis(1000));

		props.getSqlLog().setEnabled(true);
		props.getSqlLog().setSlownessThreshold(Duration.ofMillis(250));

		assertThat(props.getSqlLog().isEnabled()).isTrue();
		assertThat(props.getSqlLog().getSlownessThreshold()).isEqualTo(Duration.ofMillis(250));
	}

}
