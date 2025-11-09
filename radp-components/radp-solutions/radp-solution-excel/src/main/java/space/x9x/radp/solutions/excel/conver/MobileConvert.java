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

package space.x9x.radp.solutions.excel.conver;

import java.math.BigDecimal;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.DataFormatData;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import lombok.extern.slf4j.Slf4j;

/**
 * 手机号转换器. 可以避免写入 excel 时, 手机号所见非所得
 *
 * @author IO x9x
 * @since 2025-11-09 14:21
 */
@Slf4j
public class MobileConvert implements Converter<String> {

	/**
	 * 指明此转换器支持的 Java 字段类型.
	 */
	@Override
	public Class<?> supportJavaTypeKey() {
		return String.class;
	}

	/**
	 * 指明此转换器支持的 Excel 单元格原始类型（可返回 null 表示不限制）.
	 */
	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		// 可返回 null：支持任意原始单元格类型；或指定 STRING/NUMBER 等
		return null;
	}

	/**
	 * 写出: Java -> Excel. 把 Java 的手机号写成“文本单元格”，并设置 dataFormat=49
	 */
	@Override
	public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		WriteCellData<Object> cell = new WriteCellData<>(value);
		cell.setType(CellDataTypeEnum.STRING);

		WriteCellStyle style = new WriteCellStyle();
		DataFormatData df = new DataFormatData();
		df.setFormat("@"); // "@" 即 Excel 中的文本
		style.setDataFormatData(df);

		cell.setWriteCellStyle(style);
		return cell;
	}

	/**
	 * 写入: Excel -> Java. 无论原始是字符串还是数字，都还原为纯字符串，避免科学计数法/进位
	 */
	@Override
	public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		switch (cellData.getType()) {
			case NUMBER:
				BigDecimal num = cellData.getNumberValue();
				return trimToNull(num == null ? null : num.toPlainString());
			case BOOLEAN:
				return String.valueOf(cellData.getBooleanValue());
			case STRING:
			default:
				return trimToNull(cellData.getStringValue());
		}
	}

	private String trimToNull(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		return s.isEmpty() ? null : s;
	}

}
