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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import cn.hutool.extra.spring.SpringUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.StaticApplicationContext;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;

import space.x9x.radp.solutions.dict.core.DictItem;
import space.x9x.radp.solutions.dict.core.DictService;
import space.x9x.radp.solutions.excel.function.ExcelColumnSelectFunction;
import space.x9x.radp.solutions.excel.utils.ExcelUtils;

/**
 * Tests that exercise @DictFormat and @ExcelColumnSelect behaviors, and verify empty-row
 * exclusion using the annotated ExcelImportVO.
 */
class ExcelDictAndSelectTest {

	private static final AtomicInteger ID_OPTIONS_INVOKED = new AtomicInteger();

	private static final AtomicInteger DICT_LABELS_INVOKED = new AtomicInteger();

	@BeforeAll
	static void initSpringBeans() {
		// Prepare a tiny Spring ApplicationContext and inject it into Hutool's SpringUtil
		StaticApplicationContext ctx = new StaticApplicationContext();
		ctx.getBeanFactory().registerSingleton("dictService", new TestDictService());
		ctx.getBeanFactory().registerSingleton("idOptions", new IdOptionsFunction());
		// Inject context into Hutool's SpringUtil via its ApplicationContextAware method
		new SpringUtil().setApplicationContext(ctx);
	}

	@Test
	@DisplayName("@DictFormat round-trip + @ExcelColumnSelect function and dict wiring")
	void testDictFormatRoundTripAndColumnSelect() throws IOException {
		// Build data rows with numeric codes for sex/status; converts to labels on write
		List<ExcelImportVO> list = new ArrayList<>();
		list.add(row(1L, "alice", "Alice", "alice@example.com", "13800000001", 0, 1)); // 男,
																						// 启用
		list.add(row(2L, "bob", "Bob", "bob@example.com", "13900000002", 1, 0)); // 女, 禁用

		MockHttpServletResponse resp = new MockHttpServletResponse();
		ExcelUtils.write(resp, "users-dict.xlsx", "Users", ExcelImportVO.class, list);

		// Ensure handlers/converters were engaged
		Assertions.assertThat(ID_OPTIONS_INVOKED.get()).as("idOptions function should be invoked").isGreaterThan(0);
		Assertions.assertThat(DICT_LABELS_INVOKED.get())
			.as("DictService.getLabels should be invoked for dropdowns")
			.isGreaterThan(0);

		// Read back and verify codes preserved via label<->value conversion
		byte[] bytes = resp.getContentAsByteArray();
		MockMultipartFile file = new MockMultipartFile("file", "users-dict.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new ByteArrayInputStream(bytes));

		List<ExcelImportVO> read = ExcelUtils.read(file, ExcelImportVO.class, true);
		Assertions.assertThat(read).hasSize(2);
		Assertions.assertThat(read.get(0).getId()).isEqualTo(1L);
		Assertions.assertThat(read.get(0).getSex()).isZero();
		Assertions.assertThat(read.get(0).getStatus()).isEqualTo(1);
		Assertions.assertThat(read.get(1).getId()).isEqualTo(2L);
		Assertions.assertThat(read.get(1).getSex()).isEqualTo(1);
		Assertions.assertThat(read.get(1).getStatus()).isZero();
	}

	@Test
	@DisplayName("skipEmptyRows=true excludes blank rows for ExcelImportVO")
	void testSkipEmptyRowsForExcelImportVO() throws IOException {
		List<ExcelImportVO> list = new ArrayList<>();
		list.add(row(1L, "eve", "Eve", "eve@example.com", "13700000003", 1, 1));
		list.add(new ExcelImportVO()); // empty
		list.add(row(2L, "mallory", "Mallory", "mallory@example.com", "13600000004", 0, 0));

		MockHttpServletResponse resp = new MockHttpServletResponse();
		ExcelUtils.write(resp, "skip.xlsx", "Users", ExcelImportVO.class, list);

		byte[] bytes = resp.getContentAsByteArray();
		MockMultipartFile file = new MockMultipartFile("file", "skip.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new ByteArrayInputStream(bytes));

		List<ExcelImportVO> read = ExcelUtils.read(file, ExcelImportVO.class, true);
		Assertions.assertThat(read).hasSize(2);
		Assertions.assertThat(read).extracting(ExcelImportVO::getUsername).containsExactly("eve", "mallory");
	}

	private static ExcelImportVO row(Long id, String username, String nickname, String email, String mobile,
			Integer sex, Integer status) {
		ExcelImportVO vo = new ExcelImportVO();
		vo.setId(id);
		vo.setUsername(username);
		vo.setNickname(nickname);
		vo.setEmail(email);
		vo.setMobile(mobile);
		vo.setSex(sex);
		vo.setStatus(status);
		return vo;
	}

	// --- Test beans ---
	public static class IdOptionsFunction implements ExcelColumnSelectFunction {

		@Override
		public String getName() {
			return "idOptions";
		}

		@Override
		public List<String> getOptions() {
			ID_OPTIONS_INVOKED.incrementAndGet();
			return java.util.Arrays.asList("1", "2", "3");
		}

	}

	public static class TestDictService implements DictService {

		private final Map<String, Map<String, String>> labelToValue = new HashMap<>();

		private final Map<String, Map<String, String>> valueToLabel = new HashMap<>();

		public TestDictService() {
			put("user_sex", "男", "0");
			put("user_sex", "女", "1");
			put("common_status", "启用", "1");
			put("common_status", "禁用", "0");
		}

		private void put(String type, String label, String value) {
			labelToValue.computeIfAbsent(type, k -> new HashMap<>()).put(label, value);
			valueToLabel.computeIfAbsent(type, k -> new HashMap<>()).put(value, label);
		}

		@Override
		public String getLabel(String type, String value) {
			return valueToLabel.getOrDefault(type, Collections.emptyMap()).get(value);
		}

		@Override
		public String getValue(String type, String label) {
			return labelToValue.getOrDefault(type, Collections.emptyMap()).get(label);
		}

		@Override
		public List<String> getLabels(String type) {
			DICT_LABELS_INVOKED.incrementAndGet();
			return new ArrayList<>(labelToValue.getOrDefault(type, Collections.emptyMap()).keySet());
		}

		@Override
		public List<DictItem> getItems(String type) {
			// Not required for current tests
			return Collections.emptyList();
		}

	}

}
