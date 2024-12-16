package com.x9x.radp.commons.env;

import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author x9x
 * @since 2024-09-30 13:03
 */
@UtilityClass
public class Charsets {
    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    public static final Charset US_ASCII = StandardCharsets.US_ASCII;
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final Charset UTF_16 = StandardCharsets.UTF_16;
    public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;
    public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;
    public static final String GBK_NAME = GBK.name();
    public static final String ISO_8859_1_NAME = ISO_8859_1.name();
    public static final String US_ASCII_NAME = US_ASCII.name();
    public static final String UTF_8_NAME = UTF_8.name();
    public static final String UTF_16_NAME = UTF_16.name();
    public static final String UTF_16BE_NAME = UTF_16BE.name();
    public static final String UTF_16LE_NAME = UTF_16LE.name();
}
