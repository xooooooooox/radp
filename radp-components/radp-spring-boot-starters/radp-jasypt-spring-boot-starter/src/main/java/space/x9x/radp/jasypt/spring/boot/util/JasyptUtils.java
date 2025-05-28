/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.jasypt.spring.boot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Utility class for Jasypt encryption and decryption operations. This class provides
 * methods for encrypting and decrypting text using various Password-Based Encryption
 * (PBE) algorithms supported by Jasypt.
 *
 * @author IO x9x
 * @since 2024-10-17 10:30
 */
@Slf4j
@UtilityClass
public class JasyptUtils {

	/**
	 * Custom PBE (Password-Based Encryption) encryptor. This method creates a PBE
	 * encryptor with the specified algorithm and password, then encrypts the original
	 * text.
	 *
	 * <pre>
	 * Supported PBE algorithms include:
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
	 * @param originText the original text to encrypt
	 * @param pbeAlgorithm the PBE algorithm to use
	 * @param password the password (salt) for encryption
	 * @return the encrypted text
	 */
	public String customPBEEncrypt(String originText, String pbeAlgorithm, String password) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setAlgorithm(pbeAlgorithm);
		encryptor.setPassword(password);
		return encrypt(encryptor, originText);
	}

	/**
	 * Custom PBE (Password-Based Encryption) decryptor. This method creates a PBE
	 * decryptor with the specified algorithm and password, then decrypts the encrypted
	 * text.
	 * @param encryptedText the encrypted text to decrypt
	 * @param pbeAlgorithm the PBE algorithm to use
	 * @param password the password (salt) for decryption
	 * @return the decrypted text
	 */
	public String customPBEDecrypt(String encryptedText, String pbeAlgorithm, String password) {
		StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
		decryptor.setAlgorithm(pbeAlgorithm);
		decryptor.setPassword(password);
		return decrypt(decryptor, encryptedText);
	}

	/**
	 * Encrypts text using the provided StringEncryptor. This method uses the provided
	 * encryptor to encrypt the original text and logs both the original and encrypted
	 * text.
	 * @param stringEncryptor the encryptor to use for encryption
	 * @param originText the original text to encrypt
	 * @return the encrypted text
	 */
	public String encrypt(StringEncryptor stringEncryptor, String originText) {
		String encryptedText = stringEncryptor.encrypt(originText);
		log.info("origin text '{}', encrypted text '{}'", originText, encryptedText);
		return encryptedText;
	}

	/**
	 * Decrypts text using the provided StringEncryptor. This method uses the provided
	 * encryptor to decrypt the encrypted text and logs both the encrypted and decrypted
	 * text.
	 * @param stringEncryptor the encryptor to use for decryption
	 * @param encryptedText the encrypted text to decrypt
	 * @return the decrypted text
	 */
	public String decrypt(StringEncryptor stringEncryptor, String encryptedText) {
		String decryptedText = stringEncryptor.decrypt(encryptedText);
		log.info("encrypted text '{}', decrypted text '{}'", encryptedText, decryptedText);
		return decryptedText;
	}

}
