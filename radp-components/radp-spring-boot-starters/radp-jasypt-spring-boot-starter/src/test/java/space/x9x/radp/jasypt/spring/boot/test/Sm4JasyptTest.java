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

package space.x9x.radp.jasypt.spring.boot.test;

import org.junit.jupiter.api.Test;

import space.x9x.radp.jasypt.spring.boot.util.JasyptUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for SM4 encrypt/decrypt support.
 */
class Sm4JasyptTest {

	@Test
	void sm4RoundTrip_cbc() {
		String password = "dsaf#,jds.klfj1";
		String algorithm = "SM4/CBC/PKCS5Padding";
		String[] plainTexts = { "root", "123456", "简体中文", "with symbols !@#" };
		for (String plain : plainTexts) {
			String enc = JasyptUtils.customSM4Encrypt(plain, algorithm, password);
			String dec = JasyptUtils.customSM4Decrypt(enc, algorithm, password);
			assertThat(dec).isEqualTo(plain);
		}
	}

	@Test
	void sm4RoundTrip_ecb() {
		String password = "p@ssw0rd-16bytes";
		String algorithm = "SM4/ECB/PKCS5Padding";
		String plain = "hello-world";
		String enc = JasyptUtils.customSM4Encrypt(plain, algorithm, password);
		String dec = JasyptUtils.customSM4Decrypt(enc, algorithm, password);
		assertThat(dec).isEqualTo(plain);
	}

}
