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
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Tests for {@link LambdaQueryWrapperX} helpers.
 *
 * @author Junie
 * @since 2025-12-05 00:06
 */
class LambdaQueryWrapperXTests {

	static class E {

		private String name;

		private Integer age;

		private Integer score;

		E(String name, Integer age, Integer score) {
			this.name = name;
			this.age = age;
			this.score = score;
		}

		String getName() {
			return name;
		}

		Integer getAge() {
			return age;
		}

		Integer getScore() {
			return score;
		}

	}

	@Test
	void likeIfPresent_shouldAppendWhenHasText_andIgnoreWhenEmpty() {
		LambdaQueryWrapperX<E> w1 = new LambdaQueryWrapperX<>();
		w1.likeIfPresent(E::getName, "tom");
		assertThat(w1.getSqlSegment().toLowerCase()).contains("like");

		LambdaQueryWrapperX<E> w2 = new LambdaQueryWrapperX<>();
		w2.likeIfPresent(E::getName, " ");
		assertThat(w2.getSqlSegment()).isNull();
	}

	@Test
	void inIfPresent_shouldWorkForCollectionsAndArrays() {
		LambdaQueryWrapperX<E> w1 = new LambdaQueryWrapperX<>();
		w1.inIfPresent(E::getAge, Arrays.asList(1, 2));
		assertThat(w1.getSqlSegment().toLowerCase()).contains(" in ");

		LambdaQueryWrapperX<E> w2 = new LambdaQueryWrapperX<>();
		w2.inIfPresent(E::getAge, 1, 2, 3);
		assertThat(w2.getSqlSegment().toLowerCase()).contains(" in ");

		LambdaQueryWrapperX<E> w3 = new LambdaQueryWrapperX<>();
		w3.inIfPresent(E::getAge, Collections.emptyList());
		assertThat(w3.getSqlSegment()).isNull();
	}

	@Test
	void eqAndComparisonsIfPresent_shouldBeConditional() {
		LambdaQueryWrapperX<E> w1 = new LambdaQueryWrapperX<>();
		w1.eqIfPresent(E::getAge, 18)
			.neIfPresent(E::getAge, 20)
			.gtIfPresent(E::getScore, 60)
			.geIfPresent(E::getScore, 60)
			.ltIfPresent(E::getScore, 100)
			.leIfPresent(E::getScore, 100);
		String sql = w1.getSqlSegment();
		assertThat(sql).contains("age");
		assertThat(sql).contains("score");

		LambdaQueryWrapperX<E> w2 = new LambdaQueryWrapperX<>();
		w2.eqIfPresent(E::getAge, null)
			.neIfPresent(E::getAge, null)
			.gtIfPresent(E::getScore, null)
			.geIfPresent(E::getScore, null)
			.ltIfPresent(E::getScore, null)
			.leIfPresent(E::getScore, null);
		assertThat(w2.getSqlSegment()).isNull();
	}

	@Test
	void betweenIfPresent_shouldHandleVariousCombinations() {
		LambdaQueryWrapperX<E> w1 = new LambdaQueryWrapperX<>();
		w1.betweenIfPresent(E::getScore, 1, 5);
		assertThat(w1.getSqlSegment().toLowerCase()).contains("between");

		LambdaQueryWrapperX<E> w2 = new LambdaQueryWrapperX<>();
		w2.betweenIfPresent(E::getScore, 1, null);
		assertThat(w2.getSqlSegment()).contains(">=");

		LambdaQueryWrapperX<E> w3 = new LambdaQueryWrapperX<>();
		w3.betweenIfPresent(E::getScore, null, 5);
		assertThat(w3.getSqlSegment()).contains("<=");

		LambdaQueryWrapperX<E> w4 = new LambdaQueryWrapperX<>();
		w4.betweenIfPresent(E::getScore, new Object[] { null, null });
		assertThat(w4.getSqlSegment()).isNull();
	}

	@Test
	void overriddenMethods_shouldReturnSameInstanceAndApplySql() {
		LambdaQueryWrapperX<E> w = new LambdaQueryWrapperX<>();
		assertThat(w.eq(E::getName, "a")).isSameAs(w);
		assertThat(w.in(E::getAge, Arrays.asList(1, 2))).isSameAs(w);
		assertThat(w.orderByDesc(E::getScore)).isSameAs(w);
		assertThatCode(() -> w.last("limit 1")).doesNotThrowAnyException();
	}

}
