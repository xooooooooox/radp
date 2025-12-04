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

package space.x9x.radp.mybatis.spring.boot.extension;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link QueryWrapperX} helpers.
 *
 * @author RADP x9x
 * @since 2025-12-05 00:06
 */
class QueryWrapperXTests {

	@Test
	void likeIfPresent_shouldAppendWhenHasText_andIgnoreWhenEmpty() {
		QueryWrapperX<Object> w1 = new QueryWrapperX<>();
		w1.likeIfPresent("name", "tom");
		assertThat(w1.getSqlSegment().toLowerCase()).contains("like");

		QueryWrapperX<Object> w2 = new QueryWrapperX<>();
		w2.likeIfPresent("name", " ");
		assertThat(w2.getSqlSegment()).isNullOrEmpty();
	}

	@Test
	void inIfPresent_shouldWorkForCollectionsAndArrays() {
		QueryWrapperX<Object> w1 = new QueryWrapperX<>();
		w1.inIfPresent("id", Arrays.asList(1, 2));
		assertThat(w1.getSqlSegment().toLowerCase()).contains(" in ");

		QueryWrapperX<Object> w2 = new QueryWrapperX<>();
		w2.inIfPresent("id", new Object[] { 1, 2, 3 });
		assertThat(w2.getSqlSegment().toLowerCase()).contains(" in ");

		QueryWrapperX<Object> w3 = new QueryWrapperX<>();
		w3.inIfPresent("id", Collections.emptyList());
		assertThat(w3.getSqlSegment()).isNullOrEmpty();
	}

	@Test
	void eqAndComparisonsIfPresent_shouldBeConditional() {
		QueryWrapperX<Object> w1 = new QueryWrapperX<>();
		w1.eqIfPresent("age", 18)
			.neIfPresent("age", 20)
			.gtIfPresent("score", 60)
			.geIfPresent("score", 60)
			.ltIfPresent("score", 100)
			.leIfPresent("score", 100);
		String sql = w1.getSqlSegment();
		assertThat(sql).contains("age");
		assertThat(sql).contains("score");

		QueryWrapperX<Object> w2 = new QueryWrapperX<>();
		w2.eqIfPresent("age", null)
			.neIfPresent("age", null)
			.gtIfPresent("x", null)
			.geIfPresent("x", null)
			.ltIfPresent("x", null)
			.leIfPresent("x", null);
		assertThat(w2.getSqlSegment()).isNullOrEmpty();
	}

	@Test
	void betweenIfPresent_shouldHandleVariousCombinations() {
		QueryWrapperX<Object> w1 = new QueryWrapperX<>();
		w1.betweenIfPresent("score", 1, 5);
		assertThat(w1.getSqlSegment().toLowerCase()).contains("between");

		QueryWrapperX<Object> w2 = new QueryWrapperX<>();
		w2.betweenIfPresent("score", 1, null);
		assertThat(w2.getSqlSegment()).contains(">=");

		QueryWrapperX<Object> w3 = new QueryWrapperX<>();
		w3.betweenIfPresent("score", null, 5);
		assertThat(w3.getSqlSegment()).contains("<=");

		QueryWrapperX<Object> w4 = new QueryWrapperX<>();
		w4.betweenIfPresent("score", (Object[]) null);
		assertThat(w4.getSqlSegment()).isNullOrEmpty();
	}

	@Test
	void overriddenMethods_shouldReturnSameInstanceAndApplySql() {
		QueryWrapperX<Object> w = new QueryWrapperX<>();
		assertThat(w.eq("name", "a")).isSameAs(w);
		assertThat(w.in("id", Arrays.asList(1, 2))).isSameAs(w);
		assertThat(w.orderByDesc("created_at")).isSameAs(w);
		assertThat(w.last("limit 1")).isSameAs(w);
		assertThat(w.getSqlSegment().toLowerCase()).contains("order by");
	}

}
