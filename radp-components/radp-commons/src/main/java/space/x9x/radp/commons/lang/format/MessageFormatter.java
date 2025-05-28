package space.x9x.radp.commons.lang.format;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * 用于格式化消息模板的工具类。该类提供了多种方法来将消息模板中的占位符替换为实际的参数值， 并返回一个包含格式化后的消息字符串、原始参数数组以及可能的异常对象的
 * {@link FormattingTuple}。
 *
 * <p>
 * 以下是几个使用示例：
 * </p>
 *
 * <pre>{@code
 * // 示例 1：单个参数
 * String message = MessageFormatter.format("Hello, {0}", "World").getMessage();
 * System.out.println(message); // 输出: Hello, World
 * }</pre>
 *
 * <pre>{@code
 * // 示例 2：两个参数
 * String message = MessageFormatter.format("Hello, {0} and {1}", "Alice", "Bob").getMessage();
 * System.out.println(message); // 输出: Hello, Alice and Bob
 * }</pre>
 *
 * <pre>{@code
 * // 示例 3：多个参数
 * String message = MessageFormatter.arrayFormat("Hello, {0}, {1}, and {2}", new Object[]{"Alice", "Bob", "Charlie"}).getMessage();
 * System.out.println(message); // 输出: Hello, Alice, Bob, and Charlie
 * }</pre>
 *
 * <pre>{@code
 * // 示例 4：包含异常
 * try {
 *     throw new FileNotFoundException("File not found");
 * } catch (FileNotFoundException e) {
 *     String message = MessageFormatter.arrayFormat("Error occurred: {0}", new Object[]{"File not found"}, e).getMessage();
 *     System.out.println(message); // 输出: Error occurred: File not found
 * }
 * }</pre>
 *
 * @author IO x9x
 * @since 2024-09-26 16:27
 */
