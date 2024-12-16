package com.x9x.radp.commons.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.x9x.radp.commons.json.jackson.exception.JacksonException;
import com.x9x.radp.commons.json.jackson.mapper.DefaultObjectMapper;
import com.x9x.radp.commons.json.jackson.mapper.DefaultXmlMapper;
import com.x9x.radp.commons.lang.StringUtils;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author x9x
 * @since 2024-09-23 13:45
 */
@UtilityClass
@Slf4j
@SuppressWarnings("java:S3252")
public class JacksonUtils {

    @Getter
    private static final ObjectMapper defaultObjectMapper = new DefaultObjectMapper();

    @Getter
    private static final XmlMapper defaultXmlMapper = new DefaultXmlMapper();

    // =============== to JSONString ===============

    public static <T> String toJSONString(T object) {
        return toJSONString(object, JsonInclude.Include.USE_DEFAULTS);
    }

    public static <T> String toJSONString(T object, JsonInclude.Include include) {
        return toJSONString(object, include, getDefaultObjectMapper());
    }

    public static String toJSONString(String text, Class<?> cls) {
        return toJSONString(text, cls, JsonInclude.Include.USE_DEFAULTS, getDefaultObjectMapper(), getDefaultXmlMapper());
    }

    public static <T> String toJSONString(T object, JsonInclude.Include include, ObjectMapper objectMapper) {
        if (objectMapper == null) {
            objectMapper = getDefaultObjectMapper();
        }
        try {
            return getObjectWriter(include, objectMapper).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    public static String toJSONString(String text, Class<?> cls, JsonInclude.Include include) {
        return toJSONString(text, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
    }

    public static String toJSONString(String text, Class<?> cls, JsonInclude.Include include,
                                      ObjectMapper objectMapper, XmlMapper xmlMapper) {
        if (objectMapper == null) {
            objectMapper = getDefaultObjectMapper();
        }
        if (xmlMapper == null) {
            xmlMapper = getDefaultXmlMapper();
        }
        try {
            Object object = xmlMapper.setSerializationInclusion(include).readValue(text, cls);
            return toJSONString(object, include, objectMapper);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    // ============================ to JSONStringPretty ============================

    public static <T> String toJSONStringPretty(T object, JsonInclude.Include include) {
        return toJSONStringPretty(object, include, getDefaultObjectMapper());
    }

    public static <T> String toJSONStringPretty(T object) {
        return toJSONStringPretty(object, JsonInclude.Include.USE_DEFAULTS);
    }

    public static <T> String toJSONStringPretty(T object, JsonInclude.Include include, ObjectMapper objectMapper) {
        if (objectMapper == null) {
            objectMapper = getDefaultObjectMapper();
        }
        try {
            return objectMapper.setSerializationInclusion(include).writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    // ============================ jsonString -> T ============================

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        return parseObject(text, typeReference, getDefaultObjectMapper());
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            return objectMapper.readValue(text, typeReference);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    public static <T> T parseObject(String text, Class<T> cls) {
        return parseObject(text, cls, getDefaultObjectMapper());
    }

    public static <T> T parseObject(String text, Class<T> cls, ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            return objectMapper.readValue(text, cls);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    public static <K, V, T> T parseObject(Map<K, V> map, Class<T> cls) {
        return parseObject(map, cls, getDefaultObjectMapper());
    }

    public static <K, V, T> T parseObject(Map<K, V> map, Class<T> cls, ObjectMapper objectMapper) {
        return objectMapper.convertValue(map, cls);
    }

    // ============================ jsonString -> Optional<T> ============================

    public static <T> Optional<T> parseObjectOptional(String text, TypeReference<T> typeReference) {
        return parseObjectOptional(text, typeReference, getDefaultObjectMapper());
    }

    /**
     * Parses the specified JSON string into an {@link Optional} object of the specified type.
     * <p>
     * This method will return an {@link Optional} containing the parsed object if the parsing is successful,
     * and an empty {@link Optional} otherwise.
     *
     * @param <T>           the type of the desired object
     * @param text          the JSON string to parse, may be null or empty
     * @param typeReference the type of the object to create
     * @return an {@link Optional} containing the parsed object, or an empty {@link Optional} if the
     * provided {@code text} is empty or null, or if an error occurs during parsing
     */
    public static <T> Optional<T> parseObjectOptional(String text, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(text)) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.readValue(text, typeReference));
        } catch (JsonProcessingException e) {
            log.error("json parse err, json:{}", text, e);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> parseObjectOptional(String text, Class<T> clazz) {
        return parseObjectOptional(text, clazz, getDefaultObjectMapper());
    }

    public static <T> Optional<T> parseObjectOptional(String text, Class<T> clazz, ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(text)) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.readValue(text, clazz));
        } catch (JsonProcessingException e) {
            log.error("json parse err, json:{}", text, e);
            return Optional.empty();
        }
    }

    // ============================ jsonString -> Map ============================

    public static <K, V> Map<K, V> parseMap(String text) {
        return parseMap(text, getDefaultObjectMapper());
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> parseMap(String text, ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(text, Map.class);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    // ============================ jsonString -> List<T> ============================

    public static <T> List<T> parseList(String text, Class<T> cls) {
        return parseList(text, cls, getDefaultObjectMapper());
    }

    public static <T> List<T> parseList(String text, Class<T> cls, ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }
        List<Map<Object, Object>> list;
        try {
            list = objectMapper.readValue(text, new TypeReference<List<Map<Object, Object>>>() {
            });
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
        List<T> result = new ArrayList<>();
        for (Map<Object, Object> map : list) {
            result.add(parseObject(map, cls, objectMapper));
        }
        return result;
    }

    // ============================ to toXMLString ============================

    public static String toXMLString(String text, Class<?> cls, JsonInclude.Include include) {
        return toXMLString(text, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
    }

    public static String toXMLString(String text, Class<?> cls, JsonInclude.Include include,
                                     ObjectMapper objectMapper, XmlMapper xmlMapper) {
        Object object = parseObject(text, cls, objectMapper);
        ObjectWriter objectWriter = getObjectWriter(include, xmlMapper);
        try {
            return StringUtils.trimToEmpty(objectWriter.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    public static String toXMLString(Object object) {
        return toXMLString(object, getDefaultXmlMapper());
    }

    public static String toXMLString(Object object, XmlMapper xmlMapper) {
        try {
            return xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    public static String toXMLString(String text, JsonInclude.Include include) throws JsonProcessingException {
        return toXMLString(text, include, getDefaultObjectMapper(), getDefaultXmlMapper());
    }

    public static String toXMLString(String text, JsonInclude.Include include, ObjectMapper objectMapper, XmlMapper xmlMapper) throws JsonProcessingException {
        if (objectMapper == null) {
            objectMapper = getDefaultObjectMapper();
        }
        if (xmlMapper == null) {
            xmlMapper = getDefaultXmlMapper();
        }
        ObjectWriter objectWriter = getObjectWriter(include, xmlMapper);
        return StringUtils.trimToEmpty(
                objectWriter.writeValueAsString(objectMapper.readTree(text)));
    }

    // ============================ to toXMLStringPretty ============================

    public static String toXMLStringPretty(Object object) {
        return toXMLStringPretty(object, getDefaultXmlMapper());
    }

    public static String toXMLStringPretty(Object object, XmlMapper xmlMapper) {
        if (xmlMapper == null) {
            xmlMapper = getDefaultXmlMapper();
        }
        try {
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    public static String toXMLStringPretty(String text, Class<?> cls, JsonInclude.Include include) {
        return toXMLStringPretty(text, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
    }

    public static String toXMLStringPretty(String text, Class<?> cls, JsonInclude.Include include,
                                           ObjectMapper objectMapper, XmlMapper xmlMapper) {
        Object object = parseObject(text, cls, objectMapper);
        ObjectWriter objectWriter = xmlMapper.setSerializationInclusion(include).writerWithDefaultPrettyPrinter();
        try {
            return StringUtils.trimToEmpty(objectWriter.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    // ============================ xmlString -> T ============================

    public static <T> T parseXMLObject(String xml, TypeReference<T> typeReference) {
        return parseXMLObject(xml, typeReference, getDefaultXmlMapper());
    }

    public static <T> T parseXMLObject(String xml, TypeReference<T> typeReference, XmlMapper xmlMapper) {
        if (StringUtils.isEmpty(xml)) {
            return null;
        }
        try {
            return xmlMapper.readValue(xml, typeReference);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    public static <T> T parseXMLObject(String xml, Class<T> cls) {
        return parseXMLObject(xml, cls, getDefaultXmlMapper());
    }

    public static <T> T parseXMLObject(String xml, Class<T> cls, XmlMapper xmlMapper) {
        if (StringUtils.isEmpty(xml)) {
            return null;
        }
        try {
            return xmlMapper.readValue(xml, cls);
        } catch (JsonProcessingException e) {
            throw new JacksonException(e);
        }
    }

    // ============================ xmlString -> Optional<T> ============================

    public static <T> Optional<T> parseXMLObjectOptional(String xml, TypeReference<T> typeReference) {
        return parseXMLObjectOptional(xml, typeReference, getDefaultXmlMapper());
    }

    public static <T> Optional<T> parseXMLObjectOptional(String xml, TypeReference<T> typeReference, XmlMapper xmlMapper) {
        if (StringUtils.isEmpty(xml)) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(xmlMapper.readValue(xml, typeReference));
        } catch (JsonProcessingException e) {
            log.error("xml parse error, xml: {}", xml, e);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> parseXMLObjectOptional(String xml, Class<T> clazz) {
        return parseXMLObjectOptional(xml, clazz, getDefaultXmlMapper());
    }

    public static <T> Optional<T> parseXMLObjectOptional(String xml, Class<T> clazz, XmlMapper xmlMapper) {
        if (StringUtils.isEmpty(xml)) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(xmlMapper.readValue(xml, clazz));
        } catch (JsonProcessingException e) {
            log.error("xml parse error, xml: {}", xml, e);
            return Optional.empty();
        }
    }

    // ============================ private ============================

    private static ObjectWriter getObjectWriter(JsonInclude.Include include, ObjectMapper objectMapper) {
        return objectMapper.setSerializationInclusion(include).writer();
    }

    private static ObjectWriter getObjectWriter(JsonInclude.Include include, XmlMapper xmlMapper) {
        return xmlMapper.setSerializationInclusion(include).writer();
    }
}
