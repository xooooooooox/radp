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

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * A composite {@link MetaObjectHandler} that delegates to multiple handlers.
 *
 * <p>
 * Deprecated: superseded by strategy-based auto-fill via {@link AutoFillStrategy} and
 * {@link StrategyDelegatingMetaObjectHandler}. This class is no longer used by the
 * starter auto-configuration and will be removed in a future release.
 *
 * @author Junie
 * @since 2025-11-10 15:04
 */
@Deprecated
public class CompositeMetaObjectHandler implements MetaObjectHandler {

	private final List<MetaObjectHandler> delegates;

	public CompositeMetaObjectHandler(List<MetaObjectHandler> delegates) {
		this.delegates = delegates == null ? Collections.emptyList() : delegates;
	}

	@Override
	public void insertFill(MetaObject metaObject) {
		for (MetaObjectHandler d : this.delegates) {
			try {
				// Each delegate will internally check if it should handle the entity
				d.insertFill(metaObject);
			}
			catch (Throwable ignore) {
				// Be defensive: one misbehaving handler shouldn't block others
			}
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		for (MetaObjectHandler d : this.delegates) {
			try {
				d.updateFill(metaObject);
			}
			catch (Throwable ignore) {
				// Be defensive: one misbehaving handler shouldn't block others
			}
		}
	}

}
