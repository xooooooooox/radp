package space.x9x.radp.commons.regex;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import space.x9x.radp.commons.regex.pattern.RegexCache;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p><strong>使用示例：</strong></p>
 * <pre>{@code
 * public class Demo {
 *     public static void main(String[] args) {
 *         // 1) 判断整个字符串是否匹配正则
 *         boolean isMatch = RegexUtils.isMatch("\\d+", "12345");
 *         System.out.println(isMatch); // true
 *
 *         // 2) 查找目标字符串中是否存在匹配
 *         boolean found = RegexUtils.find("abc", "xxx abc yyy");
 *         System.out.println(found); // true
 *
 *         // 3) 分组匹配示例
 *         List<String> groups = RegexUtils.group("([a-z]+)", "abc def ghi");
 *         System.out.println(groups); // [abc, def, ghi]
 *
 *         // 4) 替换所有匹配项
 *         String replaced = RegexUtils.replaceAll("\\s+", "abc def", "_");
 *         System.out.println(replaced); // "abc_def"
 *     }
 * }
 * }</pre>
 *
 * @author x9x
 * @see space.x9x.radp.commons.regex.pattern.Regex
 * @since 2024-12-27 12:50
 */
@UtilityClass
public class RegexUtils {

    public static boolean isMatch(@NonNull String regex, @NonNull CharSequence input) {
        return Pattern.matches(regex, input);
    }

    public static boolean find(@NonNull String regex, @NonNull CharSequence input) {
        Pattern pattern = RegexCache.get(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(input).find();
    }

    public static List<String> group(@NonNull String regex, @NonNull CharSequence input) {
        List<String> matches = new ArrayList<>();
        Pattern pattern = RegexCache.get(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);
        int i = 1;
        while (matcher.find()) {
            matches.add(matcher.group(i));
            i++;
        }
        return matches;
    }

    public static String groupFirst(@NonNull String regex, @NonNull CharSequence input) {
        return group(regex, input, 1);
    }

    public static String group(@NonNull String regex, @NonNull CharSequence input, int groupIndex) {
        Pattern pattern = RegexCache.get(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }

    public static String replaceAll(@NonNull String regex, @NonNull CharSequence input, @NonNull String replacement) {
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

    public static String replaceFirst(@NonNull String regex, @NonNull CharSequence input, @NonNull String replacement) {
        Pattern pattern = RegexCache.get(regex, Pattern.DOTALL);
        return pattern.matcher(input).replaceFirst(replacement);
    }
}
