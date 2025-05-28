package space.x9x.radp.commons.json.jackson.mapper;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
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
     *   <li>Serialization inclusion set to ALWAYS (includes all properties)</li>
     *   <li>Ignores unknown properties during deserialization</li>
     *   <li>Disables failure on empty beans during serialization</li>
     * </ul>
     */
    public DefaultXmlMapper() {
        super();

        this.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
