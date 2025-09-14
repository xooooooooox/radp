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
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * Base Persistent Object class for database entities. This class no longer extends any
 * other class, reducing coupling while still providing automatic field filling
 * capabilities via MyBatis-Plus MetaObjectHandler. Any entity may extend this class or
 * simply declare the same-named fields to benefit from auto-fill.
 * <p>
 * Example usage: <pre>
 * {@literal @}TableName("demo")
 * public class DemoPO extends BasePO {
 *     // Entity fields and methods
 * }
 * </pre>
 *
 * @author IO x9x
 * @since 2024-10-01 10:02
 */
@Data
public abstract class BasePO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Creation date of the entity. Auto-filled on insert.
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createdDate;

	/**
	 * Last modification date of the entity. Auto-filled on insert and update.
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime lastModifiedDate;

	/**
	 * Identifier of the creator. Auto-filled on insert.
	 */
	@TableField(fill = FieldFill.INSERT)
	private String creator;

	/**
	 * Identifier of the last updater. Auto-filled on insert and update.
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updater;

	/**
	 * Logical delete flag.
	 */
	@TableLogic
	private Boolean deleted;

}
