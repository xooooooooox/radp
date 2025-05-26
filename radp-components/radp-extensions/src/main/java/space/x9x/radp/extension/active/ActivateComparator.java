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


import space.x9x.radp.extension.Activate;

import java.util.Comparator;

/**
 * 激活比较器类，实现了Comparator接口，用于比较具有@Activate注解的对象激活顺序
 *
 * @author x9x
 * @since 2024-09-24 12:56
 */
public class ActivateComparator implements Comparator<Object> {

    /**
     * 静态比较器实例，用于快速获取ActivateComparator实例进行比较
     */
    public static final Comparator<Object> COMPARATOR = new ActivateComparator();

    /**
     * 比较两个对象的激活顺序
     * 如果对象上有@Activate注解，则根据注解中的order值进行比较；否则，默认为0
     * 如果对象为null，则按照Java中null的比较规则进行比较
     *
     * @param o1 第一个对象
     * @param o2 第二个对象
     * @return 比较结果，-1表示o1应该在o2之前，1表示o1应该在o2之后，0表示两者相等
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
     * 获取对象类上的激活顺序
     * 通过检查对象类是否具有@Activate注解来决定激活顺序
     *
     * @param clazz 对象的类
     * @return 激活顺序值，如果没有@Activate注解，则为0
     */
    private int getOrder(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Activate.class)) {
            Activate activate = clazz.getAnnotation(Activate.class);
            return activate.order();
        }
        return 0;
    }
}
