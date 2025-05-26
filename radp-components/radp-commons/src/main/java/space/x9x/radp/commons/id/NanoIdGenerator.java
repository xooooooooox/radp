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

package space.x9x.radp.commons.id;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author IO x9x
 * @since 2024-12-27 12:08
 */
@UtilityClass
public class NanoIdGenerator {

    /**
     * The default secure random number generator used for ID generation.
     * This provides cryptographically strong random numbers.
     */
    public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();
    private static final char[] DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    /**
     * The default size (length) of generated NanoIDs.
     * A length of 21 provides a good balance between uniqueness and size.
     */
    public static final int DEFAULT_SIZE = 21;

    /**
     * Generates a random NanoID using default settings.
     * <p>
     * This method uses the default random number generator, alphabet, and size
     * to create a secure, URL-friendly, unique identifier.
     *
     * @return a randomly generated NanoID string
     */
    public static String randomNanoId() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
    }

    /**
     * Generates a random NanoID with custom parameters.
     * <p>
     * This method allows customization of the random number generator, alphabet,
     * and size of the generated ID. It implements the NanoID algorithm to create
     * collision-resistant, unique identifiers.
     *
     * @param random   the random number generator to use
     * @param alphabet the characters to use for the ID
     * @param size     the length of the ID to generate
     * @return a randomly generated NanoID string
     * @throws IllegalArgumentException if any parameter is invalid
     */
    @SuppressWarnings("t")
    public static String randomNanoId(final Random random, final char[] alphabet, final int size) {
        if (random == null) {
            throw new IllegalArgumentException("random cannot be null.");
        }
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet cannot be null.");
        }
        if (alphabet.length == 0 || alphabet.length >= 256) {
            throw new IllegalArgumentException("alphabet must contain between 1 and 255 symbols.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than zero.");
        }

        final int mask = (2 << (int) Math.floor(Math.log(alphabet.length - 1) / Math.log(2))) - 1;
        final int step = (int) Math.ceil(1.6 * mask * size / alphabet.length);

        final StringBuilder idBuilder = new StringBuilder();
        while (true) {
            final byte[] bytes = new byte[step];
            random.nextBytes(bytes);
            for (int i = 0; i < step; i++) {
                final int alphabetIndex = bytes[i] & mask;
                if (alphabetIndex < alphabet.length) {
                    idBuilder.append(alphabet[alphabetIndex]);
                    if (idBuilder.length() == size) {
                        return idBuilder.toString();
                    }
                }
            }
        }
    }
}
