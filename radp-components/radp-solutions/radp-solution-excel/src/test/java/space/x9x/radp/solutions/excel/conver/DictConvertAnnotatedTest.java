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

import java.lang.reflect.Field;

import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import space.x9x.radp.solutions.excel.annotations.DictFormat;

/**
 * DictConvert tests for paths when a field is annotated with @DictFormat.
 * <p>
 * We mock ExcelContentProperty.getField() to return an annotated field, but no Spring
 * context is available so SpringUtil.getBean(...) will fail and the converter should
 * enter its catch branch and return safe fallbacks.
 *
 * @author x9x
 * @since 2025-11-09 20:52
 */
class DictConvertAnnotatedTest {

	private final DictConvert converter = new DictConvert();

	@Test
	@DisplayName("convertToJavaData with @DictFormat but no Spring DictService -> returns original label")
	void testConvertToJavaData_Annotated_CatchFallback() throws Exception {
		ExcelContentProperty contentProperty = mockContentPropertyForField("gender");
		ReadCellData<?> cell = new ReadCellData<>("男");
		Object result = converter.convertToJavaData(cell, contentProperty, null);
		Assertions.assertThat(result).isEqualTo("男");
	}

	@Test
	@DisplayName("convertToExcelData with @DictFormat but no Spring DictService -> returns raw text")
	void testConvertToExcelData_Annotated_CatchFallback() throws Exception {
		ExcelContentProperty contentProperty = mockContentPropertyForField("gender");
		WriteCellData<?> cell = converter.convertToExcelData("1", contentProperty, null);
		Assertions.assertThat(cell.getStringValue()).isEqualTo("1");
	}

	private ExcelContentProperty mockContentPropertyForField(String name) throws NoSuchFieldException {
		ExcelContentProperty contentProperty = Mockito.mock(ExcelContentProperty.class);
		Field f = Demo.class.getDeclaredField(name);
		Mockito.when(contentProperty.getField()).thenReturn(f);
		return contentProperty;
	}

	// Demo class with @DictFormat field to simulate annotation presence
	static class Demo {

		@DictFormat("gender")
		private String gender;

	}

}
