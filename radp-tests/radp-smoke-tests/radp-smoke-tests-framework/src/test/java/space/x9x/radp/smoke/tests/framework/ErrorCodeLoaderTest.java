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
