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

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import org.springframework.util.StringUtils;

import space.x9x.radp.commons.lang.ArrayUtil;

/**
 * 扩展的 MyBatis-Plus LambdaQueryWrapper，提供额外便捷能力.
 * <p>
 * 主要增强：
 * <ul>
 * <li>新增以 {@code xxxIfPresent} 命名的条件方法，仅在入参不为 null 且不为空时才向查询追加条件。</li>
 * </ul>
 * <p>
 * 用于简化动态查询的构建：只有当对应值存在时才应用条件。
 *
 * @author x9x
 * @since 2024-11-20 15:49
 * @param <T> 该包装器操作的实体类型
 */
public class LambdaQueryWrapperX<T> extends LambdaQueryWrapper<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * 仅当值非空时追加 LIKE 条件. 仅在提供的字符串包含文本时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param val 用于 LIKE 匹配的值
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, String val) {
		if (StringUtils.hasText(val)) {
			return (LambdaQueryWrapperX<T>) super.like(column, val);
		}
		return this;
	}

	/**
	 * 仅当集合不为空时追加 IN 条件. 只有当提供的集合包含元素时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param values 需要匹配的值集合
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
		if (ObjectUtil.isAllNotEmpty(values) && !cn.hutool.core.util.ArrayUtil.isEmpty(values)) {
			return (LambdaQueryWrapperX<T>) super.in(column, values);
		}
		return this;
	}

	/**
	 * 仅当数组不为空时追加 IN 条件. 只有当提供的数组包含元素时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param values 需要匹配的值数组
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
		if (ObjectUtil.isAllNotEmpty(values) && !cn.hutool.core.util.ArrayUtil.isEmpty(values)) {
			return (LambdaQueryWrapperX<T>) super.in(column, values);
		}
		return this;
	}

	/**
	 * 仅当值非空时追加等于（=）条件. 只有当提供的值存在时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param val 需要匹配的值
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
		if (ObjectUtil.isNotEmpty(val)) {
			return (LambdaQueryWrapperX<T>) super.eq(column, val);
		}
		return this;
	}

	/**
	 * 仅当值非空时追加不等于（!=）条件. 只有当提供的值存在时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param val 用于比较的不等值
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
		if (ObjectUtil.isNotEmpty(val)) {
			return (LambdaQueryWrapperX<T>) super.ne(column, val);
		}
		return this;
	}

	/**
	 * 仅当值非空时追加大于（&gt;）条件. 只有当提供的值存在时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param val 用于比较的值
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
		if (val != null) {
			return (LambdaQueryWrapperX<T>) super.gt(column, val);
		}
		return this;
	}

	/**
	 * 仅当值非空时追加大于等于（&gt;=）条件. 只有当提供的值存在时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param val 用于比较的值
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
		if (val != null) {
			return (LambdaQueryWrapperX<T>) super.ge(column, val);
		}
		return this;
	}

	/**
	 * 仅当值非空时追加小于（&lt;）条件. 只有当提供的值存在时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param val 用于比较的值
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
		if (val != null) {
			return (LambdaQueryWrapperX<T>) super.lt(column, val);
		}
		return this;
	}

	/**
	 * 仅当值非空时追加小于等于（&lt;=）条件. 只有当提供的值存在时才会生效。
	 * @param column 以 lambda 指定的列
	 * @param val 用于比较的值
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
		if (val != null) {
			return (LambdaQueryWrapperX<T>) super.le(column, val);
		}
		return this;
	}

	/**
	 * 根据入参是否存在来追加 BETWEEN 条件. 该方法会智能处理以下场景：
	 * <ul>
	 * <li>val1 与 val2 均存在：追加 BETWEEN 条件；</li>
	 * <li>仅 val1 存在：追加大于等于条件（&gt;=）；</li>
	 * <li>仅 val2 存在：追加小于等于条件（&lt;=）；</li>
	 * <li>两者都不存在：不追加任何条件。</li>
	 * </ul>
	 * @param column 以 lambda 指定的列
	 * @param val1 下界值（可为 null）
	 * @param val2 上界值（可为 null）
	 * @return 当前包装器实例，便于链式调用
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
	 * 使用数组中的值追加 BETWEEN 条件. 该便捷方法会提取数组的前两个元素作为上下界，并委托给另一个 betweenIfPresent 方法。
	 * @param column 以 lambda 指定的列
	 * @param values 包含上下界的数组（可为 null 或空）
	 * @return 当前包装器实例，便于链式调用
	 */
	public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object[] values) {
		Object val1 = ArrayUtil.get(values, 0);
		Object val2 = ArrayUtil.get(values, 1);
		return betweenIfPresent(column, val1, val2);
	}

	// ========== Overridden Parent Methods for Method Chaining ==========

	/**
	 * 覆写等值条件方法，确保返回类型便于链式调用.
	 * @param condition 是否追加该条件
	 * @param column 需要比较的实体字段
	 * @param val 用于比较的值
	 * @return 当前包装器实例
	 */
	@Override
	public LambdaQueryWrapperX<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
		super.eq(condition, column, val);
		return this;
	}

	/**
	 * 覆写等值条件方法，确保返回类型便于链式调用.
	 * @param column 需要比较的实体字段
	 * @param val 用于比较的值
	 * @return 当前包装器实例
	 */
	@Override
	public LambdaQueryWrapperX<T> eq(SFunction<T, ?> column, Object val) {
		super.eq(column, val);
		return this;
	}

	/**
	 * 覆写降序排序方法，确保返回类型便于链式调用.
	 * @param column 参与排序的实体字段
	 * @return 当前包装器实例
	 */
	@Override
	public LambdaQueryWrapperX<T> orderByDesc(SFunction<T, ?> column) {
		super.orderByDesc(true, column);
		return this;
	}

	/**
	 * 覆写 last 方法以在查询末尾附加自定义 SQL 片段，确保返回类型便于链式调用.
	 * @param lastSql 需要追加的 SQL 片段
	 * @return 当前包装器实例
	 */
	@Override
	public LambdaQueryWrapperX<T> last(String lastSql) {
		super.last(lastSql);
		return this;
	}

	/**
	 * 覆写 IN 条件方法，确保返回类型便于链式调用.
	 * @param column 需要比较的实体字段
	 * @param coll 用于比较的值集合
	 * @return 当前包装器实例
	 */
	@Override
	public LambdaQueryWrapperX<T> in(SFunction<T, ?> column, Collection<?> coll) {
		super.in(column, coll);
		return this;
	}

}
