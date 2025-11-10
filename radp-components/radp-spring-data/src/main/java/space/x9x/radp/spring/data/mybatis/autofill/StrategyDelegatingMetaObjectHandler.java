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

package space.x9x.radp.spring.data.mybatis.autofill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;

import space.x9x.radp.spring.data.mybatis.support.MybatisEntityResolver;

/**
 * A {@link MetaObjectHandler} that delegates to a list of {@link AutoFillStrategy}
 * implementations. The first strategy that reports
 * {@link AutoFillStrategy#supports(Object)} true for the current entity will be used to
 * perform fill operations.
 *
 * <p>
 * Strategies can be ordered using Spring's {@code @Order} annotation; the injected list
 * preserves this order.
 *
 * @author Junie
 * @since 2025-11-10 15:29
 */
@Slf4j
@RequiredArgsConstructor
public class StrategyDelegatingMetaObjectHandler implements MetaObjectHandler {

	private final java.util.List<AutoFillStrategy> strategies;

	@Override
	public void insertFill(MetaObject metaObject) {
		Object entity = MybatisEntityResolver.resolve(metaObject);
		if (entity == null) {
			return;
		}
		AutoFillStrategy s = selectStrategy(entity);
		if (s != null) {
			s.insertFill(entity, metaObject);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Object entity = MybatisEntityResolver.resolve(metaObject);
		if (entity == null) {
			return;
		}
		AutoFillStrategy s = selectStrategy(entity);
		if (s != null) {
			s.updateFill(entity, metaObject);
		}
	}

	private AutoFillStrategy selectStrategy(Object entity) {
		if (CollectionUtils.isEmpty(this.strategies)) {
			return null;
		}
		java.util.List<AutoFillStrategy> ordered = new java.util.ArrayList<>(this.strategies);
		AnnotationAwareOrderComparator.sort(ordered);
		for (AutoFillStrategy s : ordered) {
			try {
				if (s != null && s.supports(entity)) {
					return s;
				}
			}
			catch (Throwable ex) {
				log.debug("AutoFillStrategy {} threw on supports(): {}", s, ex.toString());
			}
		}
		return null;
	}

}
