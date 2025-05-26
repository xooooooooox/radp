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

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 继承该类的数据库持久化对象
 * <pre>{@code
 * @TableName("demo")
 * public class DemoPO extends BasePO<DemoPO> {
 *     ... ...
 * }
 * }</pre>
 *
 * @author x9x
 * @since 2024-10-01 10:02
 */
public abstract class BasePO<T extends Model<?>> extends AutofillModel<T> {

    private static final long serialVersionUID = 1L;
}
