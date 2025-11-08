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

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import space.x9x.radp.commons.json.JacksonUtils;

/**
 * JSON数据转换器，用于将对象转换为Excel单元格数据.
 * <p>
 * 该转换器通过Jackson将对象序列化为JSON字符串，以便在Excel中展示
 *
 * @author x9x
 * @since 2025-10-30 23:03
 */
public class JsonConvert implements Converter<Object> {

	/**
	 * 获取支持的Java类型.
	 * <p>
	 * 该方法暂不支持使用，也不需要实现
	 */
	@Override
	public Class<?> supportJavaTypeKey() {
		throw new UnsupportedOperationException("暂不支持,也不需要");
	}

	/**
	 * 获取支持的Excel单元格数据类型.
	 * <p>
	 * 该方法暂不支持使用，也不需要实现
	 */
	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		throw new UnsupportedOperationException("暂不支持,也不需要");
	}

	/**
	 * 将Java对象转换为Excel单元格数据.
	 * <p>
	 * 通过Jackson工具将对象序列化为JSON字符串
	 * @param value 需要转换的Java对象
	 * @param contentProperty excel内容属性
	 * @param globalConfiguration 全局配置
	 * @return excel单元格数据
	 * @throws Exception 转换异常
	 */
	@Override
	public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		// 生成 Excel 小表格
		return new WriteCellData<>(JacksonUtils.toJSONString(value));
	}

}
