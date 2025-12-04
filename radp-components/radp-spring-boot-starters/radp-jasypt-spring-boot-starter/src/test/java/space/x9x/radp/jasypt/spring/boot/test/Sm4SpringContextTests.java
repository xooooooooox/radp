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

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import space.x9x.radp.jasypt.spring.boot.TestApplication;
import space.x9x.radp.jasypt.spring.boot.util.JasyptUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verify that our starter provides a SM4-capable StringEncryptor bean when only
 * jasypt.encryptor.password (and algorithm=SM4) are configured.
 */
@SpringBootTest(classes = TestApplication.class,
		properties = { "jasypt.encryptor.algorithm=SM4", "jasypt.encryptor.password=dsaf#,jds.klfj1" })
class Sm4SpringContextTests {

	@Autowired
	private StringEncryptor encryptor;

	@Test
	void contextEncryptorShouldSupportSm4() {
		String[] plainTexts = { "root", "123456", "简体中文", "with symbols !@#" };
		for (String plain : plainTexts) {
			String enc = JasyptUtils.encrypt(encryptor, plain);
			String dec = JasyptUtils.decrypt(encryptor, enc);
			assertThat(dec).isEqualTo(plain);
		}
	}

}
