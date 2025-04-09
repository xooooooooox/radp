package space.x9x.radp.jasypt.spring.boot.test;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.x9x.radp.jasypt.spring.boot.TestApplication;
import space.x9x.radp.jasypt.spring.boot.util.JasyptUtils;

/**
 * @author x9x
 * @since 2025-04-09 11:41
 */
@SpringBootTest(classes = TestApplication.class)
@Slf4j
class JasyptTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    void test() {
        String[] plainTexts = {"root", "123456"};
        for (String plainText : plainTexts) {
            // 使用当前 SpringBoot 上下文已经集成的 jasypt 加密器进行加密
            String encrypted1 = JasyptUtils.encrypt(stringEncryptor, plainText);
            // 使用当前 SpringBoot 上下文已经集成的 jasypt 加密器进行解密
            Assertions.assertEquals(plainText, JasyptUtils.decrypt(stringEncryptor, encrypted1));
            // 使用指定的 jasypt 加密器进行加密
            String encrypted2 = JasyptUtils.customPBEEncrypt(plainText, "PBEWithMD5AndDES", "dsaf#,jds.klfj1");
            // 使用指定的 jasypt 加密器进行解密
            Assertions.assertEquals(plainText, JasyptUtils.customPBEDecrypt(encrypted2, "PBEWithMD5AndDES", "dsaf#,jds.klfj1"));
        }
    }
}
