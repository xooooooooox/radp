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

package space.x9x.radp.jasypt.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import space.x9x.radp.jasypt.spring.boot.encryptor.Sm4StringEncryptor;

/**
 * Autoconfiguration for Jasypt StringEncryptor.
 *
 * <p>
 * Behavior: - If property `jasypt.encryptor.algorithm` starts with "SM4"
 * (case-insensitive), a {@link Sm4StringEncryptor} will be created using provided
 * password and algorithm. - Otherwise, falls back to {@link StandardPBEStringEncryptor}
 * with the given algorithm/password so default Jasypt PBE keeps working.
 * <p>
 * The bean is registered with the standard name "jasyptStringEncryptor" so that
 * jasypt-spring-boot can pick it up automatically without any extra configuration.
 * <p>
 * Users only need to set: jasypt.encryptor.password=yourSecret
 * jasypt.encryptor.algorithm=SM4 (or SM4/CBC/PKCS5Padding, SM4/ECB/PKCS5Padding)
 *
 * @author x9x
 * @since 2025-11-07
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(StringEncryptor.class)
public class JasyptAutoConfiguration {

	/**
	 * Creates the primary {@link StringEncryptor} bean. Chooses SM4 or standard Jasypt
	 * PBE based on the 'jasypt.encryptor.algorithm' property.
	 * @param environment spring environment to read encryption properties
	 * @return configured {@link StringEncryptor} instance
	 */
	@Bean(name = "jasyptStringEncryptor")
	@ConditionalOnMissingBean(name = "jasyptStringEncryptor")
	@Primary
	public StringEncryptor jasyptStringEncryptor(Environment environment) {
		String algorithm = environment.getProperty("jasypt.encryptor.algorithm", "SM4");
		String password = environment.getProperty("jasypt.encryptor.password", "");

		if (algorithm.trim().toUpperCase().startsWith("SM4")) {
			log.info("Using SM4 StringEncryptor with algorithm='{}'", algorithm);
			return new Sm4StringEncryptor(algorithm, password);
		}

		log.info("Using StandardPBEStringEncryptor with algorithm='{}'", algorithm);
		StandardPBEStringEncryptor pbe = new StandardPBEStringEncryptor();
		pbe.setPassword(password);
		if (!algorithm.isEmpty()) {
			pbe.setAlgorithm(algorithm);
		}
		return pbe;
	}

}
