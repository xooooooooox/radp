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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.converters.longconverter.LongStringConverter;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import space.x9x.radp.solutions.excel.handler.ColumnWidthMatchStyleStrategy;
import space.x9x.radp.solutions.excel.handler.SelectSheetWriteHandler;

/**
 * Excel 工具类.
 * <p>
 * 一般来说, Excel 一个 sheet 最多 10 万条数据, 一万条数据大概 1MB (约 15 个字段 x 一万条数据)
 *
 * @author x9x
 * @since 2025-10-30 14:59
 */
@Slf4j
@UtilityClass
public class ExcelUtils {

	/**
	 * For large datasets, computing auto column width is expensive. Only enable it when
	 * rows do not exceed this threshold.
	 */
	private static final int COLUMN_AUTO_WIDTH_MAX_ROWS = 3000;

	/**
	 * Excel MIME type (with UTF-8 charset).
	 */
	private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";

	/**
	 * Default sheet name.
	 */
	private static final String DEFAULT_SHEET = "Sheet1";

	/**
	 * Default batch size for large dataset processing when writing Excel files. This
	 * value determines how many records are written in each batch to optimize memory
	 * usage.
	 */
	private static final int DEFAULT_BATCH_SIZE = 5000;

	private static String normalizeFilename(String filename) {
		String name = (filename == null || filename.trim().isEmpty()) ? "export.xlsx" : filename;
		if (!name.contains(".")) {
			name = String.format("%s.xlsx", name);
		}
		return name;
	}

	private static String normalizeSheetName(String sheetName) {
		return (sheetName == null || sheetName.trim().isEmpty()) ? DEFAULT_SHEET : sheetName;
	}

	private static <T> List<T> safeList(List<T> data) {
		return (data == null) ? Collections.emptyList() : data;
	}

	private static String buildContentDisposition(String filename) throws IOException {
		String encodedFileName = URLEncoder.encode(normalizeFilename(filename), StandardCharsets.UTF_8.name())
			.replace("+", "%20");
		return String.format("attachment; filename*=UTF-8''%s; filename=\"%s\"", encodedFileName, encodedFileName);
	}

