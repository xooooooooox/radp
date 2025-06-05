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

package space.x9x.radp.commons.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import space.x9x.radp.commons.json.jackson.exception.JacksonException;
import space.x9x.radp.commons.json.jackson.mapper.DefaultObjectMapper;
import space.x9x.radp.commons.json.jackson.mapper.DefaultXmlMapper;
import space.x9x.radp.commons.lang.StringUtils;

/**
 * Utility class for JSON and XML processing using Jackson. This class provides a
 * comprehensive set of methods for converting between Java objects, JSON strings, and XML
 * strings, as well as parsing JSON and XML. It includes methods for pretty-printing,
 * handling Optional results, and working with various data structures like Maps and
 * Lists.
 *
 * @author IO x9x
 * @since 2024-09-23 13:45
 */
@UtilityClass
@Slf4j
@SuppressWarnings("java:S3252")
public class JacksonUtils {

	/**
	 * The default ObjectMapper instance used for JSON operations. This mapper is
	 * configured with the settings defined in DefaultObjectMapper.
	 */
	@Getter
	private static final ObjectMapper defaultObjectMapper = new DefaultObjectMapper();

	/**
	 * The default XmlMapper instance used for XML operations. This mapper is configured
	 * with the settings defined in DefaultXmlMapper.
	 */
	@Getter
	private static final XmlMapper defaultXmlMapper = new DefaultXmlMapper();

	// =============== to JSONString ===============

	/**
	 * Converts an object to a JSON string using default inclusion settings.
	 * @param <T> the type of the object
	 * @param object the object to convert
	 * @return the JSON string representation of the object
	 */
	public static <T> String toJSONString(T object) {
		return toJSONString(object, JsonInclude.Include.USE_DEFAULTS);
	}

	/**
	 * Converts an object to a JSON string with specified inclusion settings.
	 * @param <T> the type of the object
	 * @param object the object to convert
	 * @param include the inclusion strategy for properties
	 * @return the JSON string representation of the object
	 */
	public static <T> String toJSONString(T object, JsonInclude.Include include) {
		return toJSONString(object, include, getDefaultObjectMapper());
	}

	/**
	 * Converts an XML string to a JSON string using the default settings.
	 * @param text the XML string to convert
	 * @param cls the class type to bind the XML to
	 * @return the JSON string representation
	 */
	public static String toJSONString(String text, Class<?> cls) {
		return toJSONString(text, cls, JsonInclude.Include.USE_DEFAULTS, getDefaultObjectMapper(),
				getDefaultXmlMapper());
	}

