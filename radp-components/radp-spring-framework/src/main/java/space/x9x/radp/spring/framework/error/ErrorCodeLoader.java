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

package space.x9x.radp.spring.framework.error;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.PropertyKey;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import space.x9x.radp.commons.lang.format.MessageFormatter;
import space.x9x.radp.spring.framework.beans.ApplicationContextHelper;

/**
 * Utility class for loading error codes and messages from resource bundles. This class
 * provides methods to retrieve error messages based on error codes, with support for both
 * application-specific and internal framework error messages.
 * <p>
 * Resolution strategy:
 * <ol>
 * <li>If a Spring {@link MessageSource} is available, use it (supports full i18n and
 * deterministic precedence configured by the application).</li>
 * <li>Otherwise, merge all {@code META-INF/error/message*.properties} resources on the
 * classpath into {@code APP_MESSAGES} (libraries first, then application classes), and
 * resolve from there.</li>
 * <li>If still not found, fall back to the internal bundle
 * {@code META-INF.error.internal}.</li>
 * <li>Finally, if no message is found anywhere, return {@code errCode} itself (or use it
 * as a formatting pattern for parameterized lookups).</li>
 * </ol>
 *
 * This gives you:
 * <ul>
 * <li>Library modules (e.g. radp-spring-security) can ship default error codes.</li>
 * <li>Applications can add or override individual codes without copying entire
 * files.</li>
 * <li>Internal codes remain as a last resort.</li>
 * <li>Works both with and without Spring.</li>
 * </ul>
 *
 * @author x9x
 * @since 2024-09-26 16:14
 */
@Slf4j
@UtilityClass
public class ErrorCodeLoader {

	/**
	 * Resource bundle name for internal error messages. This constant defines the path to
	 * the internal resource bundle that contains framework-provided error messages used
	 * as a fallback when application-specific error messages are not available.
	 */
	public static final String INTERNAL_BUNDLE_NAME = "META-INF.error.internal";

	/**
	 * Resource bundle name for application error messages. This constant defines the path
	 * to the resource bundle that contains application-specific error messages.
	 */
	public static final String BUNDLE_NAME = "META-INF.error.message";

	/**
	 * The internal resource bundle containing framework-provided error messages. This
	 * bundle is always loaded and used as a fallback when application-specific error
	 * messages are not available.
	 */
	private static final ResourceBundle INTERNAL_RESOURCE_BUNDLE = ResourceBundle.getBundle(INTERNAL_BUNDLE_NAME,
			Locale.SIMPLIFIED_CHINESE);

	/**
	 * All application-level error messages loaded from every
	 * META-INF/error/message*.properties resource on the classpath. Later resources
	 * override earlier ones on key conflicts.
	 *
	 * <p>
	 * NOTE: This map is lazily populated on first use when no Spring MessageSource is
	 * available, to avoid unnecessary work in pure Spring Boot + MessageSource setups.
	 * </p>
	 */
	private static final Map<String, String> APP_MESSAGES = new ConcurrentHashMap<>();

	/**
	 * Flag indicating whether APP_MESSAGES have been initialized.
	 */
	private static volatile boolean appMessagesLoaded = false;

	/**
	 * Optional Spring MessageSource for i18n and deterministic merge precedence. If
	 * present in the application context, we prefer using it to resolve messages. --
	 * SETTER -- Register a Spring MessageSource to enable i18n-driven resolution. This
	 * method is intended to be called from an auto-configuration or application bootstrap
	 * code.
	 */
	@Setter
	private static volatile MessageSource messageSource;

	// ====================================================================================
	// Resource merging: classpath META-INF/error/message*.properties
	// ====================================================================================

	/**
	 * Lazily loads application error messages into {@link #APP_MESSAGES}. This method is
	 * thread-safe and will only perform the merge once per JVM.
	 */
	private static void ensureAppMessagesLoaded() {
		if (appMessagesLoaded) {
			return;
		}
		synchronized (APP_MESSAGES) {
			if (appMessagesLoaded) {
				return;
			}
			loadApplicationMessages();
			appMessagesLoaded = true;
		}
	}

	private static void loadApplicationMessages() {
		// baseName: META-INF.error.message -> path: META-INF/error/message
		String basePath = BUNDLE_NAME.replace('.', '/');
		ClassLoader cl = getClassLoader();

		// You can extend these suffixes if you want more locale-specific variants.
		String[] suffixes = { "", "_zh_CN" };

		for (String suffix : suffixes) {
			String resourcePath = basePath + suffix + ".properties";
			try {
				Enumeration<URL> urls = (cl != null) ? cl.getResources(resourcePath)
						: ClassLoader.getSystemResources(resourcePath);

				if (urls == null || !urls.hasMoreElements()) {
					continue;
				}

				// To make "app overrides libs" deterministic, we sort URLs:
				// precedence 0: jars (dependencies)
				// precedence 1: generic classes dirs
				// precedence 2: app classes (target/build/BOOT-INF/classes) – loaded last
				List<URL> list = Collections.list(urls);
				list.sort((u1, u2) -> Integer.compare(precedence(u1), precedence(u2)));
				for (URL url : list) {
					loadPropertiesFromUrl(url, APP_MESSAGES, true);
				}
			}
			catch (IOException ex) {
				log.error("Failed to load error message resource: {}", resourcePath, ex);
			}
		}

		if (APP_MESSAGES.isEmpty()) {
			log.warn("No application error messages found for base path: {}", basePath);
		}
		else {
			log.debug("Loaded {} application error messages for base path: {}", APP_MESSAGES.size(), basePath);
		}
	}

