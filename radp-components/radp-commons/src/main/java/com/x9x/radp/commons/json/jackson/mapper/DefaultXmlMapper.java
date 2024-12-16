package com.x9x.radp.commons.json.jackson.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author x9x
 * @since 2024-09-23 13:53
 */
public class DefaultXmlMapper extends XmlMapper {

    private static final long serialVersionUID = -1966898079622236116L;

    public DefaultXmlMapper() {
        super();

        this.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
