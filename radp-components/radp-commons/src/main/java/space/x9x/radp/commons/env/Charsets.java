package space.x9x.radp.commons.env;

import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author x9x
 * @since 2024-09-30 13:03
 */
@UtilityClass
public class Charsets {
    /**
     * GBK charset encoding.
     */
    public static final Charset GBK = Charset.forName("GBK");

    /**
     * ISO-8859-1 charset encoding.
     */
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

    /**
     * US-ASCII charset encoding.
     */
    public static final Charset US_ASCII = StandardCharsets.US_ASCII;

    /**
     * UTF-8 charset encoding.
     */
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * UTF-16 charset encoding.
     */
    public static final Charset UTF_16 = StandardCharsets.UTF_16;

    /**
     * UTF-16BE (Big Endian) charset encoding.
     */
    public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;

    /**
     * UTF-16LE (Little Endian) charset encoding.
     */
    public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;
    /**
     * Name of the GBK charset.
     */
    public static final String GBK_NAME = GBK.name();

    /**
     * Name of the ISO-8859-1 charset.
     */
    public static final String ISO_8859_1_NAME = ISO_8859_1.name();

    /**
     * Name of the US-ASCII charset.
     */
    public static final String US_ASCII_NAME = US_ASCII.name();

    /**
     * Name of the UTF-8 charset.
     */
    public static final String UTF_8_NAME = UTF_8.name();

    /**
     * Name of the UTF-16 charset.
     */
    public static final String UTF_16_NAME = UTF_16.name();

    /**
     * Name of the UTF-16BE (Big Endian) charset.
     */
    public static final String UTF_16BE_NAME = UTF_16BE.name();

    /**
     * Name of the UTF-16LE (Little Endian) charset.
     */
    public static final String UTF_16LE_NAME = UTF_16LE.name();
}
