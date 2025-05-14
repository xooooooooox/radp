package space.x9x.radp.spring.framework.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import space.x9x.radp.commons.lang.format.MessageFormatter;
import space.x9x.radp.spring.framework.type.common.ServerAssert;

class ServerAssertTest {

    @Test
    void test() {
        String messagePattern = "test {}";
        Object[] params = {"world"};
        String message = MessageFormatter.arrayFormat(messagePattern, params).getMessage();
        Assertions.assertEquals("test world", message);

        try {
            ServerAssert.notNull(null, "10000", "world");
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.assertEquals("test world", e.getMessage());
        }
    }
}