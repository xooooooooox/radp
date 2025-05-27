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

package space.x9x.radp.jasypt.spring.boot.env;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/**
 * Extended Jasypt Properties implementation. This class extends Java's Properties class
 * to provide transparent decryption of encrypted property values using Jasypt. It
 * configures a Jasypt encryptor with settings loaded from various sources in priority
 * order: system properties, environment variables, and external configuration files.
 *
 * @author IO x9x
 * @since 2024-10-17 10:29
 */
@Slf4j
public class ExtendJasyptProperties extends Properties {

	private static final long serialVersionUID = 1L;

	/**
	 * Property key for the Jasypt encryptor password. This password is used for
	 * encryption and decryption operations.
	 */
	private static final String PASSWORD_KEY = "jasypt.encryptor.password";

	/**
	 * Property key for the Jasypt encryption algorithm. Specifies which algorithm to use
	 * for encryption and decryption.
	 */
	private static final String ALGORITHM_KEY = "jasypt.encryptor.algorithm";

	/**
	 * System property name for specifying an external Jasypt configuration file. If set,
	 * the application will load Jasypt configuration from this file.
	 */
	private static final String CONFIG_FILE_PROPERTY = "jasypt.config.file";

	/**
	 * Environment variable name for specifying an external Jasypt configuration file.
	 * Alternative to the system property, used if the system property is not set.
	 */
	private static final String CONFIG_FILE_ENV = "JASYPT_CONFIG_FILE";

	/**
	 * Constructs a new ExtendJasyptProperties with an initialized encryptor. This
	 * constructor sets up a Jasypt encryptor with configuration loaded from various
	 * sources in the following priority order: 1. System properties 2. Environment
	 * variables 3. External configuration file (if specified)
	 * <p>
	 * The constructor configures a StandardPBEStringEncryptor with the password and
	 * algorithm settings, then initializes EncryptableProperties with this encryptor to
	 * enable transparent decryption of encrypted property values.
	 */
	public ExtendJasyptProperties() {
		// 加载外部配置文件(如果指定)
		Properties externalConfig = loadExternalConfig();

		// 按优先级获取密码和算法
		String algorithm = getPropertyValue(ALGORITHM_KEY, externalConfig);
		String password = getPropertyValue(PASSWORD_KEY, externalConfig);

		// 配置加解密器
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(password);
		encryptor.setAlgorithm(algorithm);
		super.defaults = new EncryptableProperties(encryptor);
	}

	/**
	 * 按照优先级获取属性值 (系统属性 > 环境变量 > 外部配置文件) 一旦获取到就不会再去读取低优先级的值.
	 * @param key 属性键
	 * @param externalConfig 外部配置文件的 Properties 对象
	 * @return 属性值, 若不存在则返回 null
	 */
	private String getPropertyValue(String key, Properties externalConfig) {
		// 1. 系统属性
		String value = System.getProperty(key);
		if (value != null && !value.isEmpty()) {
			return value;
		}

		// 2. 环境变量 (将 key 转换为大写并替换 '.' 为 '_', 如 jasypt.encryptor.password ->
		// JASYPT_ENCRYPTOR_PASSWORD)
		String envKey = key.toUpperCase().replace('.', '_');
		value = System.getenv(envKey);
		if (value != null && !value.isEmpty()) {
			return value;
		}

		// 3. 外部配置文件
		return externalConfig.getProperty(key);
	}

	/**
	 * 加载外部配置文件. (如果通过系统属性或环境变量指定了配置文件路径)
	 * @return 外部配置文件的 Properties 对象,如果未指定则返回空的 Properties 对象
	 */
	private static Properties loadExternalConfig() {
		Properties config = new Properties();
		String configFilePath = System.getProperty(CONFIG_FILE_PROPERTY);
		if (configFilePath == null) {
			configFilePath = System.getenv(CONFIG_FILE_ENV);
		}
		if (configFilePath != null) {
			try (FileInputStream fis = new FileInputStream(configFilePath)) {
				config.load(fis);
			}
			catch (IOException ex) {
				log.error("如果通过 -Djasypt.encryptor.password 或 环境变量定义了 jasypt password 或者 algorithm, 那么可以忽略下面这条报错");
				log.error("Failed to load Jasypt properties file {}", configFilePath, ex);
			}
		}
		return config;
	}

	@Override
	public synchronized Object put(Object key, Object value) {
		return super.defaults.put(key, value);
	}

}
