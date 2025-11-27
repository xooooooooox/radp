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
 * 分页请求参数对象，包含页码与每页条数信息. 提供默认值与校验约束以确保分页行为正确。
 *
 * <p>
 * 功能特性：
 * <ul>
 * <li>支持从 1 开始的页码索引；</li>
 * <li>提供最小/最大分页边界常量，防止一次性加载过多数据；</li>
 * <li>支持使用 {@link #NO_PAGINATION} 表示不分页，直接返回全部结果（由上层调用方决定是否使用）。</li>
 * </ul>
 *
 * @author x9x
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
	 * 默认页码（从 1 开始计数).
	 */
	public static final int DEFAULT_PAGE_INDEX = 1;

	/**
	 * 默认每页条数.
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 允许的最小页码.
	 */
	public static final int MIN_PAGE_INDEX = 1;

	/**
	 * 允许的最小每页条数.
	 */
	public static final int MIN_PAGE_SIZE = 1;

	/**
	 * 允许的最大每页条数，用于限制单次数据量.
	 */
	public static final int MAX_PAGE_SIZE = 500;

	/**
	 * 禁用分页的特殊值（返回全部结果).
	 */
	public static final int NO_PAGINATION = -1;

	/**
	 * 当前页码（从 1 开始计数). 不得小于 {@link #MIN_PAGE_INDEX}.
	 */
	@Min(value = MIN_PAGE_INDEX, message = "页码最小值为 {value}")
	private Integer pageIndex = DEFAULT_PAGE_INDEX;

	/**
	 * 每页展示的条数. 应介于 {@link #MIN_PAGE_SIZE} 与 {@link #MAX_PAGE_SIZE} 之间，或可设置为
	 * {@link #NO_PAGINATION} 以禁用分页.
	 */
	@Min(value = MIN_PAGE_SIZE, message = "每页最小条数为 {value}")
	@Max(value = MAX_PAGE_SIZE, message = "每页最大条数为 {value}")
	private Integer pageSize = DEFAULT_PAGE_SIZE;

}
