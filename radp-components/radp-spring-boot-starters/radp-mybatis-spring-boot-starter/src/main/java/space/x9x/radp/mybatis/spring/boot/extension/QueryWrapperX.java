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

package space.x9x.radp.mybatis.spring.boot.extension;

import java.util.Collection;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import org.springframework.util.StringUtils;

/**
 * Extended MyBatis Plus QueryWrapper class with additional functionality.
 * <p>
 * Main enhancements:
 * <ul>
 * <li>Adds conditional methods with {@code xxxIfPresent} naming pattern that only add
 * conditions to the query when the provided values are not null or empty</li>
 * </ul>
 * <p>
 * This wrapper simplifies building dynamic queries where conditions should only be
 * applied when the corresponding values are present.
 *
 * @author IO x9x
 * @since 2024-11-20 15:53
 * @param <T> the entity type that this wrapper operates on
 */
public class QueryWrapperX<T> extends QueryWrapper<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * Adds a LIKE condition to the query only if the value is not null or empty. This
	 * method only applies the condition when the provided string has text.
	 * @param column the column name to apply the LIKE condition to
	 * @param val the value to match with LIKE
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> likeIfPresent(String column, String val) {
		if (StringUtils.hasText(val)) {
			return (QueryWrapperX<T>) super.like(column, val);
		}
		return this;
	}

	/**
	 * Adds an IN condition to the query only if the collection of values is not null or
	 * empty. This method only applies the condition when the provided collection contains
	 * elements.
	 * @param column the column name to apply the IN condition to
	 * @param values the collection of values to match with IN
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> inIfPresent(String column, Collection<?> values) {
		if (!CollectionUtils.isEmpty(values)) {
			return (QueryWrapperX<T>) super.in(column, values);
		}
		return this;
	}

	/**
	 * Adds an IN condition to the query only if the array of values is not null or empty.
	 * This method only applies the condition when the provided array contains elements.
	 * @param column the column name to apply the IN condition to
	 * @param values the array of values to match with IN
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> inIfPresent(String column, Object... values) {
		if (!ArrayUtils.isEmpty(values)) {
			return (QueryWrapperX<T>) super.in(column, values);
		}
		return this;
	}

	/**
	 * Adds an equality (=) condition to the query only if the value is not null. This
	 * method only applies the condition when the provided value is present.
	 * @param column the column name to apply the equality condition to
	 * @param val the value to match with equality
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> eqIfPresent(String column, Object val) {
		if (val != null) {
			return (QueryWrapperX<T>) super.eq(column, val);
		}
		return this;
	}

	/**
	 * Adds a not-equal (!=) condition to the query only if the value is not null. This
	 * method only applies the condition when the provided value is present.
	 * @param column the column name to apply the not-equal condition to
	 * @param val the value to compare with not-equal
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> neIfPresent(String column, Object val) {
		if (val != null) {
			return (QueryWrapperX<T>) super.ne(column, val);
		}
		return this;
	}

	/**
	 * Adds a greater-than (&gt;) condition to the query only if the value is not null.
	 * This method only applies the condition when the provided value is present.
	 * @param column the column name to apply the greater-than condition to
	 * @param val the value to compare with greater-than
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> gtIfPresent(String column, Object val) {
		if (val != null) {
			return (QueryWrapperX<T>) super.gt(column, val);
		}
		return this;
	}

	/**
	 * Adds a greater-than-or-equal (&gt;=) condition to the query only if the value is
	 * not null. This method only applies the condition when the provided value is
	 * present.
	 * @param column the column name to apply the greater-than-or-equal condition to
	 * @param val the value to compare with greater-than-or-equal
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> geIfPresent(String column, Object val) {
		if (val != null) {
			return (QueryWrapperX<T>) super.ge(column, val);
		}
		return this;
	}

	/**
	 * Adds a less-than (&lt;) condition to the query only if the value is not null. This
	 * method only applies the condition when the provided value is present.
	 * @param column the column name to apply the less-than condition to
	 * @param val the value to compare with less-than
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> ltIfPresent(String column, Object val) {
		if (val != null) {
			return (QueryWrapperX<T>) super.lt(column, val);
		}
		return this;
	}

	/**
	 * Adds a less-than-or-equal (&lt;=) condition to the query only if the value is not
	 * null. This method only applies the condition when the provided value is present.
	 * @param column the column name to apply the less-than-or-equal condition to
	 * @param val the value to compare with less-than-or-equal
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> leIfPresent(String column, Object val) {
		if (val != null) {
			return (QueryWrapperX<T>) super.le(column, val);
		}
		return this;
	}

	/**
	 * Adds a BETWEEN condition to the query only if one or both values are not null. This
	 * method intelligently applies different conditions based on which values are
	 * present: - If both values are present, applies a BETWEEN condition - If only the
	 * first value is present, applies a greater-than-or-equal condition - If only the
	 * second value is present, applies a less-than-or-equal condition - If neither value
	 * is present, no condition is applied
	 * @param column the column name to apply the condition to
	 * @param val1 the lower bound value (can be null)
	 * @param val2 the upper bound value (can be null)
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> betweenIfPresent(String column, Object val1, Object val2) {
		if (val1 != null && val2 != null) {
			return (QueryWrapperX<T>) super.between(column, val1, val2);
		}
		if (val1 != null) {
			return (QueryWrapperX<T>) ge(column, val1);
		}
		if (val2 != null) {
			return (QueryWrapperX<T>) le(column, val2);
		}
		return this;
	}

	/**
	 * Adds a BETWEEN condition to the query using an array of values. This method
	 * intelligently applies different conditions based on which array values are present:
	 * - If both array values are present, applies a BETWEEN condition - If only the first
	 * array value is present, applies a greater-than-or-equal condition - If only the
	 * second array value is present, applies a less-than-or-equal condition - If the
	 * array is null, empty, or neither value is present, no condition is applied
	 * @param column the column name to apply the condition to
	 * @param values an array containing the lower bound at index 0 and upper bound at
	 * index 1
	 * @return the current wrapper instance for chaining
	 */
	public QueryWrapperX<T> betweenIfPresent(String column, Object[] values) {
		if (values != null && values.length != 0 && values[0] != null && values[1] != null) {
			return (QueryWrapperX<T>) super.between(column, values[0], values[1]);
		}
		if (values != null && values.length != 0 && values[0] != null) {
			return (QueryWrapperX<T>) ge(column, values[0]);
		}
		if (values != null && values.length != 0 && values[1] != null) {
			return (QueryWrapperX<T>) le(column, values[1]);
		}
		return this;
	}

	// ========== Overridden Parent Methods for Method Chaining ==========

	/**
	 * Overridden method to add an equality condition to the query. This override ensures
	 * a proper return type for method chaining.
	 * @param condition whether to add this condition
	 * @param column the database column name to compare
	 * @param val the value to compare with
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public QueryWrapperX<T> eq(boolean condition, String column, Object val) {
		super.eq(condition, column, val);
		return this;
	}

	/**
	 * Overridden method to add an equality condition to the query. This override ensures
	 * a proper return type for method chaining.
	 * @param column the database column name to compare
	 * @param val the value to compare with
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public QueryWrapperX<T> eq(String column, Object val) {
		super.eq(column, val);
		return this;
	}

	/**
	 * Overridden method to add a descending order by clause to the query. This override
	 * ensures a proper return type for method chaining.
	 * @param column the database columns name to order by
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public QueryWrapperX<T> orderByDesc(String column) {
		super.orderByDesc(true, column);
		return this;
	}

	/**
	 * Overridden method to append a custom SQL fragment at the end of the query. This
	 * override ensures a proper return type for method chaining.
	 * @param lastSql the SQL fragment to append
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public QueryWrapperX<T> last(String lastSql) {
		super.last(lastSql);
		return this;
	}

	/**
	 * Overridden method to add an IN condition to the query. This override ensures a
	 * proper return type for method chaining.
	 * @param column the database column name to compare
	 * @param coll the collection of values to compare with
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public QueryWrapperX<T> in(String column, Collection<?> coll) {
		super.in(column, coll);
		return this;
	}

}
