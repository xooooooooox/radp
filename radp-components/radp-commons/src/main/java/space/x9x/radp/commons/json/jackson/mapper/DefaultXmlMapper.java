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

package space.x9x.radp.commons.json.jackson.mapper;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * A pre-configured Jackson XmlMapper with sensible defaults. This class extends Jackson's
 * XmlMapper and provides a default configuration suitable for most XML serialization and
 * deserialization needs. It includes settings for handling unknown properties and empty
 * beans during processing.
 *
 * @author IO x9x
 * @since 2024-09-23 13:53
 */
public class DefaultXmlMapper extends XmlMapper {

	@Serial
	private static final long serialVersionUID = -1966898079622236116L;

	/**
	 * Constructs a new DefaultXmlMapper with pre-configured settings.
	 * <p>
	 * This constructor initializes an XmlMapper with the following configurations:
	 * <ul>
	 * <li>Serialization inclusion set to ALWAYS (includes all properties)</li>
	 * <li>Ignores unknown properties during deserialization</li>
	 * <li>Disables failure on empty beans during serialization</li>
	 * </ul>
	 */
	public DefaultXmlMapper() {
		super();

		this.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

}
