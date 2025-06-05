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

package space.x9x.radp.spring.framework.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Parameter object for pagination requests that contains page index and page size
 * information. This class provides default values and validation constraints to ensure
 * proper pagination behavior.
 *
 * @author IO x9x
 * @since 2024-09-26 20:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Default page index value (1-based indexing).
	 */
	public static final int DEFAULT_PAGE_INDEX = 1;

	/**
	 * Default number of items per page.
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * Minimum allowed page index value.
	 */
	public static final int MIN_PAGE_INDEX = 1;

	/**
	 * Minimum allowed page size value.
	 */
	public static final int MIN_PAGE_SIZE = 1;

	/**
	 * Maximum allowed page size value to prevent excessive data loading.
	 */
	public static final int MAX_PAGE_SIZE = 500;

	/**
	 * Special value indicating that pagination should be disabled (return all results).
	 */
	public static final int PAGE_SIZE_NONE = -1;

	/**
	 * The current page number (1-based indexing). Must be at least
	 * {@link #MIN_PAGE_INDEX}.
	 */
	@Min(value = MIN_PAGE_INDEX, message = "页码最小值为 {value}")
	private Integer pageIndex = DEFAULT_PAGE_INDEX;

	/**
	 * The number of items to display per page. Must be between {@link #MIN_PAGE_SIZE} and
	 * {@link #MAX_PAGE_SIZE}, or can be {@link #PAGE_SIZE_NONE} to disable pagination.
	 */
	@Min(value = MIN_PAGE_SIZE, message = "每页最小条数为 {value}")
	@Max(value = MAX_PAGE_SIZE, message = "每页最大条数为 {value}")
	private Integer pageSize = DEFAULT_PAGE_SIZE;

}
