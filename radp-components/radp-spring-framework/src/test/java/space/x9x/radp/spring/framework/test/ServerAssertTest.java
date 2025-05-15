package space.x9x.radp.spring.framework.test;

import org.junit.jupiter.api.Test;
import space.x9x.radp.commons.lang.format.MessageFormatter;
import space.x9x.radp.spring.framework.type.common.ServerAssert;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerAssertTest {

    @Test
    void test() {
        String messagePattern = "hello {}";
        Object[] params = {"world"};
        String message = MessageFormatter.arrayFormat(messagePattern, params).getMessage();
        assertEquals("hello world", message);

        try {
            ServerAssert.notNull(null, "10000", "world");
        } catch (Exception e) {
            assertEquals("hello world", e.getMessage());
        }
    }
}