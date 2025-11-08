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

package space.x9x.radp.jasypt.spring.boot.encryptor;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;

/**
 * StringEncryptor implementation based on SM4 cipher using BouncyCastle.
 * <p>
 * Supported algorithm values (case-insensitive): - "SM4" (alias of
 * "SM4/CBC/PKCS5Padding") - "SM4/CBC/PKCS5Padding" - "SM4/ECB/PKCS5Padding"
 * <p>
 * The encryptor derives a 128-bit key from the provided password via UTF-8 bytes: - If
 * password bytes length >= 16, the first 16 bytes are used. - If shorter, it is
 * zero-padded to 16 bytes.
 * <p>
 * For CBC mode, a random 16-byte IV is generated per encryption and prefixed to the
 * ciphertext. The final output is Base64 encoded with the format: base64(IV ||
 * CIPHERTEXT). For ECB mode, there is no IV prefix.
 *
 * @author x9x
 * @since 2025-11-07 12:49
 */
public class Sm4StringEncryptor implements StringEncryptor {

	/**
	 * The provider name for BouncyCastle security provider.
	 */
	private static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;

	/**
	 * The cipher transformation string specifying algorithm, mode, and padding. Example
	 * value: "SM4/CBC/PKCS5Padding"
	 */
	private final String transformation;

	/**
	 * The encryption key bytes. Must be exactly 16 bytes for SM4 algorithm. Derived from
	 * the provided password and zero-padded if necessary.
	 */
	private final byte[] keyBytes;

	static {
		// Register an BC provider if not present
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	public Sm4StringEncryptor(String algorithm, String password) {
		this.transformation = normalizeAlgo(algorithm);
		this.keyBytes = deriveKey(password);
	}

	@Override
	public String encrypt(String message) {
		if (message == null) {
			return null;
		}
		try {
			String mode = modeOf(this.transformation);
			Cipher cipher = Cipher.getInstance(this.transformation, PROVIDER);
			SecretKey key = new SecretKeySpec(this.keyBytes, "SM4");
			byte[] plain = message.getBytes(StandardCharsets.UTF_8);
			if ("CBC".equals(mode)) {
				byte[] iv = new byte[16];
				new SecureRandom().nextBytes(iv);
				cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
				byte[] cipherText = cipher.doFinal(plain);
				byte[] out = new byte[iv.length + cipherText.length];
				System.arraycopy(iv, 0, out, 0, iv.length);
				System.arraycopy(cipherText, 0, out, iv.length, cipherText.length);
				return Base64.getEncoder().encodeToString(out);
			}
			else { // ECB
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] cipherText = cipher.doFinal(plain);
				return Base64.getEncoder().encodeToString(cipherText);
			}
		}
		catch (Exception ex) {
			throw new IllegalStateException("SM4 encrypt error", ex);
		}
	}

	@Override
	public String decrypt(String encryptedMessage) {
		if (encryptedMessage == null) {
			return null;
		}
		try {
			String mode = modeOf(this.transformation);
			Cipher cipher = Cipher.getInstance(this.transformation, PROVIDER);
			SecretKey key = new SecretKeySpec(this.keyBytes, "SM4");
			byte[] all = Base64.getDecoder().decode(encryptedMessage);
			if ("CBC".equals(mode)) {
				if (all.length < 16) {
					throw new IllegalArgumentException("invalid sm4 ciphertext, too short for iv");
				}
				byte[] iv = new byte[16];
				byte[] cipherText = new byte[all.length - 16];
				System.arraycopy(all, 0, iv, 0, 16);
				System.arraycopy(all, 16, cipherText, 0, cipherText.length);
				cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
				byte[] plain = cipher.doFinal(cipherText);
				return new String(plain, StandardCharsets.UTF_8);
			}
			else { // ECB
				cipher.init(Cipher.DECRYPT_MODE, key);
				byte[] plain = cipher.doFinal(all);
				return new String(plain, StandardCharsets.UTF_8);
			}
		}
		catch (Exception ex) {
			throw new IllegalStateException("SM4 decrypt error", ex);
		}
	}

	private static String normalizeAlgo(String algo) {
		if (algo == null || algo.isEmpty()) {
			return "SM4/CBC/PKCS5Padding";
		}
		String s = algo.toUpperCase(Locale.ROOT).trim();
		if ("SM4".equals(s)) {
			return "SM4/CBC/PKCS5Padding";
		}
		if (s.startsWith("SM4/")) {
			// Keep original case for JCE, but ensure it matches expected pattern
			return algo;
		}
		// Fallback to default when misconfigured
		return "SM4/CBC/PKCS5Padding";
	}

	private static String modeOf(String transformation) {
		String[] parts = transformation.split("/");
		return parts.length > 1 ? parts[1].toUpperCase(Locale.ROOT) : "CBC";
	}

	private static byte[] deriveKey(String password) {
		byte[] pwd = password == null ? new byte[0] : password.getBytes(StandardCharsets.UTF_8);
		byte[] key = new byte[16];
		int len = Math.min(16, pwd.length);
		System.arraycopy(pwd, 0, key, 0, len);
		// zero-padded
		return key;
	}

}
