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

import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for DictConvert that avoid Spring DictService by using null contentProperty so
 * that dictionary lookup paths are skipped.
 *
 * @author RADP x9x
 * @since 2025-11-09 20:30
 */
class DictConvertTests {

	private final DictConvert converter = new DictConvert();

	@Test
	@DisplayName("convertToJavaData with null contentProperty -> returns label as-is")
	void testConvertToJavaData_NoAnnotation_ReturnsLabel() throws Exception {
		ReadCellData<?> cell = new ReadCellData<>("男");
		Object result = converter.convertToJavaData(cell, null, null);
		Assertions.assertThat(result).isEqualTo("男");
	}

	@Test
	@DisplayName("convertToExcelData with null contentProperty -> returns raw text")
	void testConvertToExcelData_NoAnnotation_ReturnsText() throws Exception {
		WriteCellData<?> cell = converter.convertToExcelData(123, null, null);
		Assertions.assertThat(cell.getStringValue()).isEqualTo("123");
	}

	@Test
	@DisplayName("support*Key methods throw UnsupportedOperationException (by design)")
	void testSupportMethodsThrow() {
		Assertions.assertThatThrownBy(converter::supportJavaTypeKey).isInstanceOf(UnsupportedOperationException.class);
		Assertions.assertThatThrownBy(converter::supportExcelTypeKey).isInstanceOf(UnsupportedOperationException.class);
	}

}
