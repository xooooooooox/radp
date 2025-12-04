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

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

import space.x9x.radp.solutions.dict.core.DictService;
import space.x9x.radp.solutions.excel.annotations.DictFormat;

/**
 * Excel 数据字典转换器.
 *
 * @author RADP x9x
 * @since 2025-11-07 00:10
 */
@Slf4j
public class DictConvert implements Converter<Object> {

	@Override
	public Class<?> supportJavaTypeKey() {
		throw new UnsupportedOperationException("暂不支持,也不需要");
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		throw new UnsupportedOperationException("暂不支持,也不需要");
	}

	@Override
	public Object convertToJavaData(ReadCellData<?> readCellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		// 使用字典解析
		String label = readCellData.getStringValue();
		if (contentProperty == null || contentProperty.getField() == null
				|| !contentProperty.getField().isAnnotationPresent(DictFormat.class)) {
			return label;
		}
		String type = getType(contentProperty);
		try {
			DictService dictService = SpringUtil.getBean(DictService.class);
			String value = dictService.getValue(type, label);
			// 将 String 的 value 转换成对应属性
			Class<?> target = contentProperty.getField().getType();
			return Convert.convert(target, value);
		}
		catch (Exception ex) {
			log.warn("DictConvert.convertToJavaData error. type={}, label={}, ex={}", type, label, ex.getMessage());
			return label;
		}
	}

	@Override
	public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		String text = value == null ? "" : String.valueOf(value);
		if (contentProperty == null || contentProperty.getField() == null
				|| !contentProperty.getField().isAnnotationPresent(DictFormat.class)) {
			return new WriteCellData<>(text);
		}
		// 使用字典格式化
		String type = getType(contentProperty);
		try {
			DictService dictService = SpringUtil.getBean(DictService.class);
			String label = dictService.getLabel(type, text);
			return new WriteCellData<>(label);
		}
		catch (Exception ex) {
			log.warn("DictConvert.convertToExcelData error. type={}, value={}, ex={}", type, text, ex.getMessage());
			return new WriteCellData<>(text);
		}
	}

	private static String getType(ExcelContentProperty contentProperty) {
		return contentProperty.getField().getAnnotation(DictFormat.class).value();
	}

}
