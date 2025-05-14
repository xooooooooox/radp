package space.x9x.radp.spring.framework.test;

import org.junit.jupiter.api.Test;
import space.x9x.radp.commons.lang.format.MessageFormatter;
import space.x9x.radp.spring.framework.type.common.ServerAssert;

class ServerAssertTest {

    @Test
    void test() {
        Object o = null;
        String id = "hello";
        Object[] params = new Object[]{id};
        System.out.println(MessageFormatter.arrayFormat("提示词模板{}, 文章生成失败", params).getMessage());
        ServerAssert.notNull(o, "10000", id);
    }
}