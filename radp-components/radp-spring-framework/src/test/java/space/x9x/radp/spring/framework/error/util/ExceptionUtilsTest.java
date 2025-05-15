package space.x9x.radp.spring.framework.error.util;

import org.junit.jupiter.api.Test;
import space.x9x.radp.spring.framework.error.ClientException;
import space.x9x.radp.spring.framework.error.ServerException;
import space.x9x.radp.spring.framework.error.ThirdServiceException;
import space.x9x.radp.spring.framework.type.common.ClientAssert;
import space.x9x.radp.spring.framework.type.common.ServerAssert;
import space.x9x.radp.spring.framework.type.common.ThirdServiceAssert;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionUtilsTest {

    @Test
    void test_serverException() {
        try {
            ServerAssert.notNull(null, "10000", "world");
        } catch (Exception e) {
            assertEquals("test world", e.getMessage());
        }
        ServerException serverException = ExceptionUtils.serverException("10000", "world");
        assertEquals("test world", serverException.getMessage());
    }

    @Test
    void test_clientException() {
        try {
            ClientAssert.notNull(null, "10000", "world");
        } catch (Exception e) {
            assertEquals("test world", e.getMessage());
        }
        ClientException clientException = ExceptionUtils.clientException("10000", "world");
        assertEquals("test world", clientException.getMessage());
    }

    @Test
    void test_thirdServiceException() {
        try {
            ThirdServiceAssert.notNull(null, "10000", "world");
        } catch (Exception e) {
            assertEquals("test world", e.getMessage());
        }
        ThirdServiceException thirdServiceException = ExceptionUtils.thirdServiceException("10000", "world");
        assertEquals("test world", thirdServiceException.getMessage());
    }

}
