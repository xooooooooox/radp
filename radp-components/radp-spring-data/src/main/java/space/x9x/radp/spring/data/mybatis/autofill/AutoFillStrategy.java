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

/**
 * Strategy interface for MyBatis-Plus automatic field filling.
 * <p>
 * Implementations decide whether they support a given entity instance and how to fill its
 * fields during INSERT and UPDATE operations. This decouples the auto-fill logic from a
 * single fixed base class and allows applications to plug in custom strategies per base
 * type.
 *
 * <p>
 * Typical usage:
 * <ul>
 * <li>Use the built-in {@link BasePOAutoFillStrategy} if your entities extend
 * {@link BasePO} and you want to populate createdAt/updatedAt/creator/updater.</li>
 * <li>Create your own implementation for a custom base entity and register it as a Spring
 * bean; the delegating handler will invoke it when it supports the current entity.</li>
 * </ul>
 *
 * @author x9x
 * @since 2025-11-10 15:29
 */
public interface AutoFillStrategy {

	/**
	 * whether this strategy supports handling the specified entity instance.
	 * @param entity current entity resolved from mybatis meta object (never null)
	 * @return true if this strategy should handle auto-fill for the entity
	 */
	boolean supports(Object entity);

	/**
	 * perform insert-time filling for supported entities.
	 * @param entity the current entity (never null)
	 * @param metaObject mybatis meta object for property access (never null)
	 */
	void insertFill(Object entity, MetaObject metaObject);

	/**
	 * perform update-time filling for supported entities.
	 * @param entity the current entity (never null)
	 * @param metaObject mybatis meta object for property access (never null)
	 */
	void updateFill(Object entity, MetaObject metaObject);

}
