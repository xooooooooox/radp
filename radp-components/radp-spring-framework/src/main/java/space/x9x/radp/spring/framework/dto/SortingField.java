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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a field to sort by and its sort direction. This class is used in conjunction
 * with SortablePageParam to specify sorting criteria for paginated queries.
 *
 * @author IO x9x
 * @since 2024-12-24 14:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortingField implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Constant representing ascending sort order.
	 */
	public static final String ASC = "ASC";

	/**
	 * Constant representing descending sort order.
	 */
	public static final String DESC = "DESC";

	/**
	 * The name of the field to sort by. This corresponds to a property name in the entity
	 * being sorted.
	 */
	private String field;

	/**
	 * The sort direction for the field. Should be one of the constants ASC or DESC.
	 */
	private String order;

}
