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

package space.x9x.radp.solutions.excel.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.converters.longconverter.LongStringConverter;

import org.springframework.web.multipart.MultipartFile;

import space.x9x.radp.solutions.excel.handler.ColumnWidthMatchStyleStrategy;
import space.x9x.radp.solutions.excel.handler.SelectSheetWriteHandler;

/**
 * Excel 工具类.
 *
 * @author IO x9x
 * @since 2025-10-30 14:59
 */
public class ExcelUtils {

	/**
	 * 将列表以 Excel 响应给前端.
	 * @param response 响应
	 * @param filename 文件名
	 * @param sheetName excel sheet 名
	 * @param head excel head 头
	 * @param data 数据列表
	 * @param <T> 泛型, 保证 head 和 data 数据类型的一致性
	 * @throws IOException 写失败的情况下抛出异常
	 */
	public static <T> void write(HttpServletResponse response, String filename, String sheetName, Class<T> head,
			List<T> data) throws IOException {

		// 输出 Excel
		FastExcelFactory.write(response.getOutputStream(), head)
			.autoCloseStream(false) // 不要自动关闭, 交给 Servlet 处理
			.registerWriteHandler(new ColumnWidthMatchStyleStrategy()) // 基于 column 长度,
																		// 自动适配单元格宽度, 最大
																		// 255 宽度
			.registerWriteHandler(new SelectSheetWriteHandler(head)) // 处理 Excel 单元格的下拉框,
																		// 基于
																		// @ExcelColumnSelect
																		// 注解配置
			.registerConverter(new LongStringConverter()) // 避免 Long 类型丢失精度
			.sheet(sheetName)
			.doWrite(data);

		// 设置 header 和 contentType. 放到最后的原因是, 避免报错时, 响应 contentType 已经被修改了
		response.setHeader("Content-Disposition", String.format("attachment;filename=%s", URLEncoder.encode(filename, StandardCharsets.UTF_8.displayName())));
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
	}

	public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException{
		return FastExcelFactory.read(file.getInputStream(), head, null)
			.autoCloseStream(false) // 不自动关闭, 交给 Servlet 处理
			.doReadAllSync();
	}

}
