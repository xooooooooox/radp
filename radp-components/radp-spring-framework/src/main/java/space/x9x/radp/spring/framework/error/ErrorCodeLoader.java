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

import java.util.Locale;
import java.util.ResourceBundle;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.PropertyKey;

import space.x9x.radp.commons.lang.format.MessageFormatter;

/**
 * Utility class for loading error codes and messages from resource bundles. This class
 * provides methods to retrieve error messages based on error codes, with support for both
 * application-specific and internal framework error messages.
 *
 * @author IO x9x
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
	 * to the resource bundle that contains application-specific error messages. If this
	 * bundle cannot be loaded, the system will fall back to using the internal bundle.
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
	 * The application resource bundle containing application-specific error messages.
	 * This bundle is loaded if available, otherwise the system falls back to the internal
	 * resource bundle.
	 */
	private static ResourceBundle resourceBundle;

	static {
		try {
			resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.SIMPLIFIED_CHINESE);
		}
		catch (Exception ex) {
			log.error("加载应用错误码文件失败 {}, 将回退使用框架内置错误码资源文件 {}", BUNDLE_NAME + ".properties",
					INTERNAL_BUNDLE_NAME + ".properties", ex);
			resourceBundle = ResourceBundle.getBundle(INTERNAL_BUNDLE_NAME, Locale.SIMPLIFIED_CHINESE);
		}
	}

	/**
	 * Get the error message for the given error code without any parameter replacement.
	 * This is useful when you want to get the raw message template without any
	 * placeholder replacement.
	 * @param errCode the error code
	 * @return the error message
	 */
	public static String getErrMessage(@PropertyKey(resourceBundle = BUNDLE_NAME) String errCode) {
		if (resourceBundle.containsKey(errCode)) {
			return resourceBundle.getString(errCode);
		}

		if (INTERNAL_RESOURCE_BUNDLE.containsKey(errCode)) {
			return INTERNAL_RESOURCE_BUNDLE.getString(errCode);
		}

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
		if (resourceBundle.containsKey(errCode)) {
			String messagePattern = resourceBundle.getString(errCode);
			return MessageFormatter.arrayFormat(messagePattern, params).getMessage();
		}

		if (INTERNAL_RESOURCE_BUNDLE.containsKey(errCode)) {
			String messagePattern = INTERNAL_RESOURCE_BUNDLE.getString(errCode);
			return MessageFormatter.arrayFormat(messagePattern, params).getMessage();
		}

		return MessageFormatter.arrayFormat(errCode, params).getMessage();
	}

}
