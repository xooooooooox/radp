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

package space.x9x.radp.spring.boot.bootstrap;

import java.util.Set;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentOutboundParser;
import space.x9x.radp.spring.boot.bootstrap.util.SpringBootProfileUtils;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.framework.bootstrap.utils.SpringProfileUtils;

/**
 * Helper utility for Spring Boot application initialization and configuration. This
 * utility class provides methods for running Spring Boot applications with appropriate
 * configuration, setting system properties required by various frameworks, and logging
 * application startup information including inbound and outbound connection details.
 *
 * @author x9x
 * @since 2024-09-28 21:33
 */
@Slf4j
@UtilityClass
public class SpringBootApplicationHelper {

	/**
	 * Runs a Spring Boot application with the specified main class, arguments, and web
	 * application type. This method handles the complete application startup process,
	 * including - Setting necessary system properties - Configuring the application with
	 * default profiles - Starting the application context - Logging application startup
	 * information
	 * @param mainClass the main application class that serves as the primary
	 * configuration source
	 * @param args the command line arguments passed to the application
	 * @param webApplicationType the type of web application to create (SERVLET, REACTIVE,
	 * or NONE)
	 */
	public static void run(Class<?> mainClass, String[] args, WebApplicationType webApplicationType) {
		setSystemProperties();
		try {
			SpringApplication app = new SpringApplicationBuilder(mainClass).web(webApplicationType).build();
			SpringBootProfileUtils.addDefaultProfile(app);

			ConfigurableApplicationContext context = app.run(args);
			ConfigurableEnvironment env = context.getEnvironment();
			logAfterRunning(env);
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Sets system properties required by various frameworks. This method configures
	 * system properties to fix common issues with: - Elasticsearch (preventing processor
	 * count conflicts) - Dubbo (ensuring IPv4 stack preference) - ZooKeeper (increasing
	 * buffer size to prevent IO exceptions)
	 */
	private static void setSystemProperties() {
		// Fixed elasticsearch error: availableProcessors is already set to [], rejecting
		// []
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		// Fixed dubbo error: No such any registry to refer service in consumer
		// xxx.xxx.xxx.xxx use dubbo version 2.x.x
		System.setProperty("java.net.preferIPv4Stack", "true");

		// Fixed zookeeper error: java.io.IOException: Unreasonable length = 1048575
		System.setProperty("jute.maxbuffer", String.valueOf(8192 * 1024));
	}

	/**
	 * Logs application startup information after the application has started. This method
	 * logs the application name, active profiles, and both inbound and outbound
	 * connection information to provide a comprehensive overview of the running
	 * application.
	 * @param env the Spring environment containing application configuration
	 */
	private static void logAfterRunning(Environment env) {
		String appName = StringUtil.trimToEmpty(env.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
		String profile = String.join(Strings.COMMA, SpringProfileUtils.getActiveProfiles(env));
		log.info("\n----------------------------------------------------------\n" + "Application '{}' is running!\n"
				+ "Profile(s):\t{}\n" + getInboundInfo(env) + getOutboundInfo(env)
				+ "----------------------------------------------------------", appName, profile);
	}

	/**
	 * Collects inbound connection information from all available EnvironmentInboundParser
	 * extensions. This method loads all EnvironmentInboundParser implementations using
	 * the ExtensionLoader and aggregates their output to provide comprehensive
	 * information about inbound connections.
	 * @param env the Spring environment containing application configuration
	 * @return a string containing all inbound connection information
	 */
	private String getInboundInfo(Environment env) {
		ExtensionLoader<EnvironmentInboundParser> extensionLoader = ExtensionLoader
			.getExtensionLoader(EnvironmentInboundParser.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		StringBuilder logStr = new StringBuilder();
		for (String extension : extensions) {
			String info = extensionLoader.getExtension(extension).toString(env);
			if (StringUtil.isBlank(info)) {
				continue;
			}
			logStr.append(info).append("\n");
		}
		return logStr.toString();
	}

	/**
	 * Collects outbound connection information from all available
	 * EnvironmentOutboundParser extensions. This method loads all
	 * EnvironmentOutboundParser implementations using the ExtensionLoader and aggregates
	 * their output to provide comprehensive information about outbound connections.
	 * @param env the Spring environment containing application configuration
	 * @return a string containing all outbound connection information
	 */
	private String getOutboundInfo(Environment env) {
		ExtensionLoader<EnvironmentOutboundParser> extensionLoader = ExtensionLoader
			.getExtensionLoader(EnvironmentOutboundParser.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		StringBuilder logStr = new StringBuilder();
		for (String extension : extensions) {
			String info = extensionLoader.getExtension(extension).toString(env);
			if (StringUtil.isBlank(info)) {
				continue;
			}
			logStr.append(info).append("\n");
		}
		return logStr.toString();
	}

}
