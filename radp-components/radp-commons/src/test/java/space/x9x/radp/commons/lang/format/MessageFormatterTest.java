package space.x9x.radp.commons.lang.format;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class MessageFormatterTest {

    @Test
    void test_arrayFormat() {
        String messagePattern = "hello {}";
        Object[] params = {"world"};
        String message = MessageFormatter.arrayFormat(messagePattern, params).getMessage();
        Assertions.assertEquals("hello world", message);
    }

}