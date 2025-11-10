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
 * @author x9x
 * @since 2025-11-10 15:29
 */
public class BasePOAutoFillStrategy extends AbstractAutoFillStrategy<BasePO> {

	public BasePOAutoFillStrategy() {
		super(BasePO.class);
	}

	@Override
	protected void doInsertFill(BasePO entity, MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();
		if (entity.getCreatedAt() == null) {
			entity.setCreatedAt(now);
		}
		if (entity.getUpdatedAt() == null) {
			entity.setUpdatedAt(now);
		}
		String loginUserId = resolveLoginUserId();
		if (loginUserId != null) {
			if (entity.getCreator() == null) {
				entity.setCreator(loginUserId);
			}
			if (entity.getUpdater() == null) {
				entity.setUpdater(loginUserId);
			}
		}
	}

	@Override
	protected void doUpdateFill(BasePO entity, MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();
		entity.setUpdatedAt(now);
		String loginUserId = resolveLoginUserId();
		if (loginUserId != null) {
			entity.setUpdater(loginUserId);
		}
	}

	private String resolveLoginUserId() {
		Long loginUserId = 1L; // TODO integrate with context
		return loginUserId == null ? null : loginUserId.toString();
	}

}