	private static ClassLoader getClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) {
			cl = ErrorCodeLoader.class.getClassLoader();
		}
		return cl;
	}

	/**
	 * 从指定 URL 加载 properties，并合并到 targetMap 中.
	 * @param url 资源 URL
	 * @param targetMap 目标 Map
	 * @param override 是否允许后加载的值覆盖先前相同 key 的值
	 */
	private static void loadPropertiesFromUrl(URL url, Map<String, String> targetMap, boolean override) {
		Properties props = new Properties();
		try (InputStream is = url.openStream()) {
			props.load(is);
		}
		catch (IOException ex) {
			log.error("Failed to load error message properties from url: {}", url, ex);
			return;
		}

		for (String key : props.stringPropertyNames()) {
			String value = props.getProperty(key);
			if (value == null) {
				continue;
			}
			if (override || !targetMap.containsKey(key)) {
				// 后加载的资源可以覆盖前面相同 key 的值
				targetMap.put(key, value);
			}
		}
	}

	/**
	 * Heuristic precedence for resource URLs to achieve "app overrides libs" behavior.
	 * Lower number means loaded earlier (thus can be overridden by later ones).
	 */
	private static int precedence(URL url) {
		String u = url.toString();
		// JAR entries (dependencies)
		if (u.contains("!/") || u.endsWith(".jar") || u.contains("/BOOT-INF/lib/")) {
			return 0;
		}
		// App classes directories (typical build output)
		if (u.contains("/target/classes/") || u.contains("/build/classes/") || u.contains("/BOOT-INF/classes/")) {
			return 2;
		}
		// Other exploded classes (e.g., IDE out) as middle
		return 1;
	}

	// ====================================================================================
	// Public API: message resolution
	// ====================================================================================

	/**
	 * Get the error message for the given error code without any parameter replacement.
	 * This is useful when you want to get the raw message template without any
	 * placeholder replacement.
	 * @param errCode the error code
	 * @return the error message
	 */
	public static String getErrMessage(@PropertyKey(resourceBundle = BUNDLE_NAME) String errCode) {
		// 0. Prefer Spring MessageSource if available
		MessageSource ms = getMessageSource();
		if (ms != null) {
			Locale locale = LocaleContextHolder.getLocale();
			return ms.getMessage(errCode, null, errCode, locale);
		}

		// 1. Load & query merged application messages
		ensureAppMessagesLoaded();
		String message = APP_MESSAGES.get(errCode);
		if (message != null) {
			return message;
		}

		// 2. Fallback to the internal bundle
		if (INTERNAL_RESOURCE_BUNDLE.containsKey(errCode)) {
			return INTERNAL_RESOURCE_BUNDLE.getString(errCode);
		}

		// 3. Last resort: return the code itself
		return errCode;
	}

	/**
	 * Get the error message for the given error code with parameter replacement. This
	 * will replace placeholders in the message template with the provided parameters.
	 * @param errCode the error code
	 * @param params the parameters to replace placeholders in the message template
	 * @return the formatted error message
	 */
	public static String getErrMessage(@PropertyKey(resourceBundle = BUNDLE_NAME) String errCode, Object... params) {
		// 0. Prefer Spring MessageSource if available, but keep '{}' style formatting
		MessageSource ms = getMessageSource();
		if (ms != null) {
			Locale locale = LocaleContextHolder.getLocale();
			String pattern = ms.getMessage(errCode, null, errCode, locale);
			return MessageFormatter.arrayFormat(pattern, params).getMessage();
		}

		// 1. Load & query merged application messages
		ensureAppMessagesLoaded();
		String pattern = APP_MESSAGES.get(errCode);
		if (pattern != null) {
			return MessageFormatter.arrayFormat(pattern, params).getMessage();
		}

		// 2. Fallback to the internal bundle
		if (INTERNAL_RESOURCE_BUNDLE.containsKey(errCode)) {
			pattern = INTERNAL_RESOURCE_BUNDLE.getString(errCode);
			return MessageFormatter.arrayFormat(pattern, params).getMessage();
		}

		// 3. If no message is found anywhere, use errCode as the pattern itself
		return MessageFormatter.arrayFormat(errCode, params).getMessage();
	}

	/**
	 * Get the error message with explicit Locale selection. If Spring MessageSource is
	 * available, it will be used; otherwise, it falls back to internal merged maps.
	 * @param errCode the error code
	 * @param locale target locale
	 * @param params parameters for placeholder replacement
	 * @return formatted error message
	 */
	public static String getErrMessage(@PropertyKey(resourceBundle = BUNDLE_NAME) String errCode, Locale locale,
			Object... params) {
		MessageSource ms = getMessageSource();
		if (ms != null) {
			String pattern = ms.getMessage(errCode, null, errCode, locale);
			return MessageFormatter.arrayFormat(pattern, params).getMessage();
		}
		// Fallback to default behavior ignoring locale
		return getErrMessage(errCode, params);
	}

	private static MessageSource getMessageSource() {
		// Fast path if already set
		MessageSource ms = messageSource;
		if (ms != null) {
			return ms;
		}
		// Try to lazily resolve from Spring context if available
		try {
			ms = ApplicationContextHelper.getBean(MessageSource.class);
		}
		catch (Throwable ignore) {
			// ignore any errors – operate without Spring
		}
		if (ms != null) {
			messageSource = ms; // cache for future calls
		}
		return ms;
	}

}
