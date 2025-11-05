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
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.converters.longconverter.LongStringConverter;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import lombok.experimental.UtilityClass;

import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import space.x9x.radp.solutions.excel.handler.ColumnWidthMatchStyleStrategy;
import space.x9x.radp.solutions.excel.handler.SelectSheetWriteHandler;

/**
 * Excel 工具类.
 *
 * @author IO x9x
 * @since 2025-10-30 14:59
 */
@UtilityClass
public class ExcelUtils {

	/**
	 * For large datasets, computing auto column width is expensive. Only enable it when
	 * rows do not exceed this threshold.
	 */
	private static final int COLUMN_AUTO_WIDTH_MAX_ROWS = 3000;
	private static final java.util.regex.Pattern NON_VISIBLE_ASCII = java.util.regex.Pattern.compile("[^\\x20-\\x7E]");

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
	// @formatter:off
	public static <T> void write(HttpServletResponse response, String filename, String sheetName, Class<T> head,
			List<T> data) throws IOException {
		// 先准备好安全的文件名、表名和数据，但不要提前修改响应头，避免后续写入报错时 contentType 已被修改
		String safeFilename = (filename == null || filename.trim().isEmpty()) ? "export.xlsx" : filename;
		// 如果文件名没有扩展名，补齐 .xlsx，避免部分浏览器因无扩展名回退为默认名
		if (!safeFilename.contains(".")) {
			safeFilename = String.format("%s.xlsx", safeFilename);
		}
		String safeSheetName = (sheetName == null || sheetName.trim().isEmpty()) ? "Sheet1" : sheetName;
		List<T> safeData = (data == null) ? Collections.emptyList() : data;

		// 采用 RFC 5987/6266 的 Content-Disposition，避免在 header 中出现非 ASCII 字符导致 Tomcat 丢弃
		// 先构建 UTF-8 百分号编码的 filename*，再提供 ASCII-only 的 filename 作为回退
		String encodedFileName = URLEncoder.encode(safeFilename, StandardCharsets.UTF_8.name()).replace("+", "%20"); // 空格使用 %20
		// 为了兼容不支持 filename* 的客户端（如部分 Postman 版本），将 filename 也设置为 UTF-8 百分号编码值
		// 大多数现代浏览器会优先使用 filename*，忽略这里的编码值；不支持 filename* 的客户端可以据此还原中文文件名
		String contentDisposition = String.format(
			"attachment; filename*=UTF-8''%s; filename=\"%s\"",
			encodedFileName,
			encodedFileName
		);

		// 生成 Excel 到内存缓冲区，只有成功后才设置响应头与写出内容
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		ExcelWriterBuilder writer = FastExcelFactory.write(baos, head)
			.autoCloseStream(false) // 不要自动关闭
			.registerWriteHandler(new SelectSheetWriteHandler(head)) // 处理 Excel 单元格的下拉框, 基于@ExcelColumnSelect 注解配置
			.registerConverter(new LongStringConverter()); // 避免 Long 类型丢失精度

		int size = safeData.size();
		if (size <= COLUMN_AUTO_WIDTH_MAX_ROWS) {
			// 小数据量开启自适应列宽，避免大数据计算开销
			writer.registerWriteHandler(new ColumnWidthMatchStyleStrategy());
		}

		// 如果 doWrite 过程中抛出异常，响应头不会被提前修改
		writer.sheet(safeSheetName).doWrite(safeData);

		// 写入成功后再设置响应头与内容类型，避免错误时 contentType 被修改
		response.setCharacterEncoding("UTF-8");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		response.setContentLength(baos.size());
		baos.writeTo(response.getOutputStream());
	}
	// @formatter:on

	/**
	 * 从 Excel 文件中读取数据.
	 * @param file excel 文件
	 * @param head excel 数据对应的类型(表头)
	 * @param <T> 泛型, 确保读取的数据类型一致性
	 * @return 读取到的数据列表
	 * @throws IOException 读取失败时抛出异常
	 */
	public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
		return FastExcelFactory.read(file.getInputStream(), head, null)
			.autoCloseStream(false) // 不自动关闭, 交给 Servlet 处理
			.doReadAllSync();
	}

}
