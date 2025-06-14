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

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

/**
 * MyBatis Plus MetaObjectHandler implementation for automatically filling created date
 * and last modified date fields. This handler sets the createdDate field on insert
 * operations and the lastModifiedDate field on both insert and update operations.
 *
 * @author IO x9x
 * @since 2024-09-30 14:56
 */
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

	@Override
	public void insertFill(MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();
		this.strictInsertFill(metaObject, this.createdDateFieldName, LocalDateTime.class, now);
		this.strictUpdateFill(metaObject, this.lastModifiedDateFieldName, LocalDateTime.class, now);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();
		this.strictUpdateFill(metaObject, this.lastModifiedDateFieldName, LocalDateTime.class, now);
	}

}
