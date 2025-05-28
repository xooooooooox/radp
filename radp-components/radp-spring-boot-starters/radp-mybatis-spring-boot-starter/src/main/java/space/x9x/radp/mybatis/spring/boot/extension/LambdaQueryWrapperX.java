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

import java.io.Serial;
import java.util.Collection;

import org.springframework.util.StringUtils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import space.x9x.radp.commons.lang.ArrayUtils;

/**
 * Extended MyBatis Plus LambdaQueryWrapper class with additional functionality.
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
 * @since 2024-11-20 15:49
 * @param <T> the entity type that this wrapper operates on
 */
public class LambdaQueryWrapperX<T> extends LambdaQueryWrapper<T> {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Adds a LIKE condition to the query only if the value is not null or empty. This
	 * method only applies the condition when the provided string has text.
	 * @param column the column to apply the LIKE condition to, specified as a lambda
	 * expression
	 * @param val the value to match with LIKE
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, String val) {
		if (StringUtils.hasText(val)) {
			return (LambdaQueryWrapperX<T>) super.like(column, val);
		}
		return this;
	}

	/**
	 * Adds an IN condition to the query only if the collection of values is not null or
	 * empty. This method only applies the condition when the provided collection contains
	 * elements.
	 * @param column the column to apply the IN condition to, specified as a lambda
	 * expression
	 * @param values the collection of values to match with IN
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
		if (ObjectUtil.isAllNotEmpty(values) && !ArrayUtil.isEmpty(values)) {
			return (LambdaQueryWrapperX<T>) super.in(column, values);
		}
		return this;
	}

	/**
	 * Adds an IN condition to the query only if the array of values is not null or empty.
	 * This method only applies the condition when the provided array contains elements.
	 * @param column the column to apply the IN condition to, specified as a lambda
	 * expression
	 * @param values the array of values to match with IN
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
		if (ObjectUtil.isAllNotEmpty(values) && !ArrayUtil.isEmpty(values)) {
			return (LambdaQueryWrapperX<T>) super.in(column, values);
		}
		return this;
	}

	/**
	 * Adds an equality (=) condition to the query only if the value is not null or empty.
	 * This method only applies the condition when the provided value is present.
	 * @param column the column to apply the equality condition to, specified as a lambda
	 * expression
	 * @param val the value to match with equality
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
		if (ObjectUtil.isNotEmpty(val)) {
			return (LambdaQueryWrapperX<T>) super.eq(column, val);
		}
		return this;
	}

	/**
	 * Adds a not-equal (!=) condition to the query only if the value is not null or
	 * empty. This method only applies the condition when the provided value is present.
	 * @param column the column to apply the not-equal condition to, specified as a lambda
	 * expression
	 * @param val the value to compare with not-equal
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
		if (ObjectUtil.isNotEmpty(val)) {
			return (LambdaQueryWrapperX<T>) super.ne(column, val);
		}
		return this;
	}

	/**
	 * Adds a greater-than (&gt;) condition to the query only if the value is not null.
	 * This method only applies the condition when the provided value is present.
	 * @param column the column to apply the greater-than condition to, specified as a
	 * lambda expression
	 * @param val the value to compare with greater-than
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
		if (val != null) {
			return (LambdaQueryWrapperX<T>) super.gt(column, val);
		}
		return this;
	}

	/**
	 * Adds a greater-than-or-equal (&gt;=) condition to the query only if the value is
	 * not null. This method only applies the condition when the provided value is
	 * present.
	 * @param column the column to apply the greater-than-or-equal condition to, specified
	 * as a lambda expression
	 * @param val the value to compare with greater-than-or-equal
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
		if (val != null) {
			return (LambdaQueryWrapperX<T>) super.ge(column, val);
		}
		return this;
	}

	/**
	 * Adds a less-than (&lt;) condition to the query only if the value is not null. This
	 * method only applies the condition when the provided value is present.
	 * @param column the column to apply the less-than condition to, specified as a lambda
	 * expression
	 * @param val the value to compare with less-than
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
		if (val != null) {
			return (LambdaQueryWrapperX<T>) super.lt(column, val);
		}
		return this;
	}

	/**
	 * Adds a less-than-or-equal (&lt;=) condition to the query only if the value is not
	 * null. This method only applies the condition when the provided value is present.
	 * @param column the column to apply the less-than-or-equal condition to, specified as
	 * a lambda expression
	 * @param val the value to compare with less-than-or-equal
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
		if (val != null) {
			return (LambdaQueryWrapperX<T>) super.le(column, val);
		}
		return this;
	}

	/**
	 * Adds a BETWEEN condition to the query based on the presence of values. This method
	 * intelligently handles different scenarios: - If both values are present, adds a
	 * BETWEEN condition - If only the first value is present, adds a
	 * greater-than-or-equal condition - If only the second value is present, adds a
	 * less-than-or-equal condition - If neither value is present, adds no condition
	 * @param column the column to apply the condition to, specified as a lambda
	 * expression
	 * @param val1 the lower bound value (can be null)
	 * @param val2 the upper bound value (can be null)
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
		if (val1 != null && val2 != null) {
			return (LambdaQueryWrapperX<T>) super.between(column, val1, val2);
		}
		if (val1 != null) {
			return (LambdaQueryWrapperX<T>) ge(column, val1);
		}
		if (val2 != null) {
			return (LambdaQueryWrapperX<T>) le(column, val2);
		}
		return this;
	}

	/**
	 * Adds a BETWEEN condition to the query using values from an array. This is a
	 * convenience method that extracts the first two elements from the array and
	 * delegates to the other betweenIfPresent method.
	 * @param column the column to apply the condition to, specified as a lambda
	 * expression
	 * @param values an array containing the lower and upper bound values (can be null or
	 * empty)
	 * @return the current wrapper instance for chaining
	 */
	public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object[] values) {
		Object val1 = ArrayUtils.get(values, 0);
		Object val2 = ArrayUtils.get(values, 1);
		return betweenIfPresent(column, val1, val2);
	}

	// ========== Overridden Parent Methods for Method Chaining ==========

	/**
	 * Overridden method to add an equality condition to the query. This override ensures
	 * proper return type for method chaining.
	 * @param condition whether to add this condition
	 * @param column the entity field to compare
	 * @param val the value to compare with
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public LambdaQueryWrapperX<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
		super.eq(condition, column, val);
		return this;
	}

	/**
	 * Overridden method to add an equality condition to the query. This override ensures
	 * proper return type for method chaining.
	 * @param column the entity field to compare
	 * @param val the value to compare with
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public LambdaQueryWrapperX<T> eq(SFunction<T, ?> column, Object val) {
		super.eq(column, val);
		return this;
	}

	/**
	 * Overridden method to add a descending order by clause to the query. This override
	 * ensures proper return type for method chaining.
	 * @param column the entity field to order by
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public LambdaQueryWrapperX<T> orderByDesc(SFunction<T, ?> column) {
		super.orderByDesc(true, column);
		return this;
	}

	/**
	 * Overridden method to append a custom SQL fragment at the end of the query. This
	 * override ensures proper return type for method chaining.
	 * @param lastSql the SQL fragment to append
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public LambdaQueryWrapperX<T> last(String lastSql) {
		super.last(lastSql);
		return this;
	}

	/**
	 * Overridden method to add an IN condition to the query. This override ensures proper
	 * return type for method chaining.
	 * @param column the entity field to compare
	 * @param coll the collection of values to compare with
	 * @return this wrapper instance for method chaining
	 */
	@Override
	public LambdaQueryWrapperX<T> in(SFunction<T, ?> column, Collection<?> coll) {
		super.in(column, coll);
		return this;
	}

}
