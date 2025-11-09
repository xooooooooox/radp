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

package space.x9x.radp.solutions.excel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import space.x9x.radp.solutions.excel.annotations.DictFormat;
import space.x9x.radp.solutions.excel.annotations.ExcelColumnSelect;
import space.x9x.radp.solutions.excel.conver.DictConvert;
import space.x9x.radp.solutions.excel.conver.MobileConvert;

/**
 * VO for testing import/export with @DictFormat and @ExcelColumnSelect. Provides public
 * getters/setters for tests.
 */
@Data
public class ExcelImportVO {

	@ExcelProperty("登录名称")
	@NotBlank(message = "登录名称不能为空")
	@Size(max = 30, message = "登录名称长度不能超过30个字符")
	private String username;

	@ExcelProperty("用户昵称")
	@NotBlank(message = "用户昵称不能为空")
	@Size(max = 50, message = "用户昵称长度不能超过50个字符")
	private String nickname;

	@ExcelProperty("编号")
	@ExcelColumnSelect(functionName = "idOptions")
	@NotNull(message = "编号不能为空")
	@Positive(message = "编号必须为正数")
	private Long id;

	@ExcelProperty("用户邮箱")
	@Email(message = "邮箱格式不正确")
	@Size(max = 100, message = "邮箱长度不能超过100个字符")
	private String email;

	@ExcelProperty(value = "手机号码", converter = MobileConvert.class)
	@NotBlank(message = "手机号码不能为空")
	@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
	private String mobile;

	/**
	 * 使用字典转换性别，导入时可输入“男/女/未知”等字典标签，自动转换为对应的整型编码； 导出/模板时通过下拉框提供可选项。
	 */
	@ExcelProperty(value = "用户性别", converter = DictConvert.class)
	@ExcelColumnSelect(dictType = "user_sex")
	@DictFormat("user_sex")
	private Integer sex;

	/**
	 * 使用字典转换状态，导入时可输入“开启/停用”等字典标签，自动转换为对应的整型编码； 导出/模板时通过下拉框提供可选项。
	 */
	@ExcelProperty(value = "用户状态", converter = DictConvert.class)
	@ExcelColumnSelect(dictType = "common_status")
	@DictFormat("common_status")
	private Integer status;

}
