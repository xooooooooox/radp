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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;

import space.x9x.radp.solutions.excel.utils.ExcelUtils;

/**
 * Basic unit tests for ExcelUtils focusing on header handling, round-trip write/read, and
 * empty-row skipping behavior.
 * <p>
 * These tests avoid external dependencies (e.g., DictService) by using a simple head
 * class without dict-related annotations.
 *
 * @author x9x
 * @since 2025-11-09 19:45
 */
class ExcelUtilsTest {

	@Test
	@DisplayName("write -> sets headers and produces non-empty content")
	void testWriteSetsHeadersAndProducesContent() throws IOException {
		// Prepare data
		List<SimpleVO> list = new ArrayList<>();
		list.add(new SimpleVO("alice", 1));

		MockHttpServletResponse resp = new MockHttpServletResponse();
		ExcelUtils.write(resp, "users.xlsx", "Users", SimpleVO.class, list);

		// Verify headers
		String ct = resp.getContentType();
		Assertions.assertThat(ct)
			.isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		String cd = resp.getHeader("Content-Disposition");
		Assertions.assertThat(cd).isNotBlank();
		Assertions.assertThat(cd).contains("users.xlsx");

		// Body exists
		byte[] bytes = resp.getContentAsByteArray();
		Assertions.assertThat(bytes).hasSizeGreaterThan(0);
	}

	@Test
	@DisplayName("write/read round-trip and skipEmptyRows=true filters blank rows")
	void testRoundTripAndSkipEmptyRows() throws IOException {
		// Prepare data containing two empty rows in the middle and tail
		List<SimpleVO> list = new ArrayList<>();
		list.add(new SimpleVO("alice", 1)); // non-empty
		list.add(new SimpleVO()); // empty row (all fields null)
		list.add(new SimpleVO("   ", null)); // considered empty (blank string + null)
		list.add(new SimpleVO("bob", 2)); // non-empty

		MockHttpServletResponse resp = new MockHttpServletResponse();
		ExcelUtils.write(resp, "rt.xlsx", "SheetX", SimpleVO.class, list);

		byte[] bytes = resp.getContentAsByteArray();
		MockMultipartFile file = new MockMultipartFile("file", "rt.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new ByteArrayInputStream(bytes));

		// Read without skipping -> FastExcel itself skips completely blank rows
		List<SimpleVO> readAll = ExcelUtils.read(file, SimpleVO.class, false);
		Assertions.assertThat(readAll).hasSize(2);

		// Read with skipping -> remains 2; our extra filter is a no-op here
		List<SimpleVO> readFiltered = ExcelUtils.read(file, SimpleVO.class, true);
		Assertions.assertThat(readFiltered).hasSize(2);
		Assertions.assertThat(readFiltered.get(0).getName()).isEqualTo("alice");
		Assertions.assertThat(readFiltered.get(0).getScore()).isEqualTo(1);
		Assertions.assertThat(readFiltered.get(1).getName()).isEqualTo("bob");
		Assertions.assertThat(readFiltered.get(1).getScore()).isEqualTo(2);
		// And readAll equals readFiltered in this case
		Assertions.assertThat(readAll).extracting(SimpleVO::getName).containsExactly("alice", "bob");
	}

	@Test
	@DisplayName("writeLarge -> streaming headers set and content written")
	void testWriteLargeStreaming() throws IOException {
		// Prepare a larger list
		List<SimpleVO> list = new ArrayList<>();
		for (int i = 0; i < 1500; i++) {
			list.add(new SimpleVO("user-" + i, i));
		}

		MockHttpServletResponse resp = new MockHttpServletResponse();
		ExcelUtils.writeLarge(resp, "large.xlsx", "Users", SimpleVO.class, list, 300);

		// Headers available
		Assertions.assertThat(resp.getContentType())
			.isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		Assertions.assertThat(resp.getHeader("Content-Disposition")).contains("large.xlsx");

		// Content written and no explicit Content-Length set for streaming
		Assertions.assertThat(resp.getContentAsByteArray()).hasSizeGreaterThan(0);
		Assertions.assertThat(resp.getHeader("Content-Length")).isNull();
	}

	// Simple head model for tests
	@Setter
	@Getter
	public static class SimpleVO {

		@ExcelProperty("Name")
		private String name;

		@ExcelProperty("Score")
		private Integer score;

		public SimpleVO() {
		}

		public SimpleVO(String name, Integer score) {
			this.name = name;
			this.score = score;
		}

	}

}
