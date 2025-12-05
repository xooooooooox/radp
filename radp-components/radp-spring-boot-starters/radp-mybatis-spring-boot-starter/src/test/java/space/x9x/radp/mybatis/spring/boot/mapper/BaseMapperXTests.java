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

package space.x9x.radp.mybatis.spring.boot.mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import space.x9x.radp.spring.framework.dto.PageParam;
import space.x9x.radp.spring.framework.dto.PageResult;
import space.x9x.radp.spring.framework.dto.SortingField;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BaseMapperX} default methods.
 *
 * We use Mockito with CALLS_REAL_METHODS so default methods are executed while abstract
 * CRUD methods are stubbed.
 *
 * @author RADP x9x
 * @since 2025-12-05 00:06
 */
class BaseMapperXTests {

	static class E {

		private Long id;

		private String name;

		E(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		Long getId() {
			return id;
		}

		String getName() {
			return name;
		}

	}

	static class Dto {

		private final String name;

		Dto(String name) {
			this.name = name;
		}

		String getName() {
			return name;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void selectPage_shouldSupportNoPaginationAndPagedBranches() {
		BaseMapperX<E> mapper = mock(BaseMapperX.class, Mockito.CALLS_REAL_METHODS);

		List<E> list = Arrays.asList(new E(1L, "A"), new E(2L, "B"), new E(3L, "C"));
		given(mapper.selectList(any(Wrapper.class))).willReturn(list);

		// NO_PAGINATION branch
		PageParam noPg = new PageParam(1, PageParam.NO_PAGINATION);
		QueryWrapper<E> wrapper = new QueryWrapper<>();
		PageResult<E> r1 = mapper.selectPage(noPg,
				Collections.singletonList(new SortingField("name", SortingField.ASC)), wrapper);
		assertThat(r1.getTotal()).isEqualTo(3);
		assertThat(r1.getData()).hasSize(3);

		// paged branch
		willAnswer(inv -> {
			IPage<E> p = inv.getArgument(0);
			p.setRecords(list);
			p.setTotal(list.size());
			return p;
		}).given(mapper).selectPage(any(IPage.class), any(Wrapper.class));

		PageParam pg = new PageParam(2, 10);
		PageResult<E> r2 = mapper.selectPage(pg, null, new QueryWrapper<>());
		assertThat(r2.getTotal()).isEqualTo(3);
		assertThat(r2.getData()).hasSize(3);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void selectJoinPage_shouldSupportBothOverloads() {
		BaseMapperX<E> mapper = mock(BaseMapperX.class, Mockito.CALLS_REAL_METHODS);
		List<Dto> dtoList = Arrays.asList(new Dto("a"), new Dto("b"));

		// NO_PAGINATION branch for lambda wrapper overload
		given(mapper.selectJoinList(eq(Dto.class), any(MPJLambdaWrapper.class))).willReturn(dtoList);
		PageParam noPg = new PageParam(1, PageParam.NO_PAGINATION);
		PageResult<Dto> jr1 = mapper.selectJoinPage(noPg, Dto.class, new MPJLambdaWrapper<>());
		assertThat(jr1.getTotal()).isEqualTo(2);
		assertThat(jr1.getData()).hasSize(2);

		// paged branch for lambda wrapper overload
		willAnswer(inv -> {
			IPage<Dto> p = inv.getArgument(0);
			p.setRecords(dtoList);
			p.setTotal(dtoList.size());
			return p;
		}).given(mapper).selectJoinPage(any(IPage.class), eq(Dto.class), any(MPJLambdaWrapper.class));
		PageParam pg = new PageParam(1, 10);
		PageResult<Dto> jr2 = mapper.selectJoinPage(pg, Dto.class, new MPJLambdaWrapper<>());
		assertThat(jr2.getTotal()).isEqualTo(2);
		assertThat(jr2.getData()).hasSize(2);

		// overload with MPJBaseJoin
		MPJBaseJoin<E> join = mock(MPJBaseJoin.class);
		willAnswer(inv -> {
			IPage<Dto> p = inv.getArgument(0);
			p.setRecords(dtoList);
			p.setTotal(dtoList.size());
			return p;
		}).given(mapper).selectJoinPage(any(IPage.class), eq(Dto.class), eq(join));
		PageResult<Dto> jr3 = mapper.selectJoinPage(pg, Dto.class, join);
		assertThat(jr3.getTotal()).isEqualTo(2);
		assertThat(jr3.getData()).hasSize(2);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void simpleHelpers_shouldDelegateToBaseCrudMethods() {
		BaseMapperX<E> mapper = mock(BaseMapperX.class, Mockito.CALLS_REAL_METHODS);
		E e = new E(1L, "A");
		given(mapper.selectOne(any(Wrapper.class))).willReturn(e);
		given(mapper.selectCount(any(Wrapper.class))).willReturn(5L);
		given(mapper.selectList(any(Wrapper.class))).willReturn(Arrays.asList(e));

		assertThat(mapper.selectOne("name", "A")).isNotNull();
		assertThat(mapper.selectOne(E::getName, "A")).isNotNull();
		assertThat(mapper.selectOne("name", "A", "id", 1L)).isNotNull();
		assertThat(mapper.selectOne(E::getName, "A", E::getId, 1L)).isNotNull();
		assertThat(mapper.selectOne(E::getName, "A", E::getId, 1L, E::getName, "A")).isNotNull();

		assertThat(mapper.selectCount()).isEqualTo(5);
		assertThat(mapper.selectCount("name", "A")).isEqualTo(5);
		assertThat(mapper.selectCount(E::getName, "A")).isEqualTo(5);

		assertThat(mapper.selectList()).hasSize(1);
		assertThat(mapper.selectList("name", "A")).hasSize(1);
		assertThat(mapper.selectList(E::getName, "A")).hasSize(1);
		assertThat(mapper.selectList("id", Arrays.asList(1L))).hasSize(1);
		assertThat(mapper.selectList(E::getId, Arrays.asList(1L))).hasSize(1);
		assertThat(mapper.selectList(E::getId, 1L, E::getName, "A")).hasSize(1);
	}

}
