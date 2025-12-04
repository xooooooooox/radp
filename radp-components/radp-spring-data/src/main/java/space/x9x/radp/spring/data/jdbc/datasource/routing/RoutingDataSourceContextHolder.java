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

package space.x9x.radp.spring.data.jdbc.datasource.routing;

import java.util.ArrayDeque;
import java.util.Deque;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

import space.x9x.radp.commons.lang.StringUtil;

/**
 * 动态路由数据源上下文管理.
 *
 * @author x9x
 * @since 2025-09-20 01:06
 */
@UtilityClass
public class RoutingDataSourceContextHolder {

	/**
	 * 使用双向队列, 支持 A->B->C 数据源嵌套+线程池.
	 */
	private static final TransmittableThreadLocal<Deque<String>> CONTEXT = new TransmittableThreadLocal<Deque<String>>() {
		@Override
		protected Deque<String> initialValue() {
			return new ArrayDeque<>();
		}
	};

	/**
	 * 获得当前线程数据源.
	 * @return 数据源名称
	 */
	public static String peek() {
		return CONTEXT.get().peek();
	}

	/**
	 * 设置当前现成数据源. 如非必要不要手动调用, 调用后请确保最终清除.
	 * @param ds 数据源名称
	 * @return ds 数据源名称
	 * @see #clear()
	 */
	public static String push(String ds) {
		String dataSourceStr = StringUtil.isEmpty(ds) ? "" : ds;
		CONTEXT.get().push(dataSourceStr);
		return dataSourceStr;
	}

	/**
	 * 清空当前线程数据源. 如果当前现成是连续切换数据源, 只会移除掉当前线程的数据源名称.
	 */
	public static void poll() {
		Deque<String> deque = CONTEXT.get();
		deque.poll();
		if (deque.isEmpty()) {
			CONTEXT.remove();
		}
	}

	/**
	 * 强制清空本地线程. 防止内存泄露, 如手动调用了 push 可调用此方法确保清楚.
	 */
	public static void clear() {
		CONTEXT.remove();
	}

}
