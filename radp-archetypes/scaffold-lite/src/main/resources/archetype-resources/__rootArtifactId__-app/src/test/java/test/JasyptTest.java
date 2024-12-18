#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test;

import space.x9x.radp.jasypt.spring.boot.util.JasyptUtils;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author x9x
 * @since 2024-10-13 12:48
 */
@SpringBootTest
@Slf4j
class JasyptTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    void test() {
        String[] plainTexts = {"root", "123456"};
        for (String plainText : plainTexts) {
            // 使用当前 SpringBoot 上下文已经集成的 jasypt 加密器进行加密
            JasyptUtils.encrypt(stringEncryptor, plainText);
            // 使用指定的 jasypt 加密器进行加密
            JasyptUtils.customPBEEncrypt(plainText, "PBEWithMD5AndDES", "dsaf#,jds.klfj1");
        }

        // 使用当前 SpringBoot 上下文已经集成的 jasypt 加密器进行解密
        Assertions.assertEquals("root", JasyptUtils.decrypt(stringEncryptor, "VDoSzyZ7jS/lvaBnAaIc3ePEo9bh6QEb"));
        Assertions.assertEquals("123456", JasyptUtils.decrypt(stringEncryptor, "uvgZQKBNymDMjPilZgaRHAE4LEeSW1xj"));
        // 使用指定的 jasypt 加密器进行解密
        Assertions.assertEquals("root", JasyptUtils.customPBEDecrypt("0R/8uhwNlqioUF0hbuJZWA==", "PBEWithMD5AndDES", "dsaf#,jds.klfj1"));
        Assertions.assertEquals("123456", JasyptUtils.customPBEDecrypt("lTEOkXx15CN6dH48COhuVA==", "PBEWithMD5AndDES", "dsaf#,jds.klfj1"));
    }
}