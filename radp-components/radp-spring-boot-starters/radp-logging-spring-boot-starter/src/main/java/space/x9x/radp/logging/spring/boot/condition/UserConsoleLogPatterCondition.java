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

package space.x9x.radp.logging.spring.boot.condition;

import ch.qos.logback.core.boolex.PropertyConditionBase;

/**
 * Resolve warning: {@code The 'condition' attribute in {@literal <if>} element is
 * deprecated and slated for removal.}
 * <p>
 * <a href="https://logback.qos.ch/codes.html"> Spring Boot 3.5.x upgrades Logback to a
 * version where the {@code condition="..."} attribute on {@literal <if>} is now
 * deprecated (because it relies on Janino to compile arbitrary Java expressions, which is
 * considered a security risk). </a>
 *
 * @author x9x
 * @since 2025-11-29 17:18
 */
public class UserConsoleLogPatterCondition extends PropertyConditionBase {

	@Override
	public boolean evaluate() {
		if (!isDefined("userConsoleLogPattern")) {
			return true;
		}
		String userConsoleLogPattern = property("userConsoleLogPattern");
		return userConsoleLogPattern != null && !userConsoleLogPattern.isEmpty();
	}

}
