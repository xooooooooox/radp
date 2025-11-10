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

import org.apache.ibatis.reflection.MetaObject;

import org.springframework.util.Assert;

/**
 * Base class for {@link AutoFillStrategy} implementations that target a specific entity
 * type. Subclasses only need to implement the strongly typed template methods while this
 * class handles support checks and casts.
 *
 * @author Junie
 * @since 2025-01-28
 * @param <T> entity type supported by the strategy
 */
public abstract class AbstractAutoFillStrategy<T> implements AutoFillStrategy {

	private final Class<T> supportedType;

	protected AbstractAutoFillStrategy(Class<T> supportedType) {
		Assert.notNull(supportedType, "supportedType must not be null");
		this.supportedType = supportedType;
	}

	@Override
	public final boolean supports(Object entity) {
		return entity != null && this.supportedType.isInstance(entity);
	}

	@Override
	public final void insertFill(Object entity, MetaObject metaObject) {
		doInsertFill(this.supportedType.cast(entity), metaObject);
	}

	@Override
	public final void updateFill(Object entity, MetaObject metaObject) {
		doUpdateFill(this.supportedType.cast(entity), metaObject);
	}

	/**
	 * Template method invoked for INSERT statements.
	 * @param entity typed entity instance
	 * @param metaObject mybatis meta object for advanced access if necessary
	 */
	protected abstract void doInsertFill(T entity, MetaObject metaObject);

	/**
	 * Template method invoked for UPDATE statements.
	 * @param entity typed entity instance
	 * @param metaObject mybatis meta object for advanced access if necessary
	 */
	protected abstract void doUpdateFill(T entity, MetaObject metaObject);

	protected final Class<T> getSupportedType() {
		return this.supportedType;
	}

}
