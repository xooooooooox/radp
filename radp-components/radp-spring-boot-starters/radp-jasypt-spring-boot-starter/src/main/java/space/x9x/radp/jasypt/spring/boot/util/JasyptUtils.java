package space.x9x.radp.jasypt.spring.boot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author x9x
 * @since 2024-10-17 10:30
 */
@Slf4j
@UtilityClass
public class JasyptUtils {


    /**
     * 自定义 PBE 加密器
     * <pre>
     * PBEWithMD5AndDES
     * PBEWithSHA1AndDESede
     * PBEWithSHA1AndRC2_40
     * PBEWithSHA1AndRC2_128
     * PBEWithSHA1AndRC4_40
     * PBEWithSHA1AndRC4_128
     * PBEWithMD5AndTripleDES
     * PBEWithHmacSHA1AndAES_128
     * PBEWithHmacSHA224AndAES_128
     * PBEWithHmacSHA256AndAES_128
     * PBEWithHmacSHA384AndAES_128
     * PBEWithHmacSHA512AndAES_128
     * PBEWithHmacSHA1AndAES_256
     * PBEWithHmacSHA224AndAES_256
     * PBEWithHmacSHA256AndAES_256
     * PBEWithHmacSHA384AndAES_256
     * PBEWithHmacSHA512AndAES_256
     * </pre>
     *
     * @param originText   原始文本
     * @param pbeAlgorithm 使用的加密算法
     * @param password     盐值
     * @return 加密后的文本
     */
    public String customPBEEncrypt(String originText, String pbeAlgorithm, String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(pbeAlgorithm);
        encryptor.setPassword(password);
        return encrypt(encryptor, originText);
    }

    /**
     * 自定义 PBE 解密器
     *
     * @param encryptedText 密文
     * @param pbeAlgorithm  使用的加密算法
     * @param password      盐值
     * @return 加密后的文本
     */
    public String customPBEDecrypt(String encryptedText, String pbeAlgorithm, String password) {
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setAlgorithm(pbeAlgorithm);
        decryptor.setPassword(password);
        return decrypt(decryptor, encryptedText);
    }

    public String encrypt(StringEncryptor stringEncryptor, String originText) {
        String encryptedText = stringEncryptor.encrypt(originText);
        log.info("origin text '{}', encrypted text '{}'", originText, encryptedText);
        return encryptedText;
    }

    public String decrypt(StringEncryptor stringEncryptor, String encryptedText) {
        String decryptedText = stringEncryptor.decrypt(encryptedText);
        log.info("encrypted text '{}', decrypted text '{}'", encryptedText, decryptedText);
        return decryptedText;
    }
}