@UtilityClass
public class MessageFormatter {
    private static final String DELIM_START = "{";
    private static final String DELIM_STOP = "}";
    private static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    /**
     * 格式化单个参数的消息模板。
     *
     * @param messagePattern 消息模板
     * @param arg            参数
     * @return 格式化后的结果
     */
    public static FormattingTuple format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, new Object[]{arg});
    }

    /**
     * 格式化两个参数的消息模板。
     *
     * @param messagePattern 消息模板
     * @param arg1           第一个参数
     * @param arg2           第二个参数
     * @return 格式化后的结果
     */
    public static FormattingTuple format(final String messagePattern, Object arg1, Object arg2) {
        return arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }

    /**
     * 格式化多个参数的消息模板。
     *
     * @param messagePattern 消息模板
     * @param argArray       参数数组
     * @return 格式化后的结果
     */
    public static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray) {
        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(argArray);
        Object[] args = argArray;
        if (throwableCandidate != null) {
            args = MessageFormatter.trimmedCopy(argArray);
        }
        return arrayFormat(messagePattern, args, throwableCandidate);
    }

    /**
     * 格式化多个参数的消息模板，并包含一个异常对象。
     *
     * @param messagePattern 消息模板
     * @param argArray       参数数组
     * @param throwable      异常对象
     * @return 格式化后的结果
     */
    public static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray, Throwable throwable) {

        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }

        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }

        int i = 0;
        int j;
        StringBuilder stringBuilder = new StringBuilder(messagePattern.length() + 50);

        int L;
        for (L = 0; L < argArray.length; L++) {
            j = messagePattern.indexOf(DELIM_STR, i);

            if (j == -1) {
                if (i == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwable);
                } else {
                    stringBuilder.append(messagePattern, i, messagePattern.length());
                    return new FormattingTuple(stringBuilder.toString(), argArray, throwable);
                }
            } else {
                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        L--;
                        stringBuilder.append(messagePattern, i, j - 1);
                        stringBuilder.append(DELIM_START);
                        i = j + 1;
                    } else {
                        stringBuilder.append(messagePattern, i, j - 1);
                        deeplyAppendParameter(stringBuilder, argArray[L], new HashMap<>());
                        i = j + 2;
                    }
                } else {
                    stringBuilder.append(messagePattern, i, j);
                    deeplyAppendParameter(stringBuilder, argArray[L], new HashMap<>());
                    i = j + 2;
                }
            }
        }
        stringBuilder.append(messagePattern, i, messagePattern.length());
        return new FormattingTuple(stringBuilder.toString(), argArray, throwable);
    }

    /**
     * 检查占位符是否被双转义。
     *
     * @param messagePattern      消息模板
     * @param delimiterStartIndex 占位符起始位置
     * @return 是否被双转义
     */
    private static boolean isEscapedDelimeter(String messagePattern, int delimiterStartIndex) {
        if (delimiterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimiterStartIndex - 1);
        return potentialEscape == ESCAPE_CHAR;
    }

    /**
     * 检查占位符是否被双转义。
     *
     * @param messagePattern      消息模板
     * @param delimiterStartIndex 占位符起始位置
     * @return 是否被双转义
     */
    public static boolean isDoubleEscaped(String messagePattern, int delimiterStartIndex) {
        return delimiterStartIndex >= 2 && messagePattern.charAt(delimiterStartIndex - 2) == ESCAPE_CHAR;
    }

    /**
     * 递归处理参数并追加到字符串构建器中。
     *
     * @param stringBuilder 字符串构建器
     * @param o             参数
     * @param seenMap       已处理的数组映射
     */
    private static void deeplyAppendParameter(StringBuilder stringBuilder, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            stringBuilder.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            safeObjectAppend(stringBuilder, o);
        } else {
            if (o instanceof boolean[]) {
                booleanArrayAppend(stringBuilder, (boolean[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(stringBuilder, (byte[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(stringBuilder, (char[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(stringBuilder, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(stringBuilder, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(stringBuilder, (long[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(stringBuilder, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(stringBuilder, (double[]) o);
            } else {
                objectArrayAppend(stringBuilder, (Object[]) o, seenMap);
            }
        }
    }

    /**
     * 安全地追加对象到字符串构建器中。
     *
     * @param stringBuilder 字符串构建器
     * @param o             对象
     */
    private static void safeObjectAppend(StringBuilder stringBuilder, Object o) {
        try {
            String oAsString = o.toString();
            stringBuilder.append(oAsString);
        } catch (Throwable t) {
            stringBuilder.append("[FAILED toString()]");
        }

    }

    /**
     * 追加对象数组到字符串构建器中。
     *
     * @param stringBuilder 字符串构建器
     * @param a             对象数组
     * @param seenMap       已处理的数组映射
     */
    private static void objectArrayAppend(StringBuilder stringBuilder, Object[] a, Map<Object[], Object> seenMap) {
        stringBuilder.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(stringBuilder, a[i], seenMap);
                if (i != len - 1)
                    stringBuilder.append(", ");
            }
            seenMap.remove(a);
        } else {
            stringBuilder.append("...");
        }
        stringBuilder.append(']');
    }

    private static void booleanArrayAppend(StringBuilder stringBuilder, boolean[] a) {
        stringBuilder.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            stringBuilder.append(a[i]);
            if (i != len - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(']');
    }

    private static void byteArrayAppend(StringBuilder stringBuilder, byte[] a) {
        stringBuilder.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            stringBuilder.append(a[i]);
            if (i != len - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(']');
    }

    private static void charArrayAppend(StringBuilder stringBuilder, char[] a) {
        stringBuilder.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            stringBuilder.append(a[i]);
            if (i != len - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(']');
    }

    private static void shortArrayAppend(StringBuilder stringBuilder, short[] a) {
        stringBuilder.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            stringBuilder.append(a[i]);
            if (i != len - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(']');
    }

    private static void intArrayAppend(StringBuilder stringBuilder, int[] a) {
        stringBuilder.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            stringBuilder.append(a[i]);
            if (i != len - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(']');
    }

    private static void longArrayAppend(StringBuilder stringBuilder, long[] a) {
        stringBuilder.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            stringBuilder.append(a[i]);
            if (i != len - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(']');
    }

    private static void floatArrayAppend(StringBuilder stringBuilder, float[] a) {
        stringBuilder.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            stringBuilder.append(a[i]);
            if (i != len - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(']');
    }

    private static void doubleArrayAppend(StringBuilder stringBuilder, double[] a) {
        stringBuilder.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            stringBuilder.append(a[i]);
            if (i != len - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(']');
    }

    /**
     * Extracts a Throwable from the last element of an array if it is a Throwable.
     * <p>
     * This method is useful for message formatting patterns where the last argument
     * might be an exception that should be handled specially.
     *
     * @param argArray the array to check for a Throwable as its last element
     * @return the last element as a Throwable if it is one, or null otherwise
     */
    public static Throwable getThrowableCandidate(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }

        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }

        return null;
    }

    /**
     * Creates a copy of an array without its last element.
     * <p>
     * This method is typically used in conjunction with {@link #getThrowableCandidate}
     * to separate the exception from the rest of the arguments for message formatting.
     *
     * @param argArray the array to trim (must not be null or empty)
     * @return a new array containing all elements except the last one
     * @throws IllegalStateException if the input array is null or empty
     */
    public static Object[] trimmedCopy(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }

        final int trimmedLen = argArray.length - 1;
        Object[] trimmed = new Object[trimmedLen];
        if (trimmedLen > 0) {
            System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
        }
        return trimmed;
    }
}
