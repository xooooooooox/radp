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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExceptionUtilsTest {

	@Test
	void test_serverException() {
		try {
			ServerAssert.notNull(null, "10000", "world");
		}
		catch (Exception e) {
			assertEquals("hello world", e.getMessage());
		}
		ServerException serverException = ExceptionUtils.serverException("10000", "world");
		assertEquals("hello world", serverException.getMessage());
	}

	@Test
	void test_serverExceptionWithThrowable() {
		try {
			try {
				throw new IllegalArgumentException("aaa");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("10000", "world", e);
			}
		}
		catch (ServerException se) {
			assertEquals("hello world", se.getMessage());
			assertEquals("10000", se.getErrCode());
			assertEquals(IllegalArgumentException.class, se.getCause().getClass());
			assertEquals("aaa", se.getCause().getMessage());
		}
		try {
			try {
				throw new IOException("bbb");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("10002", "world", "!!!", e);
			}
		}
		catch (ServerException e) {
			assertEquals("hello world !!!", e.getMessage());
			assertEquals("10002", e.getErrCode());
			assertEquals(IOException.class, e.getCause().getClass());
			assertEquals("bbb", e.getCause().getMessage());
		}
		try {
			try {
				throw new IOException("ccc");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("10001", e);
			}
		}
		catch (ServerException e) {
			assertEquals("test", e.getMessage());
			assertEquals("10001", e.getErrCode());
			assertEquals(IOException.class, e.getCause().getClass());
			assertEquals("ccc", e.getCause().getMessage());
		}
		try {
			try {
				throw new IOException("ddd");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("10000", e);
			}
		}
		catch (ServerException e) {
			assertEquals("hello {}", e.getMessage());
			assertEquals("10000", e.getErrCode());
			assertEquals(IOException.class, e.getCause().getClass());
			assertEquals("ddd", e.getCause().getMessage());
		}
		try {
			try {
				throw new IOException("fff");
			}
			catch (Exception e) {
				throw ExceptionUtils.serverException("10000");
			}
		}
		catch (ServerException e) {
			assertEquals("hello {}", e.getMessage());
			assertEquals("10000", e.getErrCode());
			// No cause should be set since we didn't pass the exception
			assertNull(e.getCause());
		}
	}

	@Test
	void test_clientException() {
		try {
			ClientAssert.notNull(null, "10000", "world");
		}
		catch (Exception e) {
			assertEquals("hello world", e.getMessage());
		}
		ClientException clientException = ExceptionUtils.clientException("10000", "world");
		assertEquals("hello world", clientException.getMessage());
	}

	@Test
	void test_thirdServiceException() {
		try {
			ThirdServiceAssert.notNull(null, "10000", "world");
		}
		catch (Exception e) {
			assertEquals("hello world", e.getMessage());
		}
		ThirdServiceException thirdServiceException = ExceptionUtils.thirdServiceException("10000", "world");
		assertEquals("hello world", thirdServiceException.getMessage());
	}

}
