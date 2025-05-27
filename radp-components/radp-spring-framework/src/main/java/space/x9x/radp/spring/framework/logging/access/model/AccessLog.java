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

package space.x9x.radp.spring.framework.logging.access.model;

import lombok.*;

import java.time.Instant;

/**
 * @author IO x9x
 * @since 2024-09-30 09:54
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessLog {

	private String location;

	private String arguments;

	private String returnValue;

	private Throwable throwable;

	private long duration;

	private transient Instant start;

	private transient Instant end;

}
