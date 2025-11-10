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

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.ibatis.reflection.MetaObject;

/**
 * default auto-fill strategy for {@link BasePO}.
 * <p>
 * Populates createdAt/updatedAt/creator/updater fields on insert and updatedAt/updater on
 * update. This strategy only applies when the entity is an instance of {@link BasePO} (or
 * a subclass).
 *
 * <p>
 * Note: user id resolution is currently a placeholder and should be integrated with your
 * security context.
 *
 * @author Junie
 * @since 2025-11-10 15:29
 */
public class BasePOAutoFillStrategy implements AutoFillStrategy {

	@Override
	public boolean supports(Object entity) {
		return entity instanceof BasePO;
	}

	@Override
	public void insertFill(Object entity, MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();
		fillIfNull(metaObject, "createdAt", now);
		fillIfNull(metaObject, "updatedAt", now);

		Long loginUserId = 1L; // TODO integrate with context
		if (Objects.nonNull(loginUserId)) {
			fillIfNull(metaObject, "creator", loginUserId.toString());
			fillIfNull(metaObject, "updater", loginUserId.toString());
		}
	}

	@Override
	public void updateFill(Object entity, MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();
		setIfPresent(metaObject, "updatedAt", now);
		Long loginUserId = 1L; // TODO integrate with context
		if (Objects.nonNull(loginUserId)) {
			setIfPresent(metaObject, "updater", loginUserId.toString());
		}
	}

	private void fillIfNull(MetaObject metaObject, String field, Object value) {
		if (metaObject.hasGetter(field)) {
			Object current = metaObject.getValue(field);
			if (current == null) {
				metaObject.setValue(field, value);
			}
		}
	}

	private void setIfPresent(MetaObject metaObject, String field, Object value) {
		if (metaObject.hasGetter(field)) {
			metaObject.setValue(field, value);
		}
	}

}
