#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author x9x
 * @since 2024-11-15 11:56
 */
@SpringBootTest
@Slf4j
class ApiTest {

    @Test
    void test() {
        log.info("do something");
    }
}
