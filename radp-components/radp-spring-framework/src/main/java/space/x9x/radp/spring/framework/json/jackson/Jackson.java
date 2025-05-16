package space.x9x.radp.spring.framework.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import space.x9x.radp.commons.json.JacksonUtils;
import space.x9x.radp.spring.framework.json.JSON;

import java.util.List;

/**
 * @author x9x
 * @since 2024-09-26 11:37
 */
public class Jackson implements JSON {

    @Override
    public <T> String toJSONString(T object) {
        return JacksonUtils.toJSONString(object);
    }

    @Override
    public <T> T parseObject(String text, Class<T> clazz) {
        return JacksonUtils.parseObject(text, clazz);
    }

    @Override
    public <T> List<T> parseList(String text, Class<T> clazz) {
        return JacksonUtils.parseList(text, clazz);
    }

    /**
     * Returns the default ObjectMapper instance.
     * This method provides access to the configured Jackson ObjectMapper for custom serialization/deserialization needs.
     *
     * @return the default ObjectMapper instance from JacksonUtils
     */
    public ObjectMapper getObjectMapper() {
        return JacksonUtils.getDefaultObjectMapper();
    }
}
