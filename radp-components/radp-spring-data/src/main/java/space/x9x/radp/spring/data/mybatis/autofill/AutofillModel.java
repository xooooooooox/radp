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

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base model class with automatic field filling capabilities. This class extends MyBatis
 * Plus's Model class and adds fields for tracking creation and modification dates, which
 * are automatically filled by the AutofillMetaObjectHandler.
 *
 * @author IO x9x
 * @since 2024-09-30 14:55
 * @param <T> the type of the model extending this class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class AutofillModel<T extends Model<?>> extends Model<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Creation date of the entity. This field is automatically filled when the entity is
	 * inserted.
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createdDate;

	/**
	 * Last modification date of the entity. This field is automatically filled when the
	 * entity is inserted or updated.
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime lastModifiedDate;

	/**
	 * The username or identifier of the user who created the entity. This field is
	 * automatically populated during the insert operation using MyBatis Plus's automatic
	 * field filling mechanism.
	 */
	@TableField(fill = FieldFill.INSERT)
	private String creator;

	/**
	 * The username or identifier of the user who last updated the entity. This field is
	 * automatically populated during both insert and update operations using MyBatis
	 * Plus's automatic field filling mechanism.
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updater;

}
