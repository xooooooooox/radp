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

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import space.x9x.radp.jasypt.spring.boot.TestApplication;
import space.x9x.radp.jasypt.spring.boot.util.JasyptUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author RADP x9x
 * @since 2025-04-09 11:41
 */
@SpringBootTest(classes = TestApplication.class)
@Slf4j
class JasyptTest {

	@Autowired
	private StringEncryptor stringEncryptor;

	@Test
	void test() {
		String[] plainTexts = { "root", "123456" };
		for (String plainText : plainTexts) {
			// 使用当前 SpringBoot 上下文已经集成的 jasypt 加密器进行加密
			String encrypted1 = JasyptUtils.encrypt(stringEncryptor, plainText);
			// 使用当前 SpringBoot 上下文已经集成的 jasypt 加密器进行解密
			assertThat(JasyptUtils.decrypt(stringEncryptor, encrypted1)).isEqualTo(plainText);
			// 使用指定的 jasypt 加密器进行加密
			String encrypted2 = JasyptUtils.customPBEEncrypt(plainText, "PBEWithMD5AndDES", "dsaf#,jds.klfj1");
			// 使用指定的 jasypt 加密器进行解密
			assertThat(JasyptUtils.customPBEDecrypt(encrypted2, "PBEWithMD5AndDES", "dsaf#,jds.klfj1"))
				.isEqualTo(plainText);
		}
	}

}
