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

package space.x9x.radp.spring.framework.dto.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import space.x9x.radp.spring.framework.dto.Result;
import space.x9x.radp.spring.framework.dto.extension.ResponseBuilder;
import space.x9x.radp.spring.framework.json.support.JSONHelper;

/**
 * @author x9x
 * @since 2025-11-23 12:50
 */
class ResponseBuilderTest {

	@DisplayName("场景:不带占位符")
	@Test
	void test() {
		String msg = "msg";
		Result result = (Result) ResponseBuilder.builder().buildFailure("0400", msg);
		System.out.println(JSONHelper.json().toJSONString(result));
		Assertions.assertThat(result.getCode()).isEqualTo("0400");
		Assertions.assertThat(result.getMsg()).isEqualTo(msg);

	}

	@DisplayName("场景:带占位符")
	@Test
	void test_with_params() {
		String msg = "msg {} {}";
		Result result;
		result = (Result) ResponseBuilder.builder().buildFailure("0400", msg);
		System.out.println(JSONHelper.json().toJSONString(result));
		Assertions.assertThat(result.getCode()).isEqualTo("0400");
		Assertions.assertThat(result.getMsg()).isEqualTo("msg {} {}");
		result = (Result) ResponseBuilder.builder().buildFailure("0400", msg, "a", "b");
		System.out.println(JSONHelper.json().toJSONString(result));
		Assertions.assertThat(result.getCode()).isEqualTo("0400");
		Assertions.assertThat(result.getMsg()).isEqualTo("msg a b");
		result = (Result) ResponseBuilder.builder().buildFailure("0400", msg, "a");
		System.out.println(JSONHelper.json().toJSONString(result));
		Assertions.assertThat(result.getCode()).isEqualTo("0400");
		Assertions.assertThat(result.getMsg()).isEqualTo("msg a {}");
		result = (Result) ResponseBuilder.builder().buildFailure("0400", msg, "a", "b", "c");
		System.out.println(JSONHelper.json().toJSONString(result));
		Assertions.assertThat(result.getCode()).isEqualTo("0400");
		Assertions.assertThat(result.getMsg()).isEqualTo("msg a b");
	}

	@DisplayName("场景:异常")
	void test_with_exception() {
		String msg = "this is exception message";
		Result result;
		try {
			throw new RuntimeException(msg);
		}
		catch (Exception e) {
			result = (Result) ResponseBuilder.builder().buildFailure("0400", e.getMessage());
			System.out.println(JSONHelper.json().toJSONString(result));
			Assertions.assertThat(result.getCode()).isEqualTo("0400");
			Assertions.assertThat(result.getMsg()).isEqualTo(msg);
		}
	}

}
