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

package space.x9x.radp.extension.active;

import java.util.Comparator;

import space.x9x.radp.extension.Activate;

/**
 * 激活比较器类. 实现了Comparator接口，用于比较具有@Activate注解的对象激活顺序
 * <p>
 * Activation comparator class. Implements the Comparator interface to compare the
 * activation order of objects with the @Activate annotation.
 *
 * @author x9x
 * @since 2024-09-24 12:56
 */
public class ActivateComparator implements Comparator<Object> {

	/**
	 * 静态比较器实例. 用于快速获取ActivateComparator实例进行比较
	 * <p>
	 * static comparator instance. used to quickly obtain an ActivateComparator instance
	 * for comparison.
	 */
	public static final Comparator<Object> COMPARATOR = new ActivateComparator();

	/**
	 * 比较两个对象的激活顺序. 如果对象上有@Activate注解，则根据注解中的order值进行比较；否则，默认为0
	 * 如果对象为null，则按照Java中null的比较规则进行比较
	 * <p>
	 * compares the activation order of two objects. if the objects have the @Activate
	 * annotation, they are compared based on the order value in the annotation;
	 * otherwise, the default is 0. if an object is null, it follows Java's null
	 * comparison rules.
	 * @param o1 the first object
	 * @param o2 the second object
	 * @return the comparison result, -1 means o1 should come before o2, 1 means o1 should
	 * come after o2, 0 means they are equal
	 */
	@Override
	public int compare(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1.equals(o2)) {
			return 0;
		}

		return getOrder(o1.getClass()) > getOrder(o2.getClass()) ? 1 : -1;
	}

	/**
	 * 获取对象类上的激活顺序. 通过检查对象类是否具有@Activate注解来决定激活顺序
	 * <p>
	 * gets the activation order on the object class. determines the activation order by
	 * checking if the object class has the @Activate annotation.
	 * @param clazz the class of the object
	 * @return the activation order value, or 0 if there is no @Activate annotation
	 */
	private int getOrder(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Activate.class)) {
			Activate activate = clazz.getAnnotation(Activate.class);
			return activate.order();
		}
		return 0;
	}

}
