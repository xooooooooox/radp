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

package space.x9x.radp.extension.strategy;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

/**
 * 优先级接口，用于定义对象的优先级顺序.
 * <p>
 * Priority interface used to define the priority order of objects. This interface extends
 * Comparable to allow objects to be sorted based on their priority values. Lower priority
 * values indicate higher precedence in processing order.
 *
 * @author IO x9x
 * @since 2024-09-24 19:41
 */
public interface Prioritized extends Comparable<Prioritized> {

	/**
	 * Constant representing the maximum priority value. Elements with this priority will
	 * be considered last in priority ordering.
	 */
	int MAX_PRIORITY = Integer.MAX_VALUE;

	/**
	 * Constant representing the minimum priority value. Elements with this priority will
	 * be considered first in priority ordering.
	 */
	int MIN_PRIORITY = Integer.MIN_VALUE;

	/**
	 * Constant representing the normal (default) priority value. Elements with this
	 * priority will be considered in the middle of priority ordering.
	 */
	int NORMAL_PRIORITY = 0;

	/**
	 * Comparator for comparing objects based on their priority. This comparator handles
	 * both Prioritized and non-Prioritized objects: - Prioritized objects are compared
	 * using their priority values - A Prioritized object is considered higher priority
	 * than a non-Prioritized object - Two non-Prioritized objects are considered equal in
	 * priority
	 */
	Comparator<Object> COMPARATOR = (one, two) -> {
		// 判断两个对象是否为Prioritized的实例
		boolean b1 = one instanceof Prioritized;
		boolean b2 = two instanceof Prioritized;
		// 如果第一个对象是Prioritized实例而第二个不是，返回-1，表示第一个优先级更高
		if (b1 && !b2) {
			return -1;
		}
		// 如果第二个对象是Prioritized实例而第一个不是，返回1，表示第二个优先级更高
		if (b2 && !b1) {
			return 1;
		}
		// 如果两个对象都是Prioritized实例，根据它们的compareTo方法返回结果
		if (b1) {
			return ((Prioritized) one).compareTo((Prioritized) two);
		}
		// 如果两个对象都不是Prioritized实例，或者上述条件都不满足，返回0
		return 0;
	};

	/**
	 * 获取当前对象的优先级. 默认实现返回NORMAL_PRIORITY，意味着未特殊指定优先级的默认为正常优先级.
	 * <p>
	 * Gets the priority of the current object. The default implementation returns
	 * NORMAL_PRIORITY, meaning that objects without a specifically assigned priority will
	 * have normal priority.
	 * @return the priority of the current object
	 */
	default int getPriority() {
		return NORMAL_PRIORITY;
	}

	/**
	 * 比较当前对象与另一个Prioritized对象的优先级. 重写了Comparable接口的compareTo方法，基于优先级进行比较.
	 * <p>
	 * Compares the priority of this object with another Prioritized object. This method
	 * overrides the compareTo method from the Comparable interface and performs
	 * comparison based on priority values.
	 * @param that the Prioritized object to compare with
	 * @return the comparison result: -1 if this object has lower priority, 0 if
	 * priorities are equal, 1 if this object has higher priority
	 */
	@Override
	default int compareTo(@NotNull Prioritized that) {
		return Integer.compare(this.getPriority(), that.getPriority());
	}

}
