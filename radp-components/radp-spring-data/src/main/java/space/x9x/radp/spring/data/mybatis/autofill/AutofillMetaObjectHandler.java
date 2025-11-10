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

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

/**
 * MyBatis Plus MetaObjectHandler implementation for automatically filling created date
 * and last modified date fields. This handler sets the createdDate field on insert
 * operations and the lastModifiedDate field on both insert and update operations.
 *
 * <p>
 * Deprecated: superseded by strategy-based auto-fill via {@link AutoFillStrategy} and
 * {@link StrategyDelegatingMetaObjectHandler}. This class is no longer used by the
 * starter auto-configuration and will be removed in a future release.
 *
 * @author x9x
 * @since 2024-09-30 14:56
 */
@Deprecated
@RequiredArgsConstructor
public class AutofillMetaObjectHandler implements MetaObjectHandler {

	/**
	 * The field name for the creation date in the entity.
	 */
	private final String createdDateFieldName;

	/**
	 * The field name for the last modification date in the entity.
	 */
	private final String lastModifiedDateFieldName;

	/**
	 * The field name representing the creator of the entity.
	 */
	private final String creatorFieldName;

	/**
	 * The field name representing the updater of the entity.
	 */
	private final String updaterFieldName;

	/**
	 * Only apply auto-fill for entities assignable to this target type.
	 */
	private final Class<?> targetType;

	@Override
	public void insertFill(MetaObject metaObject) {
		if (!shouldHandle(metaObject)) {
			return;
		}
		LocalDateTime now = LocalDateTime.now();

		if (hasProperty(metaObject, this.createdDateFieldName)) {
			Object createdVal = this.getFieldValByName(this.createdDateFieldName, metaObject);
			if (Objects.isNull(createdVal)) {
				this.strictInsertFill(metaObject, this.createdDateFieldName, LocalDateTime.class, now);
			}
		}

		if (hasProperty(metaObject, this.lastModifiedDateFieldName)) {
			Object lastModVal = this.getFieldValByName(this.lastModifiedDateFieldName, metaObject);
			if (Objects.isNull(lastModVal)) {
				this.strictInsertFill(metaObject, this.lastModifiedDateFieldName, LocalDateTime.class, now);
			}
		}

		Long loginUserId = 1L; // TODO v3.25-2025/9/14: 需要从上下文中获取当前登录用户编号
		if (Objects.nonNull(loginUserId) && hasProperty(metaObject, this.creatorFieldName)) {
			Object creatorVal = this.getFieldValByName(this.creatorFieldName, metaObject);
			if (Objects.isNull(creatorVal)) {
				this.strictInsertFill(metaObject, this.creatorFieldName, String.class, loginUserId.toString());
			}
		}
		if (Objects.nonNull(loginUserId) && hasProperty(metaObject, this.updaterFieldName)) {
			Object updaterVal = this.getFieldValByName(this.updaterFieldName, metaObject);
			if (Objects.isNull(updaterVal)) {
				this.strictInsertFill(metaObject, this.updaterFieldName, String.class, loginUserId.toString());
			}
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		if (!shouldHandle(metaObject)) {
			return;
		}
		LocalDateTime now = LocalDateTime.now();
		if (hasProperty(metaObject, this.lastModifiedDateFieldName)) {
			this.setFieldValByName(this.lastModifiedDateFieldName, now, metaObject);
		}

		Long loginUserId = 1L; // TODO v3.25-2025/9/14: 需要从上下文中获取当前登录用户编号
		if (Objects.nonNull(loginUserId) && hasProperty(metaObject, this.updaterFieldName)) {
			this.setFieldValByName(this.updaterFieldName, loginUserId.toString(), metaObject);
		}
	}

	private boolean shouldHandle(MetaObject metaObject) {
		Object entity = resolveEntity(metaObject);
		return entity != null && this.targetType.isInstance(entity);
	}

	private boolean hasProperty(MetaObject metaObject, String fieldName) {
		return fieldName != null && !fieldName.trim().isEmpty() && metaObject.hasGetter(fieldName);
	}

	private Object resolveEntity(MetaObject metaObject) {
		Object original = metaObject.getOriginalObject();
		if (original != null && this.targetType.isInstance(original)) {
			return original;
		}
		// Common parameter containers used by MyBatis-Plus
		String[] candidates = new String[] { "et", "entity", "param1", "arg0", "record" };
		for (String key : candidates) {
			if (metaObject.hasGetter(key)) {
				Object val = metaObject.getValue(key);
				if (val != null && this.targetType.isInstance(val)) {
					return val;
				}
			}
		}
		return null;
	}

}