	private static void setExcelHeaders(HttpServletResponse response, String contentDisposition) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
		response.setContentType(EXCEL_CONTENT_TYPE);
	}

	// @formatter:off
	private static <T> ExcelWriterBuilder baseWriter(OutputStream out, Class<T> head) {
		return baseWriter(out, head, null, null);
	}

	private static <T> ExcelWriterBuilder baseWriter(OutputStream out, Class<T> head, Integer firstRow) {
		return baseWriter(out, head, firstRow, null);
	}

	private static <T> ExcelWriterBuilder baseWriter(OutputStream out, Class<T> head, Integer firstRow, Integer lastRow) {
		// Excel stores numbers with a maximum precision of 15 digits.
		// Java `Long` (up to 19 digits). When exported as numeric cells, Excel rounds the tailing digits to zeros

		return FastExcelFactory.write(out, head)
			.autoCloseStream(false) // 不要自动关闭, 交给 Servlet 处理
			.registerWriteHandler((firstRow == null && lastRow == null)
				? new SelectSheetWriteHandler(head)
				: new SelectSheetWriteHandler(head, firstRow, lastRow)) // 处理 Excel 单元格的下拉框,基于@ExcelColumnSelect 注解配置
			.registerConverter(new LongStringConverter()); // 避免 Long 类型丢失精度
	}
	// @formatter:on

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
		write(response, filename, sheetName, head, data, null);
	}

	/**
	 * 将列表以 Excel 响应给前端，支持自定义下拉起始行.
	 * @param response 响应
	 * @param filename 文件名
	 * @param sheetName excel sheet 名
	 * @param head excel head 头
	 * @param data 数据列表
	 * @param firstRow 下拉验证的首行（0 基，未指定默认为 1）
	 * @param <T> 泛型, 保证 head 和 data 数据类型的一致性
	 * @throws IOException 写失败的情况下抛出异常
	 */
	public static <T> void write(HttpServletResponse response, String filename, String sheetName, Class<T> head,
			List<T> data, Integer firstRow) throws IOException {
		write(response, filename, sheetName, head, data, firstRow, null);
	}

	/**
	 * 将列表以 Excel 响应给前端，支持自定义下拉起始/终止行.
	 * @param response 响应
	 * @param filename 文件名
	 * @param sheetName excel sheet 名
	 * @param head excel head 头
	 * @param data 数据列表
	 * @param firstRow 下拉验证的首行（0 基，未指定默认为 1）
	 * @param lastRow 下拉验证的末行（0 基，未指定默认为默认 LAST_ROW）
	 * @param <T> 泛型, 保证 head 和 data 数据类型的一致性
	 * @throws IOException 写失败的情况下抛出异常
	 */
	public static <T> void write(HttpServletResponse response, String filename, String sheetName, Class<T> head,
			List<T> data, Integer firstRow, Integer lastRow) throws IOException {
		// 先准备好安全的文件名、表名和数据，但不要提前修改响应头，避免后续写入报错时 contentType 已被修改
		String safeFilename = normalizeFilename(filename);
		String safeSheetName = normalizeSheetName(sheetName);
		List<T> safeData = safeList(data);

		// 采用 RFC 5987/6266 的 Content-Disposition
		String contentDisposition = buildContentDisposition(filename);

		// 生成 Excel 到内存缓冲区，只有成功后才设置响应头与写出内容
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ExcelWriterBuilder writer = baseWriter(baos, head, firstRow, lastRow);

		int size = safeData.size();
		// @formatter:off
		if (size <= COLUMN_AUTO_WIDTH_MAX_ROWS) {
			// 小数据量开启自适应列宽，避免大数据计算开销
			writer.registerWriteHandler(new ColumnWidthMatchStyleStrategy()); // 基于 column 长度，自动适配。最大 255 宽度
		}
		// @formatter:on

		// 如果 doWrite 过程中抛出异常，响应头不会被提前修改
		writer.sheet(safeSheetName).doWrite(safeData);

		// 写入成功后再设置响应头与内容类型，避免错误时 contentType 被修改
		setExcelHeaders(response, contentDisposition);
		response.setContentLength(baos.size());
		baos.writeTo(response.getOutputStream());
	}

	/**
	 * 针对大数据量的 Excel 导出，直接将内容流式写入响应输出流，避免在内存中构建整份文件。使用分块传输（不设置
	 * Content-Length）以降低内存占用，同时关闭自适应列宽计算.
	 * @param response 响应
	 * @param filename 文件名
	 * @param sheetName sheet 名称
	 * @param head 表头类型
	 * @param data 数据列表（会按批次写出）
	 * @param <T> 泛型
	 * @throws IOException 写入失败抛出异常。
	 */
	public static <T> void writeLarge(HttpServletResponse response, String filename, String sheetName, Class<T> head,
			List<T> data) throws IOException {
		writeLarge(response, filename, sheetName, head, data, DEFAULT_BATCH_SIZE, null);
	}

	/**
	 * 针对大数据量的 Excel 导出，支持自定义批大小的流式写出. 建议在 Controller 中直接调用本方法，以便客户端尽早开始下载。
	 * @param response 响应
	 * @param filename 文件名
	 * @param sheetName sheet 名称
	 * @param head 表头类型
	 * @param data 数据列表（会按批次写出）
	 * @param batchSize 每批写出的记录数，建议 1000~10000
	 * @param <T> 泛型
	 * @throws IOException 写入失败抛出异常。
	 */
	public static <T> void writeLarge(HttpServletResponse response, String filename, String sheetName, Class<T> head,
			List<T> data, int batchSize) throws IOException {
		writeLarge(response, filename, sheetName, head, data, batchSize, null);
	}

	/**
	 * 针对大数据量的 Excel 导出，支持自定义批大小与下拉起始行的流式写出.
	 * @param response 响应
	 * @param filename 文件名
	 * @param sheetName sheet 名称
	 * @param head 表头类型
	 * @param data 数据列表（会按批次写出）
	 * @param batchSize 每批写出的记录数，建议 1000~10000
	 * @param firstRow 下拉验证的首行（0 基，未指定默认为 1）
	 * @param <T> 泛型
	 * @throws IOException 写入失败抛出异常。
	 */
	public static <T> void writeLarge(HttpServletResponse response, String filename, String sheetName, Class<T> head,
			List<T> data, int batchSize, Integer firstRow) throws IOException {
		writeLarge(response, filename, sheetName, head, data, batchSize, firstRow, null);
	}

	/**
	 * 针对大数据量的 Excel 导出，支持自定义批大小与下拉起始/终止行的流式写出.
	 * @param response 响应
	 * @param filename 文件名
	 * @param sheetName sheet 名称
	 * @param head 表头类型
	 * @param data 数据列表（会按批次写出）
	 * @param batchSize 每批写出的记录数，建议 1000~10000
	 * @param firstRow 下拉验证的首行（0 基，未指定默认为 1）
	 * @param lastRow 下拉验证的末行（0 基，未指定默认为默认 LAST_ROW）
	 * @param <T> 泛型
	 * @throws IOException 写入失败抛出异常。
	 */
	public static <T> void writeLarge(HttpServletResponse response, String filename, String sheetName, Class<T> head,
			List<T> data, int batchSize, Integer firstRow, Integer lastRow) throws IOException {
		String safeFilename = normalizeFilename(filename);
		String safeSheetName = normalizeSheetName(sheetName);
		List<T> safeData = safeList(data);

		String contentDisposition = buildContentDisposition(filename);

		// 提前设置响应头，启用分块传输（不设置 Content-Length）
		setExcelHeaders(response, contentDisposition);

		ExcelWriter excelWriter = null;
		try {
			excelWriter = baseWriter(response.getOutputStream(), head, firstRow, lastRow)
				// 大文件导出关闭列宽自适应，避免高昂的计算开销（不注册列宽策略）
				.build();

			WriteSheet writeSheet = FastExcelFactory.writerSheet(safeSheetName).build();

			int size = safeData.size();
			if (size == 0) {
				excelWriter.write(Collections.emptyList(), writeSheet);
			}
			else {
				int bs = Math.max(batchSize, 1);
				for (int from = 0; from < size; from += bs) {
					int to = Math.min(from + bs, size);
					java.util.List<T> sub = safeData.subList(from, to);
					excelWriter.write(sub, writeSheet);
				}
			}
		}
		finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
			// 确保缓冲区刷出
			try {
				response.flushBuffer();
			}
			catch (Exception ignore) {
			}
		}
	}

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
