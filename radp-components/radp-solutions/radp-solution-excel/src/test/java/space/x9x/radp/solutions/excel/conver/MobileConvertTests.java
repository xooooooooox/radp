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

import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for MobileConvert.
 * <p>
 * Focus on branch coverage of convertToJavaData (NUMBER/BOOLEAN/STRING) and verifying
 * convertToExcelData sets STRING cell type with text format ("@").
 *
 * @author RADP x9x
 * @since 2025-11-09 20:15
 */
class MobileConvertTests {

	private final MobileConvert converter = new MobileConvert();

	@Test
	@DisplayName("convertToExcelData -> sets STRING type and text format '@'")
	void testConvertToExcelDataSetsStringTypeAndFormat() throws Exception {
		String mobile = "13800138000";
		WriteCellData<?> cell = converter.convertToExcelData(mobile, null, null);

		Assertions.assertThat(cell.getType()).isEqualTo(CellDataTypeEnum.STRING);
		Assertions.assertThat(cell.getStringValue()).isEqualTo(mobile);
		Assertions.assertThat(cell.getWriteCellStyle()).isNotNull();
		Assertions.assertThat(cell.getWriteCellStyle().getDataFormatData()).isNotNull();
		Assertions.assertThat(cell.getWriteCellStyle().getDataFormatData().getFormat()).isEqualTo("@");
	}

	@Test
	@DisplayName("convertToJavaData(NUMBER) -> returns plain string without scientific notation")
	void testConvertToJavaDataFromNumber() throws Exception {
		// Simulate a large numeric mobile that would be scientific if not handled
		// properly
		ReadCellData<?> numCell = new ReadCellData<>(new BigDecimal("13800138000"));
		String result = converter.convertToJavaData(numCell, null, null);
		Assertions.assertThat(result).isEqualTo("13800138000");
	}

	@Test
	@DisplayName("convertToJavaData(BOOLEAN) -> 'true' or 'false' string")
	void testConvertToJavaDataFromBoolean() throws Exception {
		ReadCellData<?> boolCell = new ReadCellData<>(Boolean.TRUE);
		String result = converter.convertToJavaData(boolCell, null, null);
		Assertions.assertThat(result).isEqualTo("true");
	}

	@Test
	@DisplayName("convertToJavaData(STRING) -> trims whitespace")
	void testConvertToJavaDataFromStringTrim() throws Exception {
		ReadCellData<?> strCell = new ReadCellData<>("  0123  ");
		String result = converter.convertToJavaData(strCell, null, null);
		Assertions.assertThat(result).isEqualTo("0123");
	}

	@Test
	@DisplayName("convertToJavaData(STRING blank) -> returns null after trim")
	void testConvertToJavaDataFromStringBlankToNull() throws Exception {
		ReadCellData<?> blank = new ReadCellData<>("   ");
		String result = converter.convertToJavaData(blank, null, null);
		Assertions.assertThat(result).isNull();
	}

}
