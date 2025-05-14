package space.x9x.radp.spring.framework.error.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import space.x9x.radp.spring.framework.error.ServerException;
import space.x9x.radp.spring.framework.type.common.ServerAssert;

class ExceptionUtilsTest {

    @Test
    void test() {
        try {
            ServerAssert.notNull(null, "10000", "world");
        } catch (Exception e) {
            Assertions.assertEquals("test world", e.getMessage());
        }
        ServerException serverException = ExceptionUtils.serverException("10000", "world");
        Assertions.assertEquals("test world", serverException.getMessage());
    }

}
