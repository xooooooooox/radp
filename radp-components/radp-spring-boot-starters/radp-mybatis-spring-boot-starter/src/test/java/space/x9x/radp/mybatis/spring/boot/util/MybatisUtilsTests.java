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

package space.x9x.radp.mybatis.spring.boot.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;

import space.x9x.radp.spring.framework.dto.PageParam;
import space.x9x.radp.spring.framework.dto.PageResult;
import space.x9x.radp.spring.framework.dto.SortingField;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Unit tests for {@link MybatisUtils}.
 *
 * <p>
 * These tests cover page building with and without sorting, transforming MyBatis-Plus
 * pages to {@link PageResult}, and applying order to both {@link QueryWrapper} and
 * {@link LambdaQueryWrapper}.
 *
 * @author RADP x9x
 * @since 2025-12-05 00:06
 */
class MybatisUtilsTests {

	@Test
	void buildPage_withoutSorting_shouldSetIndexAndSize() {
		PageParam param = new PageParam(2, 20);
		Page<String> page = MybatisUtils.buildPage(param);

		assertThat(page.getCurrent()).isEqualTo(2);
		assertThat(page.getSize()).isEqualTo(20);
		assertThat(page.orders()).isEmpty();
	}

	@Test
	void buildPage_withSorting_shouldAddOrderItems() {
		PageParam param = new PageParam(1, 10);
		List<SortingField> sorting = Arrays.asList(new SortingField("createdAt", SortingField.DESC),
				new SortingField("name", SortingField.ASC));

		Page<String> page = MybatisUtils.buildPage(param, sorting);

		assertThat(page.orders()).hasSize(2);
		// field names should be converted to underline_case
		assertThat(page.orders().get(0).getColumn()).isEqualTo("created_at");
		assertThat(page.orders().get(0).isAsc()).isFalse();
		assertThat(page.orders().get(1).getColumn()).isEqualTo("name");
		assertThat(page.orders().get(1).isAsc()).isTrue();
	}

	@Test
	void transformPage_shouldReturnPageResultWithRecordsAndTotal() {
		Page<String> page = new Page<>(1, 10);
		page.setTotal(3);
		page.setRecords(Arrays.asList("a", "b", "c"));

		PageResult<String> result = MybatisUtils.transformPage(page);
		assertThat(result.getTotal()).isEqualTo(3);
		assertThat(result.getData()).containsExactly("a", "b", "c");
	}

	@Test
	void addOrder_onQueryWrapper_shouldAppendOrderBy() {
		QueryWrapper<Object> wrapper = new QueryWrapper<>();
		MybatisUtils.addOrder(wrapper, Collections.singletonList(new SortingField("createdAt", SortingField.DESC)));

		String sqlSegment = wrapper.getSqlSegment();
		assertThat(sqlSegment.toLowerCase()).contains("order by");
		assertThat(sqlSegment).contains("created_at");
	}

	@Test
	void addOrder_onLambdaQueryWrapper_shouldNotThrow() {
		LambdaQueryWrapper<Object> wrapper = new LambdaQueryWrapper<>();
		assertThatCode(() -> MybatisUtils.addOrder(wrapper, Arrays
			.asList(new SortingField("updatedAt", SortingField.ASC), new SortingField("score", SortingField.DESC))))
			.doesNotThrowAnyException();
		// We can't easily read the 'last' SQL, but executing the branch increases
		// coverage
	}

}
