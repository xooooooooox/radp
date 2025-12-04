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

package space.x9x.radp.commons.lang;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Utility class for array operations. This class extends Apache Commons Lang's ArrayUtils
 * to provide convenient access to array manipulation methods such as adding, removing,
 * and checking elements in arrays of various types.
 *
 * @author x9x
 * @since 2024-09-24 23:10
 */
@UtilityClass
public class ArrayUtil {

	public static boolean isEmpty(final boolean[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(final byte[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(final char[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(final double[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(final float[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(final int[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(final long[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(final Object[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(final short[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isNotEmpty(final boolean[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean isNotEmpty(final byte[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean isNotEmpty(final char[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean isNotEmpty(final double[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean isNotEmpty(final float[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean isNotEmpty(final int[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean isNotEmpty(final long[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean isNotEmpty(final short[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static <T> boolean isNotEmpty(final T[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean contains(final boolean[] array, final boolean valueToFind) {
		return ArrayUtils.contains(array, valueToFind);
	}

	public static boolean contains(final byte[] array, final byte valueToFind) {
		return ArrayUtils.contains(array, valueToFind);
	}

	public static boolean contains(final char[] array, final char valueToFind) {
		return ArrayUtils.contains(array, valueToFind);
	}

	public static boolean contains(final double[] array, final double valueToFind) {
		return ArrayUtils.contains(array, valueToFind);
	}

	public static boolean contains(final double[] array, final double valueToFind, final double tolerance) {
		return ArrayUtils.contains(array, valueToFind, tolerance);
	}

	public static boolean contains(final float[] array, final float valueToFind) {
		return ArrayUtils.contains(array, valueToFind);
	}

	public static boolean contains(final int[] array, final int valueToFind) {
		return ArrayUtils.contains(array, valueToFind);
	}

	public static boolean contains(final long[] array, final long valueToFind) {
		return ArrayUtils.contains(array, valueToFind);
	}

	public static boolean contains(final Object[] array, final Object objectToFind) {
		return ArrayUtils.contains(array, objectToFind);
	}

	public static boolean contains(final short[] array, final short valueToFind) {
		return ArrayUtils.contains(array, valueToFind);
	}

	public static <T> T get(final T[] array, final int index) {
		return ArrayUtils.get(array, index);
	}

	public static <T> T get(final T[] array, final int index, final T defaultValue) {
		return ArrayUtils.get(array, index, defaultValue);
	}

}
