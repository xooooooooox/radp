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

package space.x9x.radp.spring.framework.error.util;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import space.x9x.radp.spring.framework.error.ClientException;
import space.x9x.radp.spring.framework.error.ServerException;
import space.x9x.radp.spring.framework.error.ThirdServiceException;
import space.x9x.radp.spring.framework.type.exception.asserts.ClientAssert;
import space.x9x.radp.spring.framework.type.exception.asserts.ServerAssert;
import space.x9x.radp.spring.framework.type.exception.asserts.ThirdServiceAssert;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionUtilsTest {

	@Test
	void test_serverException() {
		try {
			ServerAssert.notNull(null, "TEST_0001", "world");
		}
		catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("hello world");
		}
		ServerException serverException = ExceptionUtils.serverException("TEST_0001", "world");
		assertThat(serverException.getMessage()).isEqualTo("hello world");
	}

	@Test
	void test_serverExceptionWithThrowable() {
		try {
			try {
				throw new IllegalArgumentException("aaa");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("TEST_0001", "world", e);
			}
		}
		catch (ServerException se) {
			assertThat(se.getMessage()).isEqualTo("hello world");
			assertThat(se.getErrCode()).isEqualTo("TEST_0001");
			assertThat(se.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
			assertThat(se.getCause().getMessage()).isEqualTo("aaa");
		}
		try {
			try {
				throw new IOException("bbb");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("TEST_0003", "world", "!!!", e);
			}
		}
		catch (ServerException e) {
			assertThat(e.getMessage()).isEqualTo("hello world !!!");
			assertThat(e.getErrCode()).isEqualTo("TEST_0003");
			assertThat(e.getCause().getClass()).isEqualTo(IOException.class);
			assertThat(e.getCause().getMessage()).isEqualTo("bbb");
		}
		try {
			try {
				throw new IOException("ccc");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("TEST_0002", e);
			}
		}
		catch (ServerException e) {
			assertThat(e.getMessage()).isEqualTo("test");
			assertThat(e.getErrCode()).isEqualTo("TEST_0002");
			assertThat(e.getCause().getClass()).isEqualTo(IOException.class);
			assertThat(e.getCause().getMessage()).isEqualTo("ccc");
		}
		try {
			try {
				throw new IOException("ddd");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("TEST_0001", e);
			}
		}
		catch (ServerException e) {
			assertThat(e.getMessage()).isEqualTo("hello {}");
			assertThat(e.getErrCode()).isEqualTo("TEST_0001");
			assertThat(e.getCause().getClass()).isEqualTo(IOException.class);
			assertThat(e.getCause().getMessage()).isEqualTo("ddd");
		}
		try {
			try {
				throw new IOException("fff");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("TEST_0001");
			}
		}
		catch (ServerException e) {
			assertThat(e.getMessage()).isEqualTo("hello {}");
			assertThat(e.getErrCode()).isEqualTo("TEST_0001");
			// No cause should be set since we didn't pass the exception
			assertThat(e.getCause()).isNull();
		}
	}

	@Test
	void test_clientException() {
		try {
			ClientAssert.notNull(null, "TEST_0001", "world");
		}
		catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("hello world");
		}
		ClientException clientException = ExceptionUtils.clientException("TEST_0001", "world");
		assertThat(clientException.getMessage()).isEqualTo("hello world");
	}

	@Test
	void test_thirdServiceException() {
		try {
			ThirdServiceAssert.notNull(null, "TEST_0001", "world");
		}
		catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("hello world");
		}
		ThirdServiceException thirdServiceException = ExceptionUtils.thirdServiceException("TEST_0001", "world");
		assertThat(thirdServiceException.getMessage()).isEqualTo("hello world");
	}

}
