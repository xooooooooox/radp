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

package space.x9x.radp.smoke.tests.framework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.x9x.radp.smoke.tests.framework.asserts.ServerAssert;
import space.x9x.radp.spring.framework.error.ServerException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author IO x9x
 * @since 2025-05-16 03:02
 */
class ErrorCodeLoaderTest {

    @DisplayName("测试 resourceBundle 加载优先级以及 error message format")
    @Test
    void test() {
        try {
            ServerAssert.notNull(null, "10000", 123);
        } catch (ServerException e) {
            assertEquals("HELLO 123", e.getMessage());
            assertEquals("10000", e.getErrCode());
            assertNull(e.getCause());
        }
    }
}
