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

import org.springframework.util.StringUtils;

import space.x9x.radp.spring.framework.web.LoginUserResolver;

/**
 * default auto-fill strategy for {@link BasePO}.
 * <p>
 * Populates createdAt/updatedAt/creator/updater fields on insert and updatedAt/updater on
 * update. This strategy only applies when the entity is an instance of {@link BasePO} (or
 * a subclass).
 *
 * @author x9x
 * @since 2025-11-10 15:29
 */
public class BasePOAutoFillStrategy extends AbstractAutoFillStrategy<BasePO> {

	private final LoginUserResolver loginUserResolver;

	/**
	 * Creates the default auto-fill strategy for {@link BasePO}-based entities.
	 * @param loginUserResolver resolver used to obtain the current login user id; may be
	 * null
	 */
	public BasePOAutoFillStrategy(LoginUserResolver loginUserResolver) {
		super(BasePO.class);
		this.loginUserResolver = loginUserResolver;
	}

	@Override
	protected void doInsertFill(BasePO basePO, MetaObject metaObject) {
		// 1) 自动填充 创建时间以及最后更新时间
		LocalDateTime now = LocalDateTime.now();
		if (Objects.isNull(basePO.getCreatedAt())) {
			// 如果未显示指定创建时间
			basePO.setCreatedAt(now);
		}
		if (Objects.isNull(basePO.getUpdatedAt())) {
			// 如果为显式指定修改时间
			basePO.setUpdatedAt(now);
		}

		// 2) 自动填充 创建者与修改者
		String loginUserId = resolveLoginUserId();
		if (Objects.nonNull(loginUserId)) {
			if (!StringUtils.hasText(basePO.getCreator())) {
				basePO.setCreator(loginUserId);
			}
			if (!StringUtils.hasText(basePO.getUpdater())) {
				basePO.setUpdater(loginUserId);
			}
		}
	}

	@Override
	protected void doUpdateFill(BasePO basePO, MetaObject metaObject) {
		// 1) 自动填充 更新时间
		LocalDateTime now = LocalDateTime.now();
		// entity.setUpdatedAt(now);
		if (Objects.isNull(basePO.getUpdatedAt())) {
			// 如果为显式指定"最后修改时间", 则自动填充当前时间作为"最后修改时间"
			basePO.setUpdatedAt(now);
		}

		// 2) 自动填充 修改者
		String loginUserId = resolveLoginUserId(); // 当前登录用户
		if (Objects.nonNull(loginUserId) && !StringUtils.hasText(basePO.getUpdater())) {
			// 若未显式指定更新者且当前登录用户非空, 则自动填充当前登录用户人作为 updater
			basePO.setUpdater(loginUserId);
		}
	}

	private String resolveLoginUserId() {
		return this.loginUserResolver != null ? this.loginUserResolver.resolveCurrentLoginUser() : null;
	}

}