	/**
	 * Converts an object to a JSON string with specified inclusion settings and object
	 * mapper.
	 * @param <T> the type of the object
	 * @param object the object to convert
	 * @param include the inclusion strategy for properties
	 * @param objectMapper the object mapper to use for conversion
	 * @return the JSON string representation of the object
	 * @throws JacksonException if an error occurs during JSON processing
	 */
	public static <T> String toJSONString(T object, JsonInclude.Include include, ObjectMapper objectMapper) {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		try {
			return getObjectWriter(include, objectMapper).writeValueAsString(object);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	/**
	 * Converts an XML string to a JSON string with specified inclusion settings.
	 * @param text the XML string to convert
	 * @param cls the class type to bind the XML to
	 * @param include the inclusion strategy for properties
	 * @return the JSON string representation
	 */
	public static String toJSONString(String text, Class<?> cls, JsonInclude.Include include) {
		return toJSONString(text, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	/**
	 * Converts an XML string to a JSON string with specified inclusion settings and
	 * mappers.
	 * @param text the XML string to convert
	 * @param cls the class type to bind the XML to
	 * @param include the inclusion strategy for properties
	 * @param objectMapper the object mapper to use for JSON conversion
	 * @param xmlMapper the XML mapper to use for XML parsing
	 * @return the JSON string representation
	 * @throws JacksonException if an error occurs during processing
	 */
	public static String toJSONString(String text, Class<?> cls, JsonInclude.Include include, ObjectMapper objectMapper,
			XmlMapper xmlMapper) {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		if (xmlMapper == null) {
			xmlMapper = getDefaultXmlMapper();
		}
		try {
			Object object = xmlMapper.setSerializationInclusion(include).readValue(text, cls);
			return toJSONString(object, include, objectMapper);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	// ============================ to JSONStringPretty ============================

	/**
	 * Converts an object to a pretty-printed JSON string with specified inclusion
	 * settings.
	 * @param <T> the type of the object
	 * @param object the object to convert
	 * @param include the inclusion strategy for properties
	 * @return the pretty-printed JSON string representation
	 */
	public static <T> String toJSONStringPretty(T object, JsonInclude.Include include) {
		return toJSONStringPretty(object, include, getDefaultObjectMapper());
	}

	/**
	 * Converts an object to a pretty-printed JSON string using default inclusion
	 * settings.
	 * @param <T> the type of the object
	 * @param object the object to convert
	 * @return the pretty-printed JSON string representation
	 */
	public static <T> String toJSONStringPretty(T object) {
		return toJSONStringPretty(object, JsonInclude.Include.USE_DEFAULTS);
	}

	/**
	 * Converts an object to a pretty-printed JSON string with specified inclusion
	 * settings and object mapper.
	 * @param <T> the type of the object
	 * @param object the object to convert
	 * @param include the inclusion strategy for properties
	 * @param objectMapper the object mapper to use for conversion
	 * @return the pretty-printed JSON string representation
	 * @throws JacksonException if an error occurs during JSON processing
	 */
	public static <T> String toJSONStringPretty(T object, JsonInclude.Include include, ObjectMapper objectMapper) {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		try {
			return objectMapper.setSerializationInclusion(include)
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(object);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	// ============================ jsonString -> T ============================

	/**
	 * Parses a JSON string into an object of the specified type using a TypeReference.
	 * @param <T> the target type
	 * @param text the JSON string to parse
	 * @param typeReference the type reference describing the target type
	 * @return the parsed object, or null if the input is empty
	 */
	public static <T> T parseObject(String text, TypeReference<T> typeReference) {
		return parseObject(text, typeReference, getDefaultObjectMapper());
	}

	/**
	 * Parses a JSON string into an object of the specified type using a TypeReference and
	 * custom object mapper.
	 * @param <T> the target type
	 * @param text the JSON string to parse
	 * @param typeReference the type reference describing the target type
	 * @param objectMapper the object mapper to use for parsing
	 * @return the parsed object, or null if the input is empty
	 * @throws JacksonException if an error occurs during JSON processing
	 */
	public static <T> T parseObject(String text, TypeReference<T> typeReference, ObjectMapper objectMapper) {
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		try {
			return objectMapper.readValue(text, typeReference);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	/**
	 * Parses a JSON string into an object of the specified class type.
	 * @param <T> the target type
	 * @param text the JSON string to parse
	 * @param cls the class of the target type
	 * @return the parsed object, or null if the input is empty
	 */
	public static <T> T parseObject(String text, Class<T> cls) {
		return parseObject(text, cls, getDefaultObjectMapper());
	}

	/**
	 * Parses a JSON string into an object of the specified class type using a custom
	 * object mapper.
	 * @param <T> the target type
	 * @param text the JSON string to parse
	 * @param cls the class of the target type
	 * @param objectMapper the object mapper to use for parsing
	 * @return the parsed object, or null if the input is empty
	 * @throws JacksonException if an error occurs during JSON processing
	 */
	public static <T> T parseObject(String text, Class<T> cls, ObjectMapper objectMapper) {
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		try {
			return objectMapper.readValue(text, cls);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	/**
	 * Converts a Map to an object of the specified class type.
	 * @param <K> the type of keys in the map
	 * @param <V> the type of values in the map
	 * @param <T> the target type
	 * @param map the map to convert
	 * @param cls the class of the target type
	 * @return the converted object
	 */
	public static <K, V, T> T parseObject(Map<K, V> map, Class<T> cls) {
		return parseObject(map, cls, getDefaultObjectMapper());
	}

	/**
	 * Converts a Map to an object of the specified class type using a custom object
	 * mapper.
	 * @param <K> the type of keys in the map
	 * @param <V> the type of values in the map
	 * @param <T> the target type
	 * @param map the map to convert
	 * @param cls the class of the target type
	 * @param objectMapper the object mapper to use for conversion
	 * @return the converted object
	 */
	public static <K, V, T> T parseObject(Map<K, V> map, Class<T> cls, ObjectMapper objectMapper) {
		return objectMapper.convertValue(map, cls);
	}

	// ============================ jsonString -> Optional<T> ============================

	/**
	 * Parses the specified JSON string into an {@link Optional} object of the specified
	 * type. This method uses the default ObjectMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param text the JSON string to parse, may be null or empty
	 * @param typeReference the type reference of the object to create
	 * @return an {@link Optional} containing the parsed object, or an empty
	 * {@link Optional} if parsing fails
	 */
	public static <T> Optional<T> parseObjectOptional(String text, TypeReference<T> typeReference) {
		return parseObjectOptional(text, typeReference, getDefaultObjectMapper());
	}

	/**
	 * Parses the specified JSON string into an {@link Optional} object of the specified
	 * type.
	 * <p>
	 * This method will return an {@link Optional} containing the parsed object if the
	 * parsing is successful, and an empty {@link Optional} otherwise.
	 * @param <T> the type of the desired object
	 * @param text the JSON string to parse, may be null or empty
	 * @param typeReference the type of the object to create
	 * @param objectMapper the ObjectMapper to use for parsing
	 * @return an {@link Optional} containing the parsed object, or an empty
	 * {@link Optional} if the provided {@code text} is empty or null, or if an error
	 * occurs during parsing
	 */
	public static <T> Optional<T> parseObjectOptional(String text, TypeReference<T> typeReference,
			ObjectMapper objectMapper) {
		if (StringUtils.isEmpty(text)) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable(objectMapper.readValue(text, typeReference));
		}
		catch (JsonProcessingException ex) {
			log.error("json parse err, json:{}", text, ex);
			return Optional.empty();
		}
	}

	/**
	 * Parses the specified JSON string into an {@link Optional} object of the specified
	 * class. This method uses the default ObjectMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param text the JSON string to parse, may be null or empty
	 * @param clazz the class of the object to create
	 * @return an {@link Optional} containing the parsed object, or an empty
	 * {@link Optional} if parsing fails
	 */
	public static <T> Optional<T> parseObjectOptional(String text, Class<T> clazz) {
		return parseObjectOptional(text, clazz, getDefaultObjectMapper());
	}

	/**
	 * Parses the specified JSON string into an {@link Optional} object of the specified
	 * class. This method allows specifying a custom ObjectMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param text the JSON string to parse, may be null or empty
	 * @param clazz the class of the object to create
	 * @param objectMapper the ObjectMapper to use for parsing
	 * @return an {@link Optional} containing the parsed object, or an empty
	 * {@link Optional} if parsing fails
	 */
	public static <T> Optional<T> parseObjectOptional(String text, Class<T> clazz, ObjectMapper objectMapper) {
		if (StringUtils.isEmpty(text)) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable(objectMapper.readValue(text, clazz));
		}
		catch (JsonProcessingException ex) {
			log.error("json parse err, json:{}", text, ex);
			return Optional.empty();
		}
	}

	// ============================ jsonString -> Map ============================

	/**
	 * Parses the specified JSON string into a Map. This method uses the default
	 * ObjectMapper for parsing.
	 * @param <K> the type of keys in the map
	 * @param <V> the type of values in the map
	 * @param text the JSON string to parse, may be null or empty
	 * @return a Map containing the parsed data, or an empty Map if parsing fails
	 */
	public static <K, V> Map<K, V> parseMap(String text) {
		return parseMap(text, getDefaultObjectMapper());
	}

	/**
	 * Parses the specified JSON string into a Map. This method allows specifying a custom
	 * ObjectMapper for parsing.
	 * @param <K> the type of keys in the map
	 * @param <V> the type of values in the map
	 * @param text the JSON string to parse, may be null or empty
	 * @param objectMapper the ObjectMapper to use for parsing
	 * @return a Map containing the parsed data, or an empty Map if the input is empty
	 * @throws JacksonException if a parsing error occurs
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> parseMap(String text, ObjectMapper objectMapper) {
		if (StringUtils.isEmpty(text)) {
			return Collections.emptyMap();
		}
		try {
			return objectMapper.readValue(text, Map.class);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	// ============================ jsonString -> List<T> ============================

	/**
	 * Parses the specified JSON string into a List containing objects of the specified
	 * class. This method uses the default ObjectMapper for parsing.
	 * @param <T> the type of elements in the list
	 * @param text the JSON string to parse, may be null or empty
	 * @param cls the class of the elements to create
	 * @return a List containing the parsed objects, or an empty List if the input is
	 * empty
	 */
	public static <T> List<T> parseList(String text, Class<T> cls) {
		return parseList(text, cls, getDefaultObjectMapper());
	}

	/**
	 * Parses the specified JSON string into a List containing objects of the specified
	 * class. This method allows specifying a custom ObjectMapper for parsing.
	 * @param <T> the type of elements in the list
	 * @param text the JSON string to parse, may be null or empty
	 * @param cls the class of the elements to create
	 * @param objectMapper the ObjectMapper to use for parsing
	 * @return a List containing the parsed objects, or an empty List if the input is
	 * empty
	 * @throws JacksonException if a parsing error occurs
	 */
	public static <T> List<T> parseList(String text, Class<T> cls, ObjectMapper objectMapper) {
		if (StringUtils.isEmpty(text)) {
			return Collections.emptyList();
		}
		List<Map<Object, Object>> list;
		try {
			list = objectMapper.readValue(text, new TypeReference<>() {
			});
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
		List<T> result = new ArrayList<>();
		for (Map<Object, Object> map : list) {
			result.add(parseObject(map, cls, objectMapper));
		}
		return result;
	}

	// ============================ to toXMLString ============================

	/**
	 * Converts a JSON string to an XML string after parsing it to the specified class.
	 * This method uses the default ObjectMapper and XmlMapper for conversion.
	 * @param text the JSON string to convert
	 * @param cls the class to parse the JSON into before converting to XML
	 * @param include the inclusion strategy for properties
	 * @return the XML representation of the object
	 */
	public static String toXMLString(String text, Class<?> cls, JsonInclude.Include include) {
		return toXMLString(text, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	/**
	 * Converts a JSON string to an XML string after parsing it to the specified class.
	 * This method allows specifying custom ObjectMapper and XmlMapper for conversion.
	 * @param text the JSON string to convert
	 * @param cls the class to parse the JSON into before converting to XML
	 * @param include the inclusion strategy for properties
	 * @param objectMapper the ObjectMapper to use for parsing JSON
	 * @param xmlMapper the XmlMapper to use for generating XML
	 * @return the XML representation of the object
	 * @throws JacksonException if a conversion error occurs
	 */
	public static String toXMLString(String text, Class<?> cls, JsonInclude.Include include, ObjectMapper objectMapper,
			XmlMapper xmlMapper) {
		Object object = parseObject(text, cls, objectMapper);
		ObjectWriter objectWriter = getObjectWriter(include, xmlMapper);
		try {
			return StringUtils.trimToEmpty(objectWriter.writeValueAsString(object));
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	/**
	 * Converts an object directly to an XML string. This method uses the default
	 * XmlMapper for conversion.
	 * @param object the object to convert to XML
	 * @return the XML representation of the object
	 */
	public static String toXMLString(Object object) {
		return toXMLString(object, getDefaultXmlMapper());
	}

	/**
	 * Converts an object directly to an XML string. This method allows specifying a
	 * custom XmlMapper for conversion.
	 * @param object the object to convert to XML
	 * @param xmlMapper the XmlMapper to use for generating XML
	 * @return the XML representation of the object
	 * @throws JacksonException if a conversion error occurs
	 */
	public static String toXMLString(Object object, XmlMapper xmlMapper) {
		try {
			return xmlMapper.writeValueAsString(object);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	/**
	 * Converts a JSON string to an XML string. This method uses the default ObjectMapper
	 * and XmlMapper for conversion.
	 * @param text the JSON string to convert
	 * @param include the inclusion strategy for properties
	 * @return the XML representation of the JSON
	 * @throws JsonProcessingException if a parsing or conversion error occurs
	 */
	public static String toXMLString(String text, JsonInclude.Include include) throws JsonProcessingException {
		return toXMLString(text, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	/**
	 * Converts a JSON string to an XML string. This method allows specifying custom
	 * ObjectMapper and XmlMapper for conversion.
	 * @param text the JSON string to convert
	 * @param include the inclusion strategy for properties
	 * @param objectMapper the ObjectMapper to use for parsing JSON, or null to use the
	 * default
	 * @param xmlMapper the XmlMapper to use for generating XML, or null to use the
	 * default
	 * @return the XML representation of the JSON
	 * @throws JsonProcessingException if a parsing or conversion error occurs
	 */
	public static String toXMLString(String text, JsonInclude.Include include, ObjectMapper objectMapper,
			XmlMapper xmlMapper) throws JsonProcessingException {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		if (xmlMapper == null) {
			xmlMapper = getDefaultXmlMapper();
		}
		ObjectWriter objectWriter = getObjectWriter(include, xmlMapper);
		return StringUtils.trimToEmpty(objectWriter.writeValueAsString(objectMapper.readTree(text)));
	}

	// ============================ to toXMLStringPretty ============================

	/**
	 * Converts an object to a pretty-printed XML string. This method uses the default
	 * XmlMapper for conversion.
	 * @param object the object to convert to XML
	 * @return the pretty-printed XML representation of the object
	 */
	public static String toXMLStringPretty(Object object) {
		return toXMLStringPretty(object, getDefaultXmlMapper());
	}

	/**
	 * Converts an object to a pretty-printed XML string. This method allows specifying a
	 * custom XmlMapper for conversion.
	 * @param object the object to convert to XML
	 * @param xmlMapper the XmlMapper to use for generating XML, or null to use the
	 * default
	 * @return the pretty-printed XML representation of the object
	 * @throws JacksonException if a conversion error occurs
	 */
	public static String toXMLStringPretty(Object object, XmlMapper xmlMapper) {
		if (xmlMapper == null) {
			xmlMapper = getDefaultXmlMapper();
		}
		try {
			return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	/**
	 * Converts a JSON string to a pretty-printed XML string after parsing it to the
	 * specified class. This method uses the default ObjectMapper and XmlMapper for
	 * conversion.
	 * @param text the JSON string to convert
	 * @param cls the class to parse the JSON into before converting to XML
	 * @param include the inclusion strategy for properties
	 * @return the pretty-printed XML representation of the object
	 */
	public static String toXMLStringPretty(String text, Class<?> cls, JsonInclude.Include include) {
		return toXMLStringPretty(text, cls, include, getDefaultObjectMapper(), getDefaultXmlMapper());
	}

	/**
	 * Converts a JSON string to a pretty-printed XML string after parsing it to the
	 * specified class. This method allows specifying custom ObjectMapper and XmlMapper
	 * for conversion.
	 * @param text the JSON string to convert
	 * @param cls the class to parse the JSON into before converting to XML
	 * @param include the inclusion strategy for properties
	 * @param objectMapper the ObjectMapper to use for parsing JSON
	 * @param xmlMapper the XmlMapper to use for generating XML
	 * @return the pretty-printed XML representation of the object
	 * @throws JacksonException if a conversion error occurs
	 */
	public static String toXMLStringPretty(String text, Class<?> cls, JsonInclude.Include include,
			ObjectMapper objectMapper, XmlMapper xmlMapper) {
		Object object = parseObject(text, cls, objectMapper);
		ObjectWriter objectWriter = xmlMapper.setSerializationInclusion(include).writerWithDefaultPrettyPrinter();
		try {
			return StringUtils.trimToEmpty(objectWriter.writeValueAsString(object));
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	// ============================ xmlString -> T ============================

	/**
	 * Parses an XML string into an object of the specified type. This method uses the
	 * default XmlMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param xml the XML string to parse, may be null or empty
	 * @param typeReference the type reference of the object to create
	 * @return the parsed object, or null if the input is empty
	 */
	public static <T> T parseXMLObject(String xml, TypeReference<T> typeReference) {
		return parseXMLObject(xml, typeReference, getDefaultXmlMapper());
	}

	/**
	 * Parses an XML string into an object of the specified type. This method allows
	 * specifying a custom XmlMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param xml the XML string to parse, may be null or empty
	 * @param typeReference the type reference of the object to create
	 * @param xmlMapper the XmlMapper to use for parsing
	 * @return the parsed object, or null if the input is empty
	 * @throws JacksonException if a parsing error occurs
	 */
	public static <T> T parseXMLObject(String xml, TypeReference<T> typeReference, XmlMapper xmlMapper) {
		if (StringUtils.isEmpty(xml)) {
			return null;
		}
		try {
			return xmlMapper.readValue(xml, typeReference);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	/**
	 * Parses an XML string into an object of the specified class. This method uses the
	 * default XmlMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param xml the XML string to parse, may be null or empty
	 * @param cls the class of the object to create
	 * @return the parsed object, or null if the input is empty
	 */
	public static <T> T parseXMLObject(String xml, Class<T> cls) {
		return parseXMLObject(xml, cls, getDefaultXmlMapper());
	}

	/**
	 * Parses an XML string into an object of the specified class. This method allows
	 * specifying a custom XmlMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param xml the XML string to parse, may be null or empty
	 * @param cls the class of the object to create
	 * @param xmlMapper the XmlMapper to use for parsing
	 * @return the parsed object, or null if the input is empty
	 * @throws JacksonException if a parsing error occurs
	 */
	public static <T> T parseXMLObject(String xml, Class<T> cls, XmlMapper xmlMapper) {
		if (StringUtils.isEmpty(xml)) {
			return null;
		}
		try {
			return xmlMapper.readValue(xml, cls);
		}
		catch (JsonProcessingException ex) {
			throw new JacksonException(ex);
		}
	}

	// ============================ xmlString -> Optional<T> ============================

	/**
	 * Parses an XML string into an {@link Optional} object of the specified type. This
	 * method uses the default XmlMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param xml the XML string to parse, may be null or empty
	 * @param typeReference the type reference of the object to create
	 * @return an {@link Optional} containing the parsed object, or an empty
	 * {@link Optional} if parsing fails
	 */
	public static <T> Optional<T> parseXMLObjectOptional(String xml, TypeReference<T> typeReference) {
		return parseXMLObjectOptional(xml, typeReference, getDefaultXmlMapper());
	}

	/**
	 * Parses an XML string into an {@link Optional} object of the specified type. This
	 * method allows specifying a custom XmlMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param xml the XML string to parse, may be null or empty
	 * @param typeReference the type reference of the object to create
	 * @param xmlMapper the XmlMapper to use for parsing
	 * @return an {@link Optional} containing the parsed object, or an empty
	 * {@link Optional} if parsing fails
	 */
	public static <T> Optional<T> parseXMLObjectOptional(String xml, TypeReference<T> typeReference,
			XmlMapper xmlMapper) {
		if (StringUtils.isEmpty(xml)) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable(xmlMapper.readValue(xml, typeReference));
		}
		catch (JsonProcessingException ex) {
			log.error("xml parse error, xml: {}", xml, ex);
			return Optional.empty();
		}
	}

	/**
	 * Parses an XML string into an {@link Optional} object of the specified class. This
	 * method uses the default XmlMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param xml the XML string to parse, may be null or empty
	 * @param clazz the class of the object to create
	 * @return an {@link Optional} containing the parsed object, or an empty
	 * {@link Optional} if parsing fails
	 */
	public static <T> Optional<T> parseXMLObjectOptional(String xml, Class<T> clazz) {
		return parseXMLObjectOptional(xml, clazz, getDefaultXmlMapper());
	}

	/**
	 * Parses an XML string into an {@link Optional} object of the specified class. This
	 * method allows specifying a custom XmlMapper for parsing.
	 * @param <T> the type of the desired object
	 * @param xml the XML string to parse, may be null or empty
	 * @param clazz the class of the object to create
	 * @param xmlMapper the XmlMapper to use for parsing
	 * @return an {@link Optional} containing the parsed object, or an empty
	 * {@link Optional} if parsing fails
	 */
	public static <T> Optional<T> parseXMLObjectOptional(String xml, Class<T> clazz, XmlMapper xmlMapper) {
		if (StringUtils.isEmpty(xml)) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable(xmlMapper.readValue(xml, clazz));
		}
		catch (JsonProcessingException ex) {
			log.error("xml parse error, xml: {}", xml, ex);
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
