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

package space.x9x.radp.spring.boot.beans.autoconfigure;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

/**
 * Auto-configuration for RADP error messages.
 * <p>
 * It defines a dedicated {@link MessageSource} that:
 * <ul>
 * <li>Loads all META-INF/error/message*.properties from the classpath (including
 * libs).</li>
 * <li>Loads META-INF/error/internal*.properties as the ultimate fallback.</li>
 * <li>Uses Spring's i18n mechanism to resolve messages by Locale.</li>
 * </ul>
 * <p>
 * Once the MessageSource is created, it is registered into {@link ErrorCodeLoader}, so
 * all static usages of ErrorCodeLoader transparently benefit from i18n and deterministic
 * precedence.
 *
 * @author x9x
 * @since 2025-11-28 20:41
 */
@AutoConfiguration
@ConditionalOnClass(MessageSource.class)
public class ErrorMessageAutoConfiguration {

	/**
	 * Main {@link MessageSource} for RADP error codes.
	 * <p>
	 * Precedence and merge:
	 * <ol>
	 * <li>{@code classpath:/META-INF/error/message} – typical app resource.</li>
	 * <li>{@code classpath*:/META-INF/error/message} – merged resources from all
	 * jars.</li>
	 * <li>{@code classpath:/META-INF/error/internal} – framework internal defaults.</li>
	 * </ol>
	 * <p>
	 * Later basename take lower priority when resolving a single key: Spring will try
	 * them in the declared order and return the first match. That gives: <pre>
	 *     app > libs > internal
	 * </pre>
	 */
	@Bean(name = "radpErrorMessageSource")
	@ConditionalOnMissingBean(name = "radpErrorMessageSource")
	public MessageSource radpErrorMessageSource() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasenames(
				// Application-local messages (single resource, not classpath*)
				"classpath:/META-INF/error/message",
				// All message*.properties on classpath (dependencies, libs, etc.)
				"classpath*:/META-INF/error/message",
				// Internal fallback bundle
				"classpath:/META-INF/error/internal");
		ms.setDefaultEncoding(StandardCharsets.UTF_8.name());
		// If no key is found anywhere, use the code itself as the default message
		ms.setUseCodeAsDefaultMessage(true);
		// Don't auto-adjust to the system default locale – rely on LocaleResolver /
		// LocaleContext
		ms.setFallbackToSystemLocale(false);
		// Reasonable default: in dev you can change it via properties if you want "hot
		// reload"
		ms.setCacheSeconds(-1); // forever; override in-app config if needed

		// Register into ErrorCodeLoader so that all static calls use this MessageSource
		ErrorCodeLoader.setMessageSource(ms);
		return ms;
	}

}
